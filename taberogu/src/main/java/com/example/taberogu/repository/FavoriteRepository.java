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

	Favorite findByRestaurantAndUser(Restaurant restaurant, User user);

	default boolean favoriteJudge(Restaurant restaurant, User user) {
		return findByRestaurantAndUser(restaurant, user) != null;
	}

	List<Favorite> findTop3ByOrderByCreatedAtDesc();

}
