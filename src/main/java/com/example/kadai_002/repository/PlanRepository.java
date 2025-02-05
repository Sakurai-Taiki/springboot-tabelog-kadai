package com.example.kadai_002.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Integer> {
    Plan findByName(String name);
}