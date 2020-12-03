package com.huawei;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/10/13 10:33
 */
@Component
public class Y {
	@Autowired
	X x;
	public Y(){
		System.out.println("调用Y构造方法, Y create");
	}
}
