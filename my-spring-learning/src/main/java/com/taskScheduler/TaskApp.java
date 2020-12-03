package com.taskScheduler;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/20 15:57
 */
public class TaskApp {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TaskConfig.class);
		MessagePrintExample messagePrintExample = ac.getBean("messagePrintExample", MessagePrintExample.class);
		System.out.println(messagePrintExample);

		for (int i = 0; i < 20; i++) {
			messagePrintExample.executor("message"+i);
		}
		messagePrintExample.scheduler();

	}

}
