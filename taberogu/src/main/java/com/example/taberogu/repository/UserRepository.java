package com.example.taberogu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
