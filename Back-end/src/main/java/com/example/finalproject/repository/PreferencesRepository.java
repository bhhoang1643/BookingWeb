package com.example.finalproject.repository;

import com.example.finalproject.entity.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Integer> {
    List<Preferences> findByCustomer_Id(Integer customerId);
}
