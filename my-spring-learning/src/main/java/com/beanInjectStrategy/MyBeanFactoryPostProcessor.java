package com.beanInjectStrategy;

import com.beanInjectStrategy.mapper.OrderMapper;
import com.beanInjectStrategy.mapper.UserMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/19 22:39
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		GenericBeanDefinition bd = (GenericBeanDefinition)beanFactory.getBeanDefinition("myClassMapper");
//		Class<?> beanClass = bd.getBeanClass();//此处获取不到，报错
		System.out.println("----"+ bd.getBeanClassName());
//		beanFactory.getBean("myClassMapper");//会去根据beanDefinition创建MyClassMapper
		//把一个自己定义的bean交给spring容器管理,有三种方式
		//方式一：
		beanFactory.registerSingleton("xxx",new Person());//通过beanFactory可以把一个自己定义的bean交给spring容器管理

		bd.setBeanClass(User.class);


	}
}
