package com.example.finalproject.repository;

import com.example.finalproject.entity.Hairstylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HairstylistRepository extends JpaRepository<Hairstylist, Integer> {
    List<Hairstylist> findByShopId(Integer shopId);
    boolean existsByIdAndShopId(Integer id, Integer shopId);
    Optional<Hairstylist> findByIdAndShopId(Integer id, Integer shopId);
}

