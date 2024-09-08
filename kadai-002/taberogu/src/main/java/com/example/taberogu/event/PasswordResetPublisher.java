package com.example.taberogu.event;

import org.springframework.context.ApplicationEventPublisher;

import com.example.taberogu.entity.User;

public class PasswordResetPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	public PasswordResetPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public void Event(User user, String requestUrl) {
		applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
	}
}
