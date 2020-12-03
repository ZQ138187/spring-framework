package com.beanLifeCycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/18 22:03
 */
public class SpringApp {
	public static void main(String[] args) {
//		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(MyConfig.class);
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("file:D:/learning/framework/spring-framework-5.0.x/my_spring_learning/src/main/resources/appLifeCycle.xml");
		HelloWorld hello = (HelloWorld)ac.getBean("helloWorld");
		hello.sayHello("sayHello");
		ac.close();
	}
}
