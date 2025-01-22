package com.example.kadai_002.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {	
	public Users findByMailAddress(String mailAddress);
	public Page<Users> findByUserNameLikeOrMailAddressLike(String nameKeyword, String furiganaKeyword, Pageable pageable);
	}