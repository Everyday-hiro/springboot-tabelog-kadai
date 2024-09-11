package com.example.taberogu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.User;
import com.example.taberogu.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
	public VerificationToken findByToken(String token);

	VerificationToken findByUser(User user);

	public void deleteByUserId(int userId);
}
