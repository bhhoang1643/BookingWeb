package com.example.finalproject.repository;

import com.example.finalproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
    List<Category> findByAgentId(Integer agentId);
    boolean existsByNameAndAgentId(String name, Integer agentId);

}