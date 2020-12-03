package com.eventListener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/20 10:45
 */
@Component
public class MailApplicationListener implements ApplicationListener<MailEvent> {
	@Override
	public void onApplicationEvent(MailEvent event) {
		System.out.println("====event:"+event.mailEnum+"=>"+event);
	}
}
