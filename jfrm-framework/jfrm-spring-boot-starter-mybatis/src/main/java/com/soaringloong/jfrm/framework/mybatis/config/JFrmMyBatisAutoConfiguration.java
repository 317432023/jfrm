package com.soaringloong.jfrm.framework.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.soaringloong.jfrm.framework.mybatis.core.handler.DefaultDBFieldHandler;
import com.soaringloong.jfrm.framework.mybatis.core.mapper.SpringBeanNameGenerator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = MybatisPlusAutoConfiguration.class) // 目的：先于 MyBatis Plus
																// 自动配置，避免 @MapperScan
																// 可能扫描不到 Mapper 打印 warn
																// 日志
@MapperScan(basePackages = "${jfrm.info.mapper-package}", annotationClass = Mapper.class,
		nameGenerator = SpringBeanNameGenerator.class, // com.cmpt为特殊bean
		lazyInitialization = "${mybatis.lazy-initialization:false}") // Mapper
																		// 懒加载，目前仅用于单元测试
public class JFrmMyBatisAutoConfiguration {

	private final Logger log = LoggerFactory.getLogger(JFrmMyBatisAutoConfiguration.class);

	/**
	 * 分页拦截器配置<br>
	 * 当类路径下存在 Interceptor 这个 class 并且 mybatis-plus.pagination 等于 havingValue
	 * 指定的值时，此拦截器生效<br>
	 * 注：在application.properties配置"jfrm.mybatis-plus.pagination.enable" <br>
	 * 如无必要请关闭 以免影响性能!!!
	 */
	@ConditionalOnClass(Interceptor.class)
	@ConditionalOnProperty(prefix = "jfrm.mybatis-plus.pagination", name = "enabled", havingValue = "true")
	@Bean
	public PaginationInnerInterceptor paginationInnerInterceptor() {
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		paginationInnerInterceptor.setDbType(DbType.MYSQL);
		// 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求 默认false
		paginationInnerInterceptor.setOverflow(false);
		// 设置最大单页限制数量，默认 500 条，-1 不受限制
		paginationInnerInterceptor.setMaxLimit(500L);
		return paginationInnerInterceptor;
	}

	/**
	 * 乐观锁拦截器配置<br>
	 * 当类路径下存在 Interceptor 这个 class 并且 mybatis-plus.optimistic 等于 havingValue
	 * 指定的值时，此拦截器生效<br>
	 * 注：在application.properties配置"jfrm.mybatis-plus.optimistic.enable" <br>
	 * 如无必要请关闭 以免影响性能!!!
	 */
	@ConditionalOnClass(Interceptor.class)
	@ConditionalOnProperty(prefix = "jfrm.mybatis-plus.optimistic", name = "enabled", havingValue = "true")
	@Bean
	public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
		return new OptimisticLockerInnerInterceptor();
	}

	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		try {
			// 添加新的 分页插件
			interceptor.addInnerInterceptor(paginationInnerInterceptor());
		}
		catch (NoSuchBeanDefinitionException e) {
			log.warn("未开启 MyBatisPlus 分页拦截插件");
		}
		try {
			// 添加新的 乐观锁插件
			interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
		}
		catch (NoSuchBeanDefinitionException e) {
			log.warn("未开启 MyBatisPlus 乐观锁拦截插件");
		}
		return interceptor;
	}

	@Bean
	public MetaObjectHandler metaObjectHandler() {
		return new DefaultDBFieldHandler();
	}

}
