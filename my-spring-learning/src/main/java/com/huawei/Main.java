package com.huawei;

import com.huawei.config.AppConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/9/29 18:21
 */

public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext(AppConfig.class);
//		ConfigurableListableBeanFactory beanFactory = acac.getBeanFactory();

//		BeanDefinition x = beanFactory.getBeanDefinition("x");

//		AppConfig appConfig = acac.getBean(AppConfig.class);
		X xBean = acac.getBean(X.class);
		System.out.println(xBean);

//		appConfig.test("hello spring!");


//		AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
//		acac.register(AppConfig.class);
//		acac.setAllowCircularReferences(false);
//		acac.refresh();
//		X xBean = acac.getBean(X.class);
//		System.out.println(xBean);
	}
}
/**
 总结关于循环引用，如何回答面试:
 首先spring在单例的情况下是默认支持循环引用的(当然原形也有办法，今天先不讨论)；在不做任何配置的情况下，
 两个bean相互依赖是能初始化成功的；spring源码中在创建bean的时候先创建这个bean的对象，创建对象完成之后
 通过判断容器对象的allowCircularReferences属性决定是否允许缓存这个临时对象，如果能被缓存成功则通过缓
 存提前暴露这个临时对象来完成循环依赖；而这个属性默认为true，所以说spring默认支持循环依赖的，但是这个
 属性spring提供了api让程序员来修改，所以spring也提供了关闭循环引用的功能；再就是spring完成这个临时对象
 的生命周期的过程中当执行到注入属性或者自动装配的周期时候会通过getSingleton方法去得到需要注入的b对象；
 而b对象这个时候肯定不存在故而会创建b对象，创建b对象成功后继续b对象的生命周期，当执行到b对象的自动注入周期
 时候会要求注入a对象；调用getSingleton；从map缓存中得到a的临时对象（因为这个时候a在set集合中；这里可以
 展开讲），而且获取的时候也会判断是否允许循环引用，但是判断的这个值是通过参数传进来的，也就是spring内部
 调用的，spring源码当中写死了为true，故而如果需要扩展spring、或者对spring二次开发的的时候程序员可以自
 定义这个值来实现自己的功能；不管放到缓存还是从缓存中取出这个临时都需要判断；而这两次判断spring源码当中
 都是默认为true；这里也能再次说明spring默认是支持循环引用的；
 然后面试中可以在说说两次调用getSingleton的意义，正在创建的那个set集合有什么用；最后在说说你在看spring
 循环引用的时候得出的aop实例化过程的新发现；就比较完美了。
 */
/**
 spring容器初始化bean的大概过程(至于详细的过程，后面文章再来介绍)；
 文字总结一下：
 1：实例化一个ApplicationContext的对象；
 2：调用bean工厂后置处理器完成扫描；
 3：循环解析扫描出来的类信息；
 4：实例化一个BeanDefinition对象来存储解析出来的信息；
 5：把实例化好的beanDefinition对象put到beanDefinitionMap当中缓存起来，以便后面实例化bean；
 6：再次调用bean工厂后置处理器；
 7：当然spring还会干很多事情，比如国际化，比如注册BeanPostProcessor等等，如果我们只关心如何实例化一个bean的话
 那么这一步就是spring调用finishBeanFactoryInitialization方法来实例化单例的bean，实例化之前spring要做验证，
 需要遍历所有扫描出来的类，依次判断这个bean是否Lazy，是否prototype，是否abstract等等；
 8：如果验证完成spring在实例化一个bean之前需要推断构造方法，因为spring实例化对象是通过构造方法反射，
 故而需要知道用哪个构造方法；
 9：推断完构造方法之后spring调用构造方法反射实例化一个对象；注意我这里说的是对象、对象、对象；
 这个时候对象已经实例化出来了，但是并不是一个完整的bean，最简单的体现是这个时候实例化出来的对象属性是没有注入，
 所以不是一个完整的bean；
 10：spring处理合并后的beanDefinition(合并？是spring当中非常重要的一块内容，后面的文章我会分析)；
 11：判断是否支持循环依赖，如果支持则提前把一个工厂存入singletonFactories——map；
 12：判断是否需要完成属性注入
 13：如果需要完成属性注入，则开始注入属性
 14：判断bean的类型回调Aware接口
 15：调用生命周期回调方法
 16：如果需要代理则完成代理
 17：put到单例池——bean完成——存在spring容器当中
 */

