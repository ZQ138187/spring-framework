package com.beanInjectStrategy;

import com.beanInjectStrategy.mapper.UserMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/20 22:07
 * new多个BeanDifination对象：
 *
 * OrderMapper=>new MyFactoryBean(OrderMapper.class)=>OrderMapper代理对象
 * UserMapper=>new MyFactoryBean(UserMapper.class)=>UserMapper代理对象
 *
 * bd = new BeanDefinition()
 * bd.beanClass = MyFactoryBean
 * bd.beanName = userMapper
 * bd.constructorArgumentValues.add(UserMapper.class)
 *
 * bean ------>UserMapper代理
 *
 *  * bd = new BeanDefinition()
 *  * bd.beanClass = MyFactoryBean
 *  * bd.beanName = orderMapper
 *  * bd.constructorArgumentValues.add(OrderMapper.class)
 *  *
 *  * bean ------>OrderMapper代理
 *
 */

public class MyFactoryBean implements FactoryBean<Object> {
	private Class<Object> mapper;
	public MyFactoryBean(Class<Object> mapper){
		this.mapper = mapper;
	}
	//方式二：把一个自己定义的bean交给spring容器管理，并且可以属性注入
	@Override
	public Object getObject() throws Exception {
//		Organization org = new Organization();
//		System.out.println(org);
//		return org;
		Object obj =Proxy.newProxyInstance(UserMapper.class.getClassLoader(), new Class<?>[]{mapper}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println(method.getName());
				return null;
			}
		});
		return obj;
	}

	@Override
	public Class<?> getObjectType() {
//		return Organization.class;
		return mapper;
	}
}
