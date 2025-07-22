package com.example.finalproject.repository;

import com.example.finalproject.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    List<Shop> findByAgentId(Integer agentId);
    Optional<Shop> findByIdAndAgentId(Integer id, Integer agentId);
    boolean existsByIdAndAgentId(Integer id, Integer agentId);
}
