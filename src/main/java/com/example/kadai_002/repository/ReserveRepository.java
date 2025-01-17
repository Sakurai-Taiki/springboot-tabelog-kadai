package com.example.kadai_002.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Reserve;
import com.example.kadai_002.entity.Users;

public interface ReserveRepository extends JpaRepository<Reserve, Integer> {
    Page<Reserve> findByUsers(Users users, Pageable pageable); // メソッド名を修正
}
