package com.example.kadai_002.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Review;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.entity.Users;
public interface ReviewRepository  extends JpaRepository<Review, Integer> {
    public List<Review> findTop6ByStoresOrderByCreatedDateDesc(Stores stores);
    public Review findByStoresAndUsers(Stores stores, Users users);
    public long countByStores(Stores stores);
    public Page<Review> findByStoresOrderByCreatedDateDesc(Stores stores, Pageable pageable);
}
