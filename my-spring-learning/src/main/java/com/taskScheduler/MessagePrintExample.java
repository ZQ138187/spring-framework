package com.taskScheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: zq
 * @Date: 2020/11/20 16:19
 */
@Component("messagePrintExample")
public class MessagePrintExample {
	@Autowired
	public TaskExecutor taskExecutor;
	@Autowired
	public TaskScheduler taskScheduler;

	public void scheduler(){
		taskScheduler.schedule(()->{
			System.out.println(Thread.currentThread().getName()+":"+new Date());
		},new CronTrigger("*/5 * * * * ?"));
	}

	public void xmlScheduler(){
		System.out.println(Thread.currentThread().getName()+"==>" + new Date());
	}

	public void executor(String message){
		taskExecutor.execute(new MessageTask(message));
	}

	private class MessageTask implements Runnable{
		private String message;
		public MessageTask(String message){
			this.message = message;
		}
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName()+"=>"+message);
		}
	}
}
