package com.example.taberogu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

	public List<Restaurant> findTop3ByOrderByCreatedAtDesc();

	public Page<Restaurant> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword,
			Pageable pageable);

	public Page<Restaurant> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword,
			Pageable pageable);

	public Page<Restaurant> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);

	public Page<Restaurant> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);

	public Page<Restaurant> findByCategoryLikeOrderByPriceAsc(String category, Pageable pageable);

	public Page<Restaurant> findByCategoryLikeOrderByCreatedAtDesc(String category, Pageable pageable);

	public Page<Restaurant> findAllByOrderByCreatedAtDesc(Pageable pageable);

	public Page<Restaurant> findAllByOrderByPriceAsc(Pageable pageable);

	public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);

	public Page<Restaurant> findByCategoryIdAndNameLike(Integer id, String string, Pageable pageable);

	public Page<Restaurant> findByCategoryId(Integer id, Pageable pageable);

}
