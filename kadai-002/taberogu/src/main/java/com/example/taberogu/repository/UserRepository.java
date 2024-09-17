package com.example.taberogu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByEmail(String email);

	public Page<User> findByNameLikeOrFuriganaLikeOrEmailLike(String name, String furigana, String email,
			Pageable pageable);

	public User findBySubscriptionId(String subscriptionId);

	public User findByCustomerId(String customerId);

}
