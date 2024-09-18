package com.soaringloong.jfrm.framework.mybatis.core.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

/**
 * mapperScan扫描器使用专属的nameGenerator，可避免同名Mapper冲突
 */
public class SpringBeanNameGenerator extends AnnotationBeanNameGenerator {

	@SuppressWarnings("NullableProblems")
	@Override
	protected String buildDefaultBeanName(BeanDefinition definition) {
		String beanClassName = definition.getBeanClassName();// 取得Bean的全限定类名
		Assert.state(beanClassName != null, "No bean class name set");
		// 仅对 com.cmpt 包下面的对象特殊处理
		// 由于 com.cmpt 包下面的mapper和bean可能出现与其他模块的Bean重名故使用Bean的全限定类名
		String theClassName = beanClassName.startsWith("com.cmpt.") ? beanClassName
				: /* shortClassName */ClassUtils.getShortName(beanClassName);
		return Introspector.decapitalize(theClassName);
	}

}