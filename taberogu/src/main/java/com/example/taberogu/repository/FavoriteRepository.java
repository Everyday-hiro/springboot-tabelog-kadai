package com.example.taberogu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

	Page<Favorite> findByUser(User user, Pageable pageable);

	default boolean favoriteJudge(Restaurant restaurant, User user) {
		return findByRestaurantIdAndUserId(restaurant.getId(), user.getId()) != null;
	}

	List<Favorite> findTop3ByOrderByCreatedAtDesc();

	Favorite findByRestaurantIdAndUserId(Integer restaurantId, Integer userId);

	List<Favorite> findTop3ByUserIdOrderByCreatedAtDesc(int user);

}
