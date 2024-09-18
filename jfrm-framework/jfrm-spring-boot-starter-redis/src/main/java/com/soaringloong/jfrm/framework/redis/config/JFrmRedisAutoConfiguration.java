package com.soaringloong.jfrm.framework.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.soaringloong.jfrm.framework.redis.tool.RedisGeoTool;
import com.soaringloong.jfrm.framework.redis.tool.RedisLockAspect;
import com.soaringloong.jfrm.framework.redis.tool.RedisTool;
import com.soaringloong.jfrm.framework.redis.tool.RedissonClientTool;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@AutoConfiguration(before = RedissonAutoConfiguration.class) // 目的：使用自己定义的 RedisTemplate
																// Bean
public class JFrmRedisAutoConfiguration {

	private final Logger log = LoggerFactory.getLogger(JFrmRedisAutoConfiguration.class);

	private static void setSerializationBehavior(String objectMapperPropName, RedisSerializer<?> redisSerializer) {
		Field field = ReflectionUtils.findField(redisSerializer.getClass(), objectMapperPropName, ObjectMapper.class);
		if (field != null) {
			ReflectionUtils.makeAccessible(field);
			ObjectMapper objectMapper = (ObjectMapper) ReflectionUtils.getField(field, redisSerializer);
			if (objectMapper != null) {
				objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
					.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL)
					// 解决 LocalDateTime 的序列化
					.registerModules(new JavaTimeModule());
			}
		}
	}

	/**
	 * 序列化器 Jackson2JsonRedisSerializer：适合需要精确恢复对象类型的场景，例如存储多态对象、处理复杂对象结构。
	 * GenericJackson2JsonRedisSerializer：适合简单对象或者不需要关注对象具体类型信息的场景，例如存储简单的配置信息、缓存数据等
	 * @param generic true-返回 GenericJackson2JsonRedisSerializer；false-返回
	 * Jackson2JsonRedisSerializer
	 * @return 序列化器
	 */
	public static RedisSerializer<?> buildRedisSerializer(boolean generic) {
		RedisSerializer<?> serializer = generic ? RedisSerializer.json()
				: new Jackson2JsonRedisSerializer<>(Object.class);
		setSerializationBehavior(generic ? "mapper" : "objectMapper", serializer);
		return serializer;
	}

	/**
	 * 覆盖默认配置 RedisTemplate，使用 String 类型作为key，设置key/value的序列化规则
	 * <p>
	 * Redis 默认配置了 RedisTemplate 和 StringRedisTemplate ，其中RedisTemplate使用的序列化规则是
	 * JdkSerializationRedisSerializer，缓存到redis后，数据都变成了\x0..，非常不易于阅读。
	 * <p>
	 * 因此，重新配置RedisTemplate，使用 Jackson2JsonRedisSerializer 或
	 * GenericJackson2JsonRedisSerializer 来序列化 Key 和
	 * Value。同时，增加HashOperations、ValueOperations等Redis数据结构相关的操作，这样比较方便使用
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		log.info(
				"====================================RedisConfiguration.redisTemplate====================================");
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		// 设置key的序列化和反序列化规则 为 StringRedisSerializer
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());
		// 设置value的序列化和反序列化器
		RedisSerializer<?> serializer = buildRedisSerializer(false);
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);

		// 开启事务(默认是关闭的)
		// *注意如果开启了事务，设置redis缓存必须按照事务的规则提交，否则当前连接将查询不到该缓存
		// template.setEnableTransactionSupport(true);

		template.afterPropertiesSet();

		return template;
	}

	@Bean
	public RedisTool redisTool(Environment env, RedisTemplate<String, Object> redisTemplate,
			StringRedisTemplate stringRedisTemplate) {
		return new RedisTool(env, redisTemplate, stringRedisTemplate);
	}

	@Bean
	public RedisGeoTool redisGeoTool(StringRedisTemplate stringRedisTemplate) {
		return new RedisGeoTool(stringRedisTemplate);
	}

	@Bean
	public RedissonClientTool redissonClientTool(RedissonClient redissonClient) {
		return new RedissonClientTool(redissonClient);
	}

	@Bean
	public RedisLockAspect redisLockAspect(RedissonClientTool redissonClientTool) {
		return new RedisLockAspect(redissonClientTool);
	}

}
