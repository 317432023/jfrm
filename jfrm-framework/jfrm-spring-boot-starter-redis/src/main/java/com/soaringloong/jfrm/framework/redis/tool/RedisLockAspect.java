package com.soaringloong.jfrm.framework.redis.tool;

import com.comm.pojo.SystemException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * RedisLockAspect
 *
 * @since 2022/7/21 11:03
 */
@Aspect
public class RedisLockAspect {

	private static final Logger log = LoggerFactory.getLogger(RedisLockAspect.class);

	private final RedissonClientTool redissonClientTool;

	public RedisLockAspect(RedissonClientTool redissonClientTool) {
		this.redissonClientTool = redissonClientTool;
	}

	/**
	 * 配置织入点
	 *
	 * @annotation 中的路径表示拦截特定注解
	 */
	@Pointcut("@annotation(RedisLock)")
	public void redisLockPC() {
	}

	@Around(value = "redisLockPC()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		// 解析参数
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();

		// 标注
		RedisLock annotation = method.getAnnotation(RedisLock.class);

		// 加锁键前缀
		String lockKeyPrefix = annotation.lockKeyPrefix();
		// 加锁参数
		String lockParam = "";
		Object[] params = pjp.getArgs();
		if (params != null && params.length > 0) {
			int paramIndex = annotation.lockField() + 1 > params.length ? params.length - 1 : annotation.lockField();
			if (params[paramIndex] != null) {
				lockParam = params[paramIndex].toString();
			}
		}

		// 加锁键
		String businessKey = lockKeyPrefix
				+ (StringUtils.hasLength(lockParam) ? "#" + DigestUtils.md5DigestAsHex(lockParam.getBytes()) : "");

		// 加锁时间
		long waitTime = annotation.lockTime() * 1000;

		// 锁续租时长
		long leaseTime = annotation.leaseTime() * 1000;

		// 加锁
		Object result;
		try {
			// 加锁开始
			boolean ret = redissonClientTool.tryLock(businessKey, waitTime, leaseTime);
			if (!ret) {
				throw new SystemException(500, "系统繁忙，请稍后重试");
			}

			// 执行业务操作
			result = pjp.proceed();

		}
		catch (InterruptedException e) {
			log.error("Interrupt exception, rollback transaction", e);
			throw new Exception("Interrupt exception, please send request again");
		}
		catch (Exception e) {
			log.error("has some error, please check again", e);
			throw e;
		}
		finally {
			// 请求结束后，强制删掉 key，释放锁
			redissonClientTool.unlock(businessKey);
			if (log.isDebugEnabled()) {
				log.debug("release the lock, businessKey is [" + businessKey + "]");
			}
		}
		return result;
	}

}
