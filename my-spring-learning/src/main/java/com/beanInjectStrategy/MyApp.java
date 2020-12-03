package com.beanInjectStrategy;

import com.beanInjectStrategy.mapper.MyClassMapper;
import com.beanInjectStrategy.mapper.UserMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/19 16:00
 */
public class MyApp {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(InjectConfig.class);
		A a = ac.getBean(A.class.getName(),A.class);
		User  myClassMapper = ac.getBean("myClassMapper",User.class);
//		ac.getBean("myClassMapper",MyClassMapper.class);
		Person xxx = ac.getBean("xxx", Person.class);
		System.out.println(xxx);
		System.out.println(a.getClass());
		System.out.println(myClassMapper);
//		System.out.println("myFactoryBean====="+ac.getBean("myFactoryBean"));
//		System.out.println("myFactoryBean====="+ac.getBean("myFactoryBean"));
//		UserMapper myFactoryBean = ac.getBean("myFactoryBean", UserMapper.class);
		UserService userService = ac.getBean("userService", UserService.class);
//		myFactoryBean.selectById(1);
		userService.test();

	}
}
