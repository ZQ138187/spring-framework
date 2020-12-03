package com.beanInjectStrategy.mapper;

import com.beanInjectStrategy.Mapper;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/19 23:41
 */
@Mapper
public interface UserMapper {
	void selectById(int id);
}
