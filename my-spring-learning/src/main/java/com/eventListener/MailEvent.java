package com.eventListener;

import org.springframework.context.ApplicationEvent;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/20 10:10
 */
public class MailEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1654137577257435602L;
	public MailEnum mailEnum;

	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param source the object on which the event initially occurred (never {@code null})
	 */
	public MailEvent(String mailMsg,MailEnum mailEnum) {
		super(mailMsg);
		this.mailEnum = mailEnum;
	}
}
