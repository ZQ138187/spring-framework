package com.beanLifeCycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/18 21:38
 */
@Component
public class HelloWorld implements MyInterface,BeanNameAware, BeanFactoryAware,
		ApplicationContextAware,InitializingBean,SmartInitializingSingleton,SmartLifecycle,DisposableBean {

	private boolean isRunning;
	public HelloWorld() {
		System.out.println("HelloWorld实例化");
	}

	@Override
	public void my(String str) {
		System.out.println("调用my()方法:str=" + str);
	}

	public void sayHello(String str) {
		System.out.println("调用sayHello()方法:str = "+ str);
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("setBeanName = "+name);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("setBeanFactory="+beanFactory);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("setApplicationContext="+applicationContext);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("afterPropertiesSet方法调用");
	}

	@Override
	public void afterSingletonsInstantiated() {
		System.out.println("afterSingletonsInstantiated方法调用");
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		System.out.println("stop()方法调用,callback="+callback);
		callback.run();
	}

	@Override
	public void start() {
		isRunning = true;
		System.out.println("LifeCycle start()====>");
	}

	@Override
	public void stop() {
		System.out.println("LifeCycle stop()====>");
	}

	@Override
	public boolean isRunning() {

		System.out.println("LifeCycle isRunning()====>");
		return isRunning ;
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("DisposableBean destroy()====>");
	}


}
