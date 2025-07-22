package com.example.finalproject.repository;

import com.example.finalproject.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory_Id(Integer categoryId);

    List<Product> findByAgentId(Integer agentId);

    Optional<Product> findByIdAndAgentId(Integer id, Integer agentId);

    boolean existsByIdAndAgentId(Integer id, Integer agentId);

    List<Product> findByCategoryIdAndAgentId(Integer categoryId, Integer agentId);

}
