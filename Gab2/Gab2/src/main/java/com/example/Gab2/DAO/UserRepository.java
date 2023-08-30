package com.example.Gab2.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Gab2.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findAllByOrderByFullNameAsc();

	List<User> findByFullNameStartsWith(String letter);
	
}