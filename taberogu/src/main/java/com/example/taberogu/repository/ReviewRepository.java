package com.example.taberogu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Review;

public interface ReviewRepository extends JpaRepository <Review, Integer>{

	Page<Review> findTop3ByRestaurantIdOrderByCreatedAtDesc(Integer restaurantId, Pageable pageable);

}
