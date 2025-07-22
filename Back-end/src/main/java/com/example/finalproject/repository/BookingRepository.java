package com.example.finalproject.repository;

import com.example.finalproject.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByCustomerId(Integer customerId);
    List<Booking> findByShopId(Integer shopId);
    List<Booking> findByAgent_Id(Integer agentId);
    @Query("SELECT bd.hairstylist.id AS stylistId, COUNT(bd.id) AS bookingCount " +
            "FROM BookingDetail bd GROUP BY bd.hairstylist.id ORDER BY bookingCount DESC")
    List<Object[]> findTopHairstylistsByBookingCount();

}
