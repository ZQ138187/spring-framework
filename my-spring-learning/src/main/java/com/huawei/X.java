package com.huawei;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/9/29 22:24
 */
@Component
public class X {
	@Autowired
	Y y;

	public X() {
		System.out.println("调用X构造方法,X create");
	}

	public void testX() {
		System.out.println("testX mothod invoke");
	}
}
