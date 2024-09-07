package com.example.taberogu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Review;
import com.example.taberogu.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	Page<Review> findByRestaurantId(Integer restaurantId, Pageable pageable);

	List<Review> findByUserAndRestaurantId(User user, Integer id);

	Page<Review> findByNameLike(String keyword, Pageable pageable);

}
