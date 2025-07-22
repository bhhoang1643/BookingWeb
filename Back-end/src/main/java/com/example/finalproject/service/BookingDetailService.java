package com.example.finalproject.service;

import com.example.finalproject.dto.BookingDetailDTO;
import com.example.finalproject.entity.*;
import com.example.finalproject.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingDetailService {

    private final BookingDetailRepository bookingDetailRepository;
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final HairstylistRepository hairstylistRepository;

    public BookingDetailService(BookingDetailRepository bookingDetailRepository,
                                BookingRepository bookingRepository,
                                ServiceRepository serviceRepository,
                                HairstylistRepository hairstylistRepository) {
        this.bookingDetailRepository = bookingDetailRepository;
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
        this.hairstylistRepository = hairstylistRepository;
    }

    public List<BookingDetailDTO> getAllBookingDetails() {
        return bookingDetailRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDetailDTO> getBookingDetailsByBookingId(Integer bookingId) {
        return bookingDetailRepository.findByBooking_BookingId(bookingId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDetailDTO> getBookingDetailsByStylistId(Integer stylistId) {
        return bookingDetailRepository.findByHairstylist_Id(stylistId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BookingDetailDTO> createBookingDetail(BookingDetailDTO bookingDetailDTO) {
        Booking booking = bookingRepository.findById(bookingDetailDTO.getBookingId())
                .orElseThrow(() -> new RuntimeException("❌ Booking does not exist!"));

        Hairstylist hairstylist = hairstylistRepository.findById(bookingDetailDTO.getStylistId())
                .orElseThrow(() -> new RuntimeException("❌ Stylist does not exist!"));

        List<BookingDetail> bookingDetails = bookingDetailDTO.getServiceIds().stream().map(serviceId -> {
            ServiceEntity service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("❌ Service does not exist!"));
            return new BookingDetail(booking, service, hairstylist);
        }).collect(Collectors.toList());

        bookingDetailRepository.saveAll(bookingDetails);
        updateBookingTotalPrice(booking);

        return bookingDetails.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public BookingDetailDTO updateBookingDetail(Integer id, BookingDetailDTO bookingDetailDTO) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Booking Detail does not exist!"));

        if (bookingDetailDTO.getServiceIds() != null && !bookingDetailDTO.getServiceIds().isEmpty()) {
            ServiceEntity service = serviceRepository.findById(bookingDetailDTO.getServiceIds().get(0))
                    .orElseThrow(() -> new RuntimeException("❌ Service does not exist!"));
            bookingDetail.setService(service);
        }
        if (bookingDetailDTO.getStylistId() != null) {
            Hairstylist hairstylist = hairstylistRepository.findById(bookingDetailDTO.getStylistId())
                    .orElseThrow(() -> new RuntimeException("❌ Stylist does not exist!"));
            bookingDetail.setHairstylist(hairstylist);
        }

        bookingDetailRepository.save(bookingDetail);
        updateBookingTotalPrice(bookingDetail.getBooking());

        return convertToDTO(bookingDetail);
    }

    public void deleteBookingDetail(Integer id) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Booking Detail does not exist!"));

        Booking booking = bookingDetail.getBooking();
        bookingDetailRepository.deleteById(id);
        updateBookingTotalPrice(booking);
    }


    public boolean isStylistBusyAt(Integer stylistId, LocalDateTime targetTime) {
        List<BookingDetail> allDetails = bookingDetailRepository.findByHairstylist_Id(stylistId);
        for (BookingDetail detail : allDetails) {
            Booking booking = detail.getBooking();
            if (booking == null || booking.getDatetime() == null) continue;

            LocalDateTime bookedTime = booking.getDatetime();
            long diff = Math.abs(java.time.Duration.between(bookedTime, targetTime).toMinutes());
            if (diff < 60) return true;
        }
        return false;
    }

    private void updateBookingTotalPrice(Booking booking) {
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByBooking_BookingId(booking.getBookingId());
        BigDecimal totalPrice = bookingDetails.stream()
                .map(detail -> detail.getService().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        booking.setTotalPrice(totalPrice);
        bookingRepository.save(booking);
    }

    private BookingDetailDTO convertToDTO(BookingDetail bookingDetail) {
        return new BookingDetailDTO(
                bookingDetail.getId(),
                bookingDetail.getBooking().getBookingId(),
                List.of(bookingDetail.getService().getServiceId()),
                bookingDetail.getHairstylist().getId()
        );
    }
}