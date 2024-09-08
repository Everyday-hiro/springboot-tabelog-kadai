package com.example.taberogu.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taberogu.entity.User;
import com.example.taberogu.entity.VerificationToken;
import com.example.taberogu.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
	private final VerificationTokenRepository verificationTokenRepository;
	private final ApplicationEventPublisher eventPublisher;

	public VerificationTokenService(VerificationTokenRepository verificationTokenRepository,
			ApplicationEventPublisher eventPublisher) {
		this.verificationTokenRepository = verificationTokenRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public void create(User user, String token) {
		VerificationToken verificationToken = new VerificationToken();

		verificationToken.setUser(user);
		verificationToken.setToken(token);

		verificationTokenRepository.save(verificationToken);
	}

	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	@Transactional
	public void createAndPublishEvent(User user, String token) {
		// トークンの生成と保存
		VerificationToken verificationToken = new VerificationToken();

		verificationToken.setUser(user);
		verificationToken.setToken(token);

		verificationTokenRepository.save(verificationToken);

	}
}
