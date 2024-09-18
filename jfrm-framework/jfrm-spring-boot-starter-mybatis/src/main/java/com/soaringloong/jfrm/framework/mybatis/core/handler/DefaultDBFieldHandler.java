package com.soaringloong.jfrm.framework.mybatis.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 通用参数填充实现类
 * <p>
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

	/**
	 * 插入时的填充策略
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		fillTime(metaObject, "createTime");
		boolean re = fillUserName(metaObject, "createBy") || fillUserName(metaObject, "creator");
		this.updateFill(metaObject);
	}

	/**
	 * 更新时的填充策略
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		fillTime(metaObject, "updateTime");
		boolean re = fillUserName(metaObject, "updateBy") || fillUserName(metaObject, "updater");
	}

	private void fillTime(MetaObject metaObject, String timeField) {
		if (metaObject.hasGetter(timeField)) {
			// 创建时间若为空，则以当前时间为创建时间
			Object createTime = metaObject.getValue(timeField);
			if (createTime == null) {
				Class<?> getterType = metaObject.getGetterType(timeField);
				if (getterType.isAssignableFrom(LocalDateTime.class)) {
					this.setFieldValByName(timeField, LocalDateTime.now(), metaObject);
				}
				else if (getterType.isAssignableFrom(Date.class)) {
					this.setFieldValByName(timeField, new Date(), metaObject);
				}
			}
		}
	}

	private boolean fillUserName(MetaObject metaObject, String userNameField) {
		if (metaObject.hasGetter(userNameField)) {
			String createBy = (String) metaObject.getValue(userNameField);
			if (createBy == null) {
				createBy = getUsername();
				if (createBy != null) {
					this.setFieldValByName(userNameField, createBy, metaObject);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 提取当前用户名
	 */
	private String getUsername() {
		// 优先从若依框架提取当前用户名
		String createBy = getCreateBy("com.ruoyi.common.utils.SecurityUtils", "getUsername");
		if (createBy != null) {
			return createBy;
		}

		// 自行封装的框架提取当前用户名
		createBy = getCreateBy("com.cmpt.sys.security.SecurityUtils", "getUsername");
		if (createBy != null) {
			return createBy;
		}

		createBy = getCreateBy("com.soaringloong.jfrm.system.security.SecurityUtils", "getUserName");
		return createBy;
	}

	private String getCreateBy(String className, String methodName) {
		String createBy = null;
		Class<?> securityUtilClass = null;
		try {
			securityUtilClass = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			// ignore
		}
		if (securityUtilClass != null) {
			try {
				Method method = securityUtilClass.getMethod(methodName);
				createBy = (String) method.invoke(null);
			}
			catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				// ignore
			}
		}

		return createBy;
	}

}