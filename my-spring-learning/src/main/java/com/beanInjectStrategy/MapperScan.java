package com.beanInjectStrategy;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/20 23:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(myBeanDefinitionRegistry.class)
public @interface MapperScan {
	public String value();
}
