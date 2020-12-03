package com.taskScheduler;

import org.springframework.beans.factory.parsing.Location;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/20 16:46
 */
@Configuration
@ComponentScan("com.taskScheduler")
@ImportResource(locations = {"file:D:\\learning\\yuanma\\spring-framework\\my-spring-learning\\src\\main\\resources\\task.xml"})
public class TaskConfig {
}
