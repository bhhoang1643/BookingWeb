package com.example.finalproject.repository;

import com.example.finalproject.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrder_Id(Integer orderId);
    Optional<Payment> findByBooking_BookingId(Integer id);
}

