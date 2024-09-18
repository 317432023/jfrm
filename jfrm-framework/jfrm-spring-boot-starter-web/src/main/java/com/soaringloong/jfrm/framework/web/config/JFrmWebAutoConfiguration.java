package com.soaringloong.jfrm.framework.web.config;

import com.cmpt.oss.upload.config.OssProperties;
import com.comm.pojo.enums.WebFilterOrderEnum;
import com.frm.springmvc.*;
import com.ruoyi.common.config.RuoYiConfig;
import com.soaringloong.jfrm.framework.web.core.filter.CacheRequestBodyFilter;
import com.soaringloong.jfrm.framework.web.core.util.WebFrameworkUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 从Spring Boot
 * 2.2版本开始，如果你的配置属性类是以组件（如使用@Component注解）的形式注册的，那么你不需要显式地使用@EnableConfigurationProperties注解。Spring
 * Boot会自动配置这些属性类。
 * 使用@EnableConfigurationProperties可以让你的配置属性类保持轻量级，无需添加如@Component这样的Spring组件注解，同时也避免了类路径扫描的开销。
 *
 * @EnableConfigurationProperties注解提供了一种集中式的方式来管理和绑定应用的配置属性，使得代码更加清晰和易于维护
 */
@AutoConfiguration
@EnableConfigurationProperties({ WebProperties.class, SettingProperties.class })
public class JFrmWebAutoConfiguration implements WebMvcConfigurer {

	@Resource
	private WebProperties webProperties;

	@Resource
	private SettingProperties settingProperties;

	/**
	 * ！！！要求主程序扫描 com.cmpt.oss 名称空间
	 */
	@Resource
	private OssProperties ossProperties;

	/**
	 * springmvc代码方式的资源文件查找 <b>实际上也可以在 springBoot 的 application*.yml
	 * 中配置spring.mvc.static-path-pattern和 spring.resources.static-locations
	 * 但是这种方式更加灵活，如果同时配置的话最终生效以代码方式为准 </b>
	 * <p>
	 * 兼容 swagger 和 knife4j
	 * </p>
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		// ruoyi
		registry.addResourceHandler("/profile/**").addResourceLocations("file:" + RuoYiConfig.getProfile() + "/");

		// knife4j+swagger
		registry.addResourceHandler("/doc.html", "/swagger-ui.html")
			.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/swagger-ui/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
			.setCacheControl(CacheControl.maxAge(5L, TimeUnit.HOURS).cachePublic());

		// upload
		registry.addResourceHandler("/upload/**")
			.addResourceLocations("file:" + ossProperties.getResPath() + "/upload/",
					"file:" + RuoYiConfig.getProfile() + "res/upload/");
		String[] locations = settingProperties.getStaticCustomLocations().split(",");
		// 文件上传后的资源文件访问路径及其对应的目录（建议仅用于测试用途）
		registry.addResourceHandler("/upload/**")
			.addResourceLocations(locations)
			.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
		registry.addResourceHandler("/**").addResourceLocations(locations);
	}

	/**
	 * 注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new GlobalInterceptor()).addPathPatterns("/**");
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// 重写url路径匹配(忽略大小写敏感)
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		antPathMatcher.setCaseSensitive(false);
		configurePathMatch(configurer, webProperties.getAdminApi());
		configurePathMatch(configurer, webProperties.getMemberApi());
	}

	/**
	 * 设置 API 前缀，仅仅匹配 controller 包下的
	 * @param configurer 配置
	 * @param api API 配置
	 */
	private void configurePathMatch(PathMatchConfigurer configurer, WebProperties.Api api) {
		AntPathMatcher antPathMatcher = new AntPathMatcher(".");
		// 给符合匹配规则的控制器增加访问前缀 仅仅匹配 controller 包
		configurer.addPathPrefix(api.getPrefix(), clazz -> clazz.isAnnotationPresent(RestController.class)
				&& antPathMatcher.match(api.getController(), clazz.getPackage().getName()));
	}

	@Bean
	@SuppressWarnings("InstantiationOfUtilityClass")
	public WebFrameworkUtils webFrameworkUtils(WebProperties webProperties) {
		// 由于 WebFrameworkUtils 需要使用到 webProperties 属性，所以注册为一个 Bean
		return new WebFrameworkUtils(webProperties);
	}

	// ========== Filter 相关 ==========

	/// **
	// * 设置全局UTF-8编码过滤器
	// * @return
	// */
	// @Bean
	// public FilterRegistrationBean<CharacterEncodingFilter>
	/// characterFilterRegistration() {
	// FilterRegistrationBean<CharacterEncodingFilter> registration = new
	/// FilterRegistrationBean<>();
	// registration.setFilter(new CharacterEncodingFilter());
	// registration.addUrlPatterns("/*");
	// registration.addInitParameter("encoding", "UTF-8");
	// registration.setName("characterEncodingFilter");
	// return registration;
	// }

