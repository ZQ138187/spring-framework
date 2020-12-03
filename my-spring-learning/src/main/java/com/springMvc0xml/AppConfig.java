package com.springMvc0xml;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/23 22:20
 */
@Configuration
@ComponentScan("com.springMvc0xml")
public class AppConfig extends WebMvcConfigurationSupport {
}
