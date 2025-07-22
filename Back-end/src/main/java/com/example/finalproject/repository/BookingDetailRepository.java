package com.example.finalproject.repository;

import com.example.finalproject.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
    List<BookingDetail> findByBooking_BookingId(Integer bookingId);
    List<BookingDetail> findByHairstylist_Id(Integer stylistId);
}
