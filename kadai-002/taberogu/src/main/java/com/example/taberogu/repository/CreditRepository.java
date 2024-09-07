package com.example.taberogu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Credit;

public interface CreditRepository extends JpaRepository<Credit, Integer> {

	Optional<Credit> findByUserId(int id);

}
