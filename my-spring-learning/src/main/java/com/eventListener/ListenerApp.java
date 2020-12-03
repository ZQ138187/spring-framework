package com.eventListener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/20 10:06
 */
public class ListenerApp {
	public static void main(String[] args) throws InterruptedException {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 100,
				0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(EventConfig.class);
		String[] beanDefinitionNames = ac.getBeanDefinitionNames();
		System.out.println("=========打印所有beanDifinitionName===========");
		Stream.of(beanDefinitionNames).forEach(beanDefinitionName-> System.out.println(beanDefinitionName));
		MailApplicationPublisherAware mailApplicationPublisherAware = ac.getBean("mailApplicationPublisherAware", MailApplicationPublisherAware.class);


		executor.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("=========Executors========");
			}
		});
		threadPoolExecutor.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("=========threadPoolExecutor========");
			}
		});
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("=====thread : publish envent thread begin=====");
				MailEvent breakfast = new MailEvent("A开始吃饭早饭", MailEnum.BREAKFASK);
				MailEvent lunch = new MailEvent("B开始吃中饭", MailEnum.LUNCH);
				mailApplicationPublisherAware.publishEnvent(breakfast);
				mailApplicationPublisherAware.publishEnvent(lunch);
			}
		});
		thread.setName("publishEnventThread");
		thread.start();
		thread.join();
		System.out.println("======Main thread end=======");
		executor.shutdown();
		threadPoolExecutor.shutdown();
		ac.close();
	}
}
