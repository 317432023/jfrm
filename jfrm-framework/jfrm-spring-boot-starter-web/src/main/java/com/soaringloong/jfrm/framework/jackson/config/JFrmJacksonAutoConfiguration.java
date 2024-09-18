package com.soaringloong.jfrm.framework.jackson.config;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tk.dep.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON序列化配置
 */
@AutoConfiguration
@Slf4j
public class JFrmJacksonAutoConfiguration {

	@Value("${jfrm.web.serial-write-timestamp:false}")
	private boolean serialWriteTimestamp = Boolean.FALSE;

	@Bean
	@SuppressWarnings("InstantiationOfUtilityClass")
	public JsonUtils jsonUtils(List<ObjectMapper> objectMappers) {
		// 1、注册 SimpleModule 到 objectMapper
		objectMappers.forEach(objectMapper -> JsonUtils.makeCustomObjectMapper(objectMapper, serialWriteTimestamp));

		// 2、设置 objectMapper 到 JsonUtils
		JsonUtils.init(CollUtil.getFirst(objectMappers));
		log.info("[init][初始化 JsonUtils 成功]");
		return new JsonUtils();
	}

	/**
	 * JackJson 序列化与反序列化配置
	 * <p>
	 * 使用此方法, 以下 spring-boot: jackson 全局日期格式化 配置 将会失效 详见
	 * JsonUtils.makeCustomObjectMapper() spring.jackson.time-zone=GMT+8
	 * spring.jackson.date-format=yyyy-MM-dd HH:mm:ss 原因: 会覆盖 @EnableAutoConfiguration 关于
	 * WebMvcAutoConfiguration 的配置
	 */
	@Bean
	public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter(
			List<ObjectMapper> objectMappers) {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setObjectMapper(CollUtil.getFirst(objectMappers));

		// 设置中文编码格式
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		mediaTypes.add(MediaType.TEXT_HTML);
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		return mappingJackson2HttpMessageConverter;
	}

}
