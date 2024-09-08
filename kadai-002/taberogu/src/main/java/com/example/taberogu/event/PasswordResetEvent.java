package com.example.taberogu.event;

import org.springframework.context.ApplicationEvent;

import com.example.taberogu.entity.User;

public class PasswordResetEvent extends ApplicationEvent {
	private final User user;
	private final String token;
	private final String requestUrl;

	public PasswordResetEvent(User user, String token, String requestUrl) {
		super(user);
		this.user = user;
		this.token = token;
		this.requestUrl = requestUrl;
	}

	public User getUser() {
		return user;
	}

	public String getToken() {
		return token;
	}

	public String getRequestUrl() {
		return requestUrl;
	}
}
