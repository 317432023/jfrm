package com.soaringloong.jfrm.framework.web.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Servlet;
import java.io.IOException;

/**
 * 配置接收 String 参数时自动去除前后空格
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class WebMvcStringTrimAutoConfiguration {

	/**
	 * 去除两端空格后 空串是否转换为 null
	 */
	private static final boolean emptyAsNull = false;

	@ControllerAdvice
	public static class ControllerStringParamTrimConfig {

		/**
		 * 接收 url 或 form 表单中的参数（get请求）
		 * @param binder
		 */
		@InitBinder
		public void initBinder(WebDataBinder binder) {
			// 参数：去除两端空格后 空串是否转换为 null
			StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(emptyAsNull);
			// 为 String 类对象注册编辑器
			binder.registerCustomEditor(String.class, stringTrimmerEditor);
		}

	}

	/**
	 * 接收Request Body中JSON或XML对象参数（post请求）
	 * @return
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.deserializerByType(String.class,
				new StdScalarDeserializer<String>(String.class) {
					@Override
					public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
						String res = StringUtils.trimWhitespace(jsonParser.getValueAsString());
						return StrUtil.isBlank(res) ? (emptyAsNull?null:"") : res;
					}
				});
	}

}
