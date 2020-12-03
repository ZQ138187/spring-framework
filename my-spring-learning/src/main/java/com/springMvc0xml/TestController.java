package com.springMvc0xml;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/23 22:13
 * springMVc 找Controller流程：
 * 1.扫描整个项目（spring已经做了）
 * 2.拿到所有加了@Controller注解的类
 * 3.遍历类里面所有的方法对象
 * 4.判断方法是否加了@RequestMappig注解
 * 5，把@RequestMapping注解的value作为map集合的key给put进去，把mathod对象作为value放入map集合
 * 6.根据用户发送的请求，拿到请求中的uri (url:http://localhost:80/test.do   uri:/test.do)
 * 7.使用请求的uri作为map的key，去map里面get，看看是否有返回值
 */
@Controller
public class TestController {
	@RequestMapping("/test.do")
	@ResponseBody
	public Object test(String name, HttpServletRequest request, HttpServletResponse response){
		request.getRequestURI();
		System.out.println("调用了");
		System.out.println(name);
		return "成功";
	}
}
