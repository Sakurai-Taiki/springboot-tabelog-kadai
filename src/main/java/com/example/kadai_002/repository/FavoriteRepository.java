package com.example.kadai_002.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.entity.Users;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

	public Page<Favorite> findByUsersOrderByCreatedDateDesc(Users users, Pageable pageable);
    public Favorite findByStoresAndUsers(Stores stores, Users users);
}