package com.springMvc0xml;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.Tomcat;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/23 22:00
 */
@Component
public class Application {
	public static void run()throws Exception{
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(80);
		Context context = tomcat.addContext("/", App.class.getResource("/").getPath().replaceAll("%20", " "));
		context.addLifecycleListener((LifecycleListener) Class.forName(tomcat.getHost().getConfigClass()).newInstance());
		tomcat.start();
	}

}
