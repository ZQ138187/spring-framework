package com.beanInjectStrategy;

import com.beanInjectStrategy.mapper.OrderMapper;
import com.beanInjectStrategy.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/19 23:43
 */
@Component
public class UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private OrderMapper orderMapper;
	public void test(){
		System.out.println(userMapper);//会调用toString方法，然后去handler方法返回null
		userMapper.selectById(1);
	}
}
