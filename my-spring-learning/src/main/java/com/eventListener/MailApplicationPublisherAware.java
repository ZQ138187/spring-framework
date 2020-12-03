package com.eventListener;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/20 10:31
 */
@Component
public class MailApplicationPublisherAware implements ApplicationEventPublisherAware {
	public ApplicationEventPublisher applicationEventPublisher;
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
	public void publishEnvent(MailEvent mailEvent){
		applicationEventPublisher.publishEvent(mailEvent);
	}
}
