package com.example.finalproject.repository;

import com.example.finalproject.entity.StyleTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StyleTagRepository extends JpaRepository<StyleTag, Long> {
    Optional<StyleTag> findByName(String name);
    boolean existsByName(String name);
}
