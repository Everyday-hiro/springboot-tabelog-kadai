package com.example.taberogu.service;

import java.util.UUID;

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

	public String createVerificationToken(User user) {
		// トークンの生成
		VerificationToken token = verificationTokenRepository.findByUser(user);
		if (token == null) {
			token = new VerificationToken();
			token.setUser(user);
		}
		token.setToken(UUID.randomUUID().toString());
		verificationTokenRepository.save(token);

		return token.getToken();
	}

	public VerificationToken findByToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	public void deleteToken(VerificationToken token) {
		verificationTokenRepository.delete(token);
	}
}
