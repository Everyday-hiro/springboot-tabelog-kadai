package com.example.taberogu.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.Review;
import com.example.taberogu.entity.User;
import com.example.taberogu.form.ReviewEditForm;
import com.example.taberogu.form.ReviewRegisterForm;
import com.example.taberogu.repository.ReviewRepository;
import com.example.taberogu.security.UserDetailsImpl;

@Service
public class ReviewService {

	private final ReviewRepository reviewRepository;

	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}

	/**
	 * レビューを投稿するための処理を行うメソッドです
	 * @param restaurant
	 * @param reviewRegisterForm
	 * @param userDetailsImpl
	 */
	@Transactional
	public void create(Restaurant restaurant, ReviewRegisterForm reviewRegisterForm,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		Review review = new Review();
		User user = userDetailsImpl.getUser();

		review.setName(user.getName());
		review.setStar(reviewRegisterForm.getStar());
		review.setDescription(reviewRegisterForm.getDescription());
		review.setRestaurant(restaurant);
		review.setUser(user);

		reviewRepository.save(review);
	}

	/**
	 * レビューの内容を変更するための処理を行うメソッドです。
	 */
	@Transactional
	public void update(Integer id, ReviewEditForm reviewEditForm,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		User user = userDetailsImpl.getUser();

		review.setName(user.getName());
		review.setStar(reviewEditForm.getStar());
		review.setDescription(reviewEditForm.getDescription());
		review.setUser(user);

		reviewRepository.save(review);
	}
}
