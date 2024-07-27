package com.example.taberogu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>{

	 public Page<Restaurant> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);    
     public Page<Restaurant> findByAddressLike(String area, Pageable pageable);
     public Page<Restaurant> findByCategoryLike(String category, Pageable pageable);
	public List<Restaurant> findTop3ByOrderByCreatedAtDesc();    
}
