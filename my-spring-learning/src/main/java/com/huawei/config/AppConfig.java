package com.huawei.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/9/29 18:22
 */

@Configuration
@ComponentScan("com.huawei")
public class AppConfig {
	public void test(String msg){
		System.out.println("msg:" + msg);
	}
}

