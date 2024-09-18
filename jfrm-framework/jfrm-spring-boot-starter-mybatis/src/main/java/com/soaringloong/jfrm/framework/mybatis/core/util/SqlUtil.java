package com.soaringloong.jfrm.framework.mybatis.core.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.soaringloong.jfrm.framework.mybatis.core.util.LikeIgnore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * MyBatis-Plus SQL工具类
 *
 */
public class SqlUtil {

	/**
	 * 全字段查询
	 * @param clazz 实体
	 * @param query QueryWrapper
	 * @param text 文本
	 * @param <T> /
	 */
	public static <T> QueryChainWrapper<T> likeAll(Class clazz, QueryChainWrapper<T> query, String text) {
		if (StrUtil.isNotEmpty(text)) {
			try {
				Field[] fields = clazz.getDeclaredFields();
				int i = 0;
				for (Field column : fields) {
					if (i != 0) {
						query.or();
					}
					// 成员不为常量并且没有加LikeIgnore注解
					if (!column.getName().equalsIgnoreCase("id") && !column.getName().equalsIgnoreCase("create_time")
							&& !column.getName().equalsIgnoreCase("update_time")
							&& column.getAnnotation(LikeIgnore.class) == null
							&& !Modifier.isFinal(column.getModifiers())) {

						// 关键字处理
						String columnName = StrUtil.toUnderlineCase(column.getName());
						if (columnName.equalsIgnoreCase("value") || columnName.equalsIgnoreCase("type")
								|| columnName.equalsIgnoreCase("name")) {
							columnName = "`" + columnName + "`";
						}
						query.like(columnName, text);
					}
					i++;
				}
			}
			catch (Exception e) {
				System.err.println(ExceptionUtil.getMessage(e));
			}
		}
		return query;
	}

}
