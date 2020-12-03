package com.beanInjectStrategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/19 16:01
 */
@Configuration
@ComponentScan("com.beanInjectStrategy")
@MapperScan("com.beanInjectStrategy.mapper")
public class InjectConfig {
	@Bean
	public CustomBeanDefinition customBeanDefinition(){
		return new CustomBeanDefinition();
	}
}
