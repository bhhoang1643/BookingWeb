package com.example.finalproject.controller;

import com.example.finalproject.dto.BookingDetailDTO;
import com.example.finalproject.service.BookingDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/booking-details")
@CrossOrigin(origins = "http://localhost:4200")
public class BookingDetailController {

    private final BookingDetailService bookingDetailService;

    public BookingDetailController(BookingDetailService bookingDetailService) {
        this.bookingDetailService = bookingDetailService;
    }

    @GetMapping
    public ResponseEntity<List<BookingDetailDTO>> getAllBookingDetails() {
        return ResponseEntity.ok(bookingDetailService.getAllBookingDetails());
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<BookingDetailDTO>> getBookingDetailsByBookingId(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailsByBookingId(bookingId));
    }

    @GetMapping("/stylist/{stylistId}")
    public ResponseEntity<List<BookingDetailDTO>> getBookingDetailsByStylistId(@PathVariable Integer stylistId) {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailsByStylistId(stylistId));
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkStylistAvailability(
            @RequestParam Integer stylistId,
            @RequestParam String datetime
    ) {
        try {
            LocalDateTime targetTime = LocalDateTime.parse(datetime);
            boolean isBusy = bookingDetailService.isStylistBusyAt(stylistId, targetTime);
            return ResponseEntity.ok(!isBusy);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @PostMapping
    public ResponseEntity<List<BookingDetailDTO>> createBookingDetail(@RequestBody BookingDetailDTO dto) {
        return ResponseEntity.status(201).body(bookingDetailService.createBookingDetail(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDetailDTO> updateBookingDetail(@PathVariable Integer id, @RequestBody BookingDetailDTO dto) {
        return ResponseEntity.ok(bookingDetailService.updateBookingDetail(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookingDetail(@PathVariable Integer id) {
        bookingDetailService.deleteBookingDetail(id);
        return ResponseEntity.ok("✅ Booking Detail has been deleted!");
    }
}