	@Bean
	public CorsFilter corsFilter() {
		// 创建 CorsConfiguration 对象
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*"); // 设置访问源地址
		config.addAllowedHeader("*"); // 设置访问源请求头
		config.addAllowedMethod("*"); // 设置访问源请求方法
		// 创建 UrlBasedCorsConfigurationSource 对象
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config); // 对接口配置跨域设置
		return new CorsFilter(source);
	}

	/**
	 * 创建 CorsFilter Bean，解决跨域问题
	 * <p>
	 * 对所有路径生效，可以删除所有控制器上的@CrossOrigin了
	 * </p>
	 */
	@ConditionalOnProperty(prefix = "jfrm.web.filter", name = "cors", havingValue = "true")
	// 如果spring.mvc.corsFilter非空并且值为true才生效
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilterBean() {
		return createFilterBean(corsFilter(), WebFilterOrderEnum.CORS_FILTER);
	}

	/**
	 * 创建 RequestBodyCacheFilter Bean，可重复读取请求内容
	 */
	@Bean
	public FilterRegistrationBean<CacheRequestBodyFilter> requestBodyCacheFilter() {
		return createFilterBean(new CacheRequestBodyFilter(), WebFilterOrderEnum.REQUEST_BODY_CACHE_FILTER);
	}

	public static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
		FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
		bean.setOrder(order);
		return bean;
	}

	/**
	 * 注意：仅针对 普通字符串（非 JSON ）的 提交 spring 接收 请求全局日期转换器，仅针对请求头为 Content-Type 为
	 * application/x-www-form-urlencoded 的表单提交
	 * <p>
	 * 关于日期转换 spring 将前端 json 转后端 pojo 底层使用的是Json序列化Jackson工具（HttpMessgeConverter，见
	 * JFrmJacksonAutoConfiguration） spring 而时间日期字符串作为普通请求参数传入时，转换用的是 Converter（也就是
	 * Content-Type 为 application/x-www-form-urlencoded 的表单提交） 两者在处理方式上有区别。
	 * <p>
	 * 后端 接收前端(非 JSON ) 请求传递 Date 的格式 配置 spring.mvc.date-format=yyyy-MM-dd HH:mm:ss 仅适用于
	 * GET DELETE 请求 或者直接在接收的请求参数中标注 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 仅适用于
	 * GET DELETE 请求 使用自定义参数转换器 参见 CustomDateConverter 仅适用于 POST PUT PATCH 请求 或者 使用
	 * ControllerAdvice 配合 initBinder，参见 ERP 的控制器基类 BaseController 仅适用于 POST PUT PATCH 请求
	 * 支持java8日期api
	 * <p>
	 * 后端 接收和响应 前端 JSON (spring jackson) 体的 日期序列化 格式 配置 spring.jackson.time-zone=GMT+8
	 * spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
	 * spring.jackson.serialization.write-dates-as-timestamps=false
	 * 或者直接在响应对象日期类型属性中标注 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =
	 * "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8") 不支持java8日期api，要求输入和输出使用统一的格式 重写
	 * Jackson的JSON序列化和反序列化 行为 适合大型项目在基础包中全局设置 见 JsonUtils.makeCustomObjectMapper()
	 * <p>
	 * <a href="https://www.csdn.net/tags/MtjaggxsNzU0NTUtYmxvZwO0O0OO0O0O.html">...</a>
	 * </p>
	 * @return
	 */
	@Bean
	public ConversionService getConversionService() {
		ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
		Set<Converter<String, ?>> converters = new HashSet<>();
		// 注册全局范围内对表单日期 java.util.Date 进行转换的转换器
		converters.add(new CustomDateConverter());
		// 注册全局范围内对表单日期包括 LocalDate LocalDateTime LocalTime 三个对象(java8)
		converters.add(new CustomJava8DateTimeConverter());
		converters.add(new CustomJava8DateConverter());
		converters.add(new CustomJava8TimeConverter());
		factoryBean.setConverters(converters);
		return factoryBean.getObject();
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		/*
		 * 交换MappingJackson2HttpMessageConverter与第一位元素 让返回值类型为String的接口能正常返回包装结果 参见
		 * ResponseResultHandler.java若不调换优先级则string类型需要这样包装：objectMapper.
		 * writeValueAsString(R.success(string));
		 */
		for (int i = 0; i < converters.size(); i++) {
			if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
				MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converters
					.get(i);
				converters.set(i, converters.get(0));
				converters.set(0, mappingJackson2HttpMessageConverter);
				break;
			}
		}
		// 加入"通用的converter"
		converters.add(createXmlHttpMessageConverter());
	}

	private HttpMessageConverter<Object> createXmlHttpMessageConverter() {
		// 这个是"通用Converter"可以灵活的配置相应的Marshaller和Unmarshaller
		MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();
		// XStreamMarshaller可以处理Accept:application/xml类型的请求
		XStreamMarshaller xstreamMarshaller = new XStreamMarshaller();
		// 组合的过程，这样以后可以高度灵活的配置不同的Marshaller
		xmlConverter.setMarshaller(xstreamMarshaller);
		xmlConverter.setUnmarshaller(xstreamMarshaller);

		return xmlConverter;
	}

	/**
	 * 创建 RestTemplate 实例
	 * @param restTemplateBuilder
	 * {@link RestTemplateAutoConfiguration#restTemplateBuilder}
	 */
	@Bean
	@ConditionalOnMissingBean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	// @Bean
	// public SpringContextHolder springContextHandler() {
	// return new SpringContextHolder();
	// }

}
