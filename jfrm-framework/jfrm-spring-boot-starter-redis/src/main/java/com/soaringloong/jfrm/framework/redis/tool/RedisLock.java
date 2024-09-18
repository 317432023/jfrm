package com.soaringloong.jfrm.framework.redis.tool;

import java.lang.annotation.*;

/**
 * 自定义注解分布式锁Redis实现
 *
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

	/**
	 * 加锁key前缀
	 * @return
	 */
	String lockKeyPrefix();

	/**
	 * 特定参数识别，默认取第 0 个下标
	 */
	int lockField() default 0;

	/**
	 * 加锁重试次数（该功能暂未实现）
	 */
	// int tryCount() default 3;

	/**
	 * 初始设定锁定时间，秒 s 单位
	 */
	long lockTime() default 30;

	/**
	 * 租约时间，秒 s 单位 0或负数 自动续租
	 */
	long leaseTime() default 10;

}
