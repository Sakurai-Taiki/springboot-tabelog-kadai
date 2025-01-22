package com.example.kadai_002.event;

import org.springframework.context.ApplicationEvent;

import com.example.kadai_002.entity.Users;

import lombok.Getter;

@Getter
public class ResetEvent  extends ApplicationEvent {
	private Users users;
	private String requestUrl;
	
	public ResetEvent(Object source, Users users, String requestUrl) {
		super(source);
		
		this.users = users;
		this.requestUrl = requestUrl;
	}

}