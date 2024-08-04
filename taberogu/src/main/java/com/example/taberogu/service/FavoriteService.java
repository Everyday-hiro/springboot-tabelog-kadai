package com.example.taberogu.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;
import com.example.taberogu.repository.FavoriteRepository;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.security.UserDetailsImpl;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final RestaurantRepository restaurantRepository;

	public FavoriteService(FavoriteRepository favoriteRepository, RestaurantRepository restaurantRepository) {
		this.favoriteRepository = favoriteRepository;
		this.restaurantRepository = restaurantRepository;
	}

	/**
	 * お気に入り登録
	 */
	@Transactional
	public void add(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Integer id) {
		Favorite favorite = new Favorite();
		User user = userDetailsImpl.getUser();
		Restaurant restaurant = restaurantRepository.getReferenceById(id);

		favorite.setRestaurant(restaurant);
		favorite.setUser(user);

		favoriteRepository.save(favorite);
	}

	/**
	 * お気に入り解除
	 */
	@Transactional
	public void delete(UserDetailsImpl userDetailsImpl, Integer id) {
		User user = userDetailsImpl.getUser();
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		Favorite favorite = favoriteRepository.findByRestaurantAndUser(restaurant, user);
		if (favorite != null) {
			favoriteRepository.delete(favorite);
		}
	}
}
