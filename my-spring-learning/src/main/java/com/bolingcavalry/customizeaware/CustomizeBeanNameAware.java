package com.bolingcavalry.customizeaware;//package com.bolingcavalry.customizeaware.aware;
//
//import com.bolingcavalry.customizeaware.util.Utils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.BeanNameAware;
//import org.springframework.stereotype.Service;
//
///**
// * @Description :
// * @Author : zq2599@gmail.com
// * @Date : 2018-08-13 18:55
// */
//@Service
//public class CustomizeBeanNameAware implements BeanNameAware {
//	private String beanName;
//
//	@Override
//	public void setBeanName(String beanName) {
//		Utils.printTrack("beanName is set to " + beanName);
//		this.beanName = beanName;
//	}
//
//	public String getBeanName() {
//		return this.beanName;
//	}
//}
//

/**
 * 控制台打印：
 * 2018-08-14 06:07:12.307  INFO 18940 --- [           main] c.b.customizeaware.util.Utils            : beanName is set to customizeBeanNameAware
 * ************************************************************
 * java.lang.Thread.getStackTrace() 1,559 <-
 * com.bolingcavalry.customizeaware.util.Utils.printTrack() 20 <-
 * com.bolingcavalry.customizeaware.aware.CustomizeBeanNameAware.setBeanName() 20 <-
 * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeAwareMethods() 1,712 <-
 * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean() 1,686 <-
 * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean() 573 <-
 * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean() 495 <-
 * org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0() 317 <-
 * org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton() 222 <-
 * org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean() 315 <-
 * org.springframework.beans.factory.support.AbstractBeanFactory.getBean() 199 <-
 * org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons() 759 <-
 * org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization() 869 <-
 * org.springframework.context.support.AbstractApplicationContext.refresh() 550 <-
 * org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh() 140 <-
 * org.springframework.boot.SpringApplication.refresh() 762 <-
 * org.springframework.boot.SpringApplication.refreshContext() 398 <-
 * org.springframework.boot.SpringApplication.run() 330 <-
 * org.springframework.boot.SpringApplication.run() 1,258 <-
 * org.springframework.boot.SpringApplication.run() 1,246 <-
 * com.bolingcavalry.customizeaware.CustomizeawareApplication.main() 10
 */
