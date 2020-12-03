package com.qing.demo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/12/3 9:13
 */
public class Demo {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		AppConfig bean = ac.getBean(AppConfig.class);
		System.out.println(bean);
		System.out.println("这时错误的");
	}
}
