package com.example.taberogu.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;
import com.example.taberogu.repository.FavoriteRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}

	/**
	 * お気に入り登録
	 */
	@Transactional
	public void add(Restaurant restaurant, User user) {
		Favorite favorite = new Favorite();

		favorite.setRestaurant(restaurant);
		favorite.setUser(user);

		favoriteRepository.save(favorite);
	}

	/**
	 * お気に入り解除
	 */
	@Transactional
	public void delete(Integer restaurantId, Integer userId) {
		Favorite favorite = favoriteRepository.findByRestaurantIdAndUserId(restaurantId, userId);
		if (favorite != null) {
			favoriteRepository.delete(favorite);
		}
	}
}
