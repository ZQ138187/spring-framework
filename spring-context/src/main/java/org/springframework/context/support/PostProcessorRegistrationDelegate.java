/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.Nullable;

/**
 * Delegate for AbstractApplicationContext's post-processor handling.
 *
 * @author Juergen Hoeller
 * @since 4.0
 */
final class PostProcessorRegistrationDelegate {

	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// Invoke BeanDefinitionRegistryPostProcessors first, if any.
		Set<String> processedBeans = new HashSet<>();
		//判断我们的beanFactory是否实现了BeanDefinitionRegistry
		if (beanFactory instanceof BeanDefinitionRegistry) {
			//强行把beanFactory转为BeanDefinitionRegistry
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			//保存BeanFactoryPostProcessor类型的后置处理器
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
			//保存BeanDefinitionRegistryPostProcessor类型的后置处理器
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();
			//循环我们传递进来的beanFactoryPostProcessors
			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				//判断我们的后置处理器是不是BeanDefinitionRegistryPostProcessor
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					//进行强制转化
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
					//调用它的后置方法
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					//添加到我们用于保存的BeanDefinitionRegistryPostProcessor的集合中
					registryProcessors.add(registryProcessor);
				} else {
					//若没有实现BeanDefinitionRegistryPostProcessor接口，那么他就是
					// BeanFactoryPostProcessor
					// 把当前的后置处理器加入到regularPostProcessors中
					regularPostProcessors.add(postProcessor);
				}
			}

			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.
			//定义一个集合用户保存当前准备创建的BeanDefinitionRegistryPostProcessor
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
			//第一步:去容器中获取BeanDefinitionRegistryPostProcessor的bean的处理器名称
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			//循环上一步获取的BeanDefinitionRegistryPostProcessor的类型名称
			for (String ppName : postProcessorNames) {
				//判断是否实现了PriorityOrdered接口的
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					//显示的调用getBean()的方式获取出该对象然后加入到currentRegistryProcessors集合中去
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					//同时也加入到processedBeans集合中去
					processedBeans.add(ppName);
				}
			}
			//对currentRegistryProcessors集合中BeanDefinitionRegistryPostProcessor进行排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			//把他加入到用于保存到registryProcessors中
			registryProcessors.addAll(currentRegistryProcessors);
			//完成对应2，3，4，5步，扫描、解析、put到map当中
			/**
			 * 在这里典型的BeanDefinitionRegistryPostProcessor就是ConfigurationClassPostProcessor
			 * 用于进行bean定义的加载 比如我们的包扫描，@import等
			 */
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			//调用完之后，马上clear
			currentRegistryProcessors.clear();

			// Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
			//下一步 又去容器中获取BeanDefinitionRegistryPostProcessor的bean的处理器名称
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			//循环上一步获取的BeanDefinitionRegistryPostProcessor的类型名称
			for (String ppName : postProcessorNames) {
				//表示没有被处理过,且实现了Ordered接口的
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					//显示的调用getBean()的方式获取出该对象然后加入到currentRegistryProcessors集合中去
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					//同时也加入到processedBeans集合中去
					processedBeans.add(ppName);
				}
			}
			//对currentRegistryProcessors集合中BeanDefinitionRegistryPostProcessor进行排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			//把他加入到用于保存到registryProcessors中
			registryProcessors.addAll(currentRegistryProcessors);
			//调用他的后置处理方法
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			//调用完之后，马上clear
			currentRegistryProcessors.clear();

			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
			//调用没有实现任何优先级接口的BeanDefinitionRegistryPostProcessor
			//定义一个重复处理的开关变量 默认值为true
			boolean reiterate = true;
			//第一次就可以进来
			while (reiterate) {
				//进入循环马上把开关变量给改为false
				reiterate = false;
				//去容器中获取BeanDefinitionRegistryPostProcessor的bean的处理器名称
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				//循环上一步获取的BeanDefinitionRegistryPostProcessor的类型名称
				for (String ppName : postProcessorNames) {
					//没有被处理过的
					if (!processedBeans.contains(ppName)) {
						//显示的调用getBean()的方式获取出该对象然后加入到currentRegistryProcessors集合中去
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						//同时也加入到processedBeans集合中去
						processedBeans.add(ppName);
						//再次设置为true
						reiterate = true;
					}
				}
				//对currentRegistryProcessors集合中BeanDefinitionRegistryPostProcessor进行排序
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				//把他加入到用于保存到registryProcessors中
				registryProcessors.addAll(currentRegistryProcessors);
				//调用他的后置处理方法
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				//进行clear
				currentRegistryProcessors.clear();
			}

			// Now, invoke the postProcessBeanFactory callback of all processors handled so far.
			//第六步 再次执行bean工厂后置处理器完成cglib代理
			//调用实现了BeanDefinitionRegistryPostProcessor的接口 ,也同时实现了BeanFactoryPostProcessor的方法
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
			//调用BeanFactoryPostProcessor
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		} else {
			// Invoke factory processors registered with the context instance.
			//若当前的beanFactory没有实现了BeanDefinitionRegistry 直接调用beanFactoryPostProcessor接口的方法进行后置处理
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!
		//最后一步 获取容器中所有的 BeanFactoryPostProcessor
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		//保存BeanFactoryPostProcessor类型实现了priorityOrdered
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		//保存BeanFactoryPostProcessor类型实现了Ordered接口的
		List<String> orderedPostProcessorNames = new ArrayList<>();
		//保存BeanFactoryPostProcessor没有实现任何优先级接口的
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			//processedBeans包含的话，表示在上面处理BeanDefinitionRegistryPostProcessor的时候处理过了
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			//判断是否实现了PriorityOrdered
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			//判断是否实现了Ordered
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			} else {
				//没有实现任何的优先级接口的
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		//先调用BeanFactoryPostProcessor实现了PriorityOrdered接口的
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
		//再调用BeanFactoryPostProcessor实现了Ordered.
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>();
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// Finally, invoke all other BeanFactoryPostProcessors.
		//调用没有实现任何方法接口的
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
		beanFactory.clearMetadataCache();
	}

	public static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {
		// 1.找出所有实现BeanPostProcessor接口的类
		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

		// BeanPostProcessor的目标计数
		// Register BeanPostProcessorChecker that logs an info message when
		// a bean is created during BeanPostProcessor instantiation, i.e. when
		// a bean is not eligible for getting processed by all BeanPostProcessors.
		int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
		// 2.添加BeanPostProcessorChecker(主要用于记录信息)到beanFactory中
		beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

		// Separate between BeanPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		// 3.定义不同的变量用于区分: 实现PriorityOrdered接口的BeanPostProcessor、实现Ordered接口的BeanPostProcessor、普通BeanPostProcessor
		// 3.1 priorityOrderedPostProcessors: 用于存放实现PriorityOrdered接口的BeanPostProcessor
		List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		// 3.2 internalPostProcessors: 用于存放Spring内部的BeanPostProcessor
		List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
// 3.3 orderedPostProcessorNames: 用于存放实现Ordered接口的BeanPostProcessor的beanName
		List<String> orderedPostProcessorNames = new ArrayList<String>();
		// 3.4 nonOrderedPostProcessorNames: 用于存放普通BeanPostProcessor的beanName
		List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
		// 4.遍历postProcessorNames, 将BeanPostProcessors按3.1 - 3.4定义的变量区分开
		for (String ppName : postProcessorNames) {
			if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				// 4.1 如果ppName对应的Bean实例实现了PriorityOrdered接口, 则拿到ppName
				// 对应的Bean实例并添加到priorityOrderedPostProcessors
				BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
				priorityOrderedPostProcessors.add(pp);
				if (pp instanceof MergedBeanDefinitionPostProcessor) {
					// 4.2 如果ppName对应的Bean实例也实现了MergedBeanDefinitionPostProcessor接口,
					// 则将ppName对应的Bean实例添加到internalPostProcessors
					internalPostProcessors.add(pp);
				}
			} else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				// 4.3 如果ppName对应的Bean实例没有实现PriorityOrdered接口, 但是实现了Ordered接口,
				// 则将ppName添加到orderedPostProcessorNames
				orderedPostProcessorNames.add(ppName);
			} else {
				// 4.4 否则, 将ppName添加到nonOrderedPostProcessorNames
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, register the BeanPostProcessors that implement PriorityOrdered.
		// First, register the BeanPostProcessors that implement PriorityOrdered.
		// 5.首先, 注册实现PriorityOrdered接口的BeanPostProcessors
		// 5.1 对priorityOrderedPostProcessors进行排序
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		// 5.2 注册priorityOrderedPostProcessors
		registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

		// Next, register the BeanPostProcessors that implement Ordered.
		// 6.接下来, 注册实现Ordered接口的BeanPostProcessors
		List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>();
		for (String ppName : orderedPostProcessorNames) {
			// 6.1 拿到ppName对应的BeanPostProcessor实例对象
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			// 6.2 将ppName对应的BeanPostProcessor实例对象添加到orderedPostProcessors,
			// 准备执行注册
			orderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				// 6.3 如果ppName对应的Bean实例也实现了MergedBeanDefinitionPostProcessor接口,
				// 则将ppName对应的Bean实例添加到internalPostProcessors
				internalPostProcessors.add(pp);
			}
		}
		// 6.4 对orderedPostProcessors进行排序
		sortPostProcessors(orderedPostProcessors, beanFactory);
		// 6.5 注册orderedPostProcessors
		registerBeanPostProcessors(beanFactory, orderedPostProcessors);

		// 7.注册所有常规的BeanPostProcessors（过程与6类似）
		// Now, register all regular BeanPostProcessors.
		List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
		for (String ppName : nonOrderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			nonOrderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

		// Finally, re-register all internal BeanPostProcessors.
		// 8.最后, 重新注册所有内部BeanPostProcessors（相当于内部的BeanPostProcessor会被移到处理器链的末尾）
		// 8.1 对internalPostProcessors进行排序
		sortPostProcessors(internalPostProcessors, beanFactory);
		// 8.2注册internalPostProcessors
		registerBeanPostProcessors(beanFactory, internalPostProcessors);

		// Re-register post-processor for detecting inner beans as ApplicationListeners,
		// moving it to the end of the processor chain (for picking up proxies etc).
		// 9.重新注册ApplicationListenerDetector（跟8类似，主要是为了移动到处理器链的末尾）
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
	}

	private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
		Comparator<Object> comparatorToUse = null;
		if (beanFactory instanceof DefaultListableBeanFactory) {
			comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
		}
		if (comparatorToUse == null) {
			comparatorToUse = OrderComparator.INSTANCE;
		}
		postProcessors.sort(comparatorToUse);
	}

	/**
	 * Invoke the given BeanDefinitionRegistryPostProcessor beans.
	 */
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {
//获取容器中的ConfigurationClassPostProcessor的后置处理器进行Bean定义的扫描
		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanDefinitionRegistry(registry);
		}
	}

	/**
	 * Invoke the given BeanFactoryPostProcessor beans.
	 */
	private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

		for (BeanFactoryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanFactory(beanFactory);
		}
	}

	/**
	 * Register the given BeanPostProcessor beans.
	 */
	private static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {
// 1.遍历postProcessors
		for (BeanPostProcessor postProcessor : postProcessors) {
			// 2.将PostProcessor添加到BeanFactory中的beanPostProcessors缓存
			beanFactory.addBeanPostProcessor(postProcessor);
		}
	}


	/**
	 * BeanPostProcessor that logs an info message when a bean is created during
	 * BeanPostProcessor instantiation, i.e. when a bean is not eligible for
	 * getting processed by all BeanPostProcessors.
	 */
	private static final class BeanPostProcessorChecker implements BeanPostProcessor {

		private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);

		private final ConfigurableListableBeanFactory beanFactory;

		private final int beanPostProcessorTargetCount;

		public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
			this.beanFactory = beanFactory;
			this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (!(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) &&
					this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) {
				if (logger.isInfoEnabled()) {
					logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() +
							"] is not eligible for getting processed by all BeanPostProcessors " +
							"(for example: not eligible for auto-proxying)");
				}
			}
			return bean;
		}

		private boolean isInfrastructureBean(@Nullable String beanName) {
			if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
				BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
				return (bd.getRole() == RootBeanDefinition.ROLE_INFRASTRUCTURE);
			}
			return false;
		}
	}

}
