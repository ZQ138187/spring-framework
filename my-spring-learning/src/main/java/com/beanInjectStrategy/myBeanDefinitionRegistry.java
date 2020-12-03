package com.beanInjectStrategy;

import com.beanInjectStrategy.mapper.OrderMapper;
import com.beanInjectStrategy.mapper.UserMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/19 15:39
 */
public class myBeanDefinitionRegistry  implements ImportBeanDefinitionRegistrar {
	private ApplicationContext ac;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		/*
		 * @Author: zq  @Description: 添加自定义beanDefinition（三种方法)
		 */
		//方法一：添加@Import注解
//		GenericBeanDefinition bd = new GenericBeanDefinition();
//		bd.setBeanClass(A.class);
//		registry.registerBeanDefinition(A.class.getName(),bd);
		//方法二：添加@Import注解
//		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(A.class);
//		AbstractBeanDefinition beanDefinition = bdb.getBeanDefinition();
//		registry.registerBeanDefinition(A.class.getName(),beanDefinition);

		MyClassPathBeanDefinitionScanner scan = new MyClassPathBeanDefinitionScanner(registry,false);
		//扫描把标注有@Mapper注解的类MyClassMapper注册为beanDefinition
		scan.addIncludeFilter(new AnnotationTypeFilter(Mapper.class));
		scan.doScan("com.beanInjectStrategy");


		Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName());
		Object value = annotationAttributes.get("value");
		System.out.println("value=="+value);
		Set<Class<?>> mappers = new HashSet<>();
		try {
			//获取指定包中标注有Mapper.class的接口
			mappers = getAnnotationClasses(value.toString(), Mapper.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		List<Class> mappers = new ArrayList<>();
//		mappers.add(UserMapper.class);
//		mappers.add(OrderMapper.class);
		for(Class<?> mapper :mappers){
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
			AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
			beanDefinition.setBeanClass(MyFactoryBean.class);
			beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(mapper);
			registry.registerBeanDefinition(mapper.getSimpleName(),beanDefinition);
		}

	}

	private Set<Class<?>> getClasses(String packageName){
		Set<Class<?>> classes = new HashSet<>();
		try {
			String packageDirName = packageName.replace('.', '/');
			Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					addClass(classes, filePath, packageName);
				}
			}
		}catch (Exception e){

		}
		return classes;

	}

	public void addClass(Set<Class<?>> classes, String filePath, String packageName) throws Exception {
		File[] files = new File(filePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
		assert files != null;
		for (File file : files) {
			String fileName = file.getName();
			if (file.isFile()) {
				String classsName = fileName.substring(0, fileName.lastIndexOf("."));
				if (!packageName.isEmpty()) {
					classsName = packageName + "." + classsName;
				}
				doAddClass(classes, classsName);
			}

		}
	}
	public void doAddClass(Set<Class<?>> classes, final String classsName) throws Exception {
		ClassLoader classLoader = new ClassLoader() {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				return super.loadClass(name);
			}
		};
		classes.add(classLoader.loadClass(classsName));
	}

	public <A extends Annotation> Set<Class<?>> getAnnotationClasses(String packageName, Class<A> annotationClass) throws Exception {

		//找用了annotationClass注解的类
		Set<Class<?>> mappers = new HashSet<>();
		Set<Class<?>> clsList = getClasses(packageName);
		if (clsList != null && clsList.size() > 0) {
			for (Class<?> cls : clsList) {
				if (cls.getAnnotation(annotationClass) != null && cls.isInterface()) {
					mappers.add(cls);
				}
			}
		}
		return mappers;
	}


}
