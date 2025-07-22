package com.example.finalproject.controller;

import com.example.finalproject.config.JwtUtils;
import com.example.finalproject.dto.BookingDTO;
import com.example.finalproject.dto.HairstylistDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.entity.Booking;
import com.example.finalproject.repository.AgentRepository;
import com.example.finalproject.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:4200")
public class BookingController {

    private final BookingService bookingService;
    private final JwtUtils jwtUtils;
    private final AgentRepository agentRepository;

    public BookingController(BookingService bookingService, JwtUtils jwtUtils, AgentRepository agentRepository) {
        this.bookingService = bookingService;
        this.jwtUtils = jwtUtils;
        this.agentRepository = agentRepository;
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomerId(customerId));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByShopId(@PathVariable Integer shopId) {
        return ResponseEntity.ok(bookingService.getBookingsByShopId(shopId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<List<BookingDTO>> getBookingsByCurrentAgent(HttpServletRequest request) {
        Integer accountId = jwtUtils.extractAccountIdFromRequest(request);
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));
        return ResponseEntity.ok(bookingService.getBookingsByAgentId(agent.getId()));
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.status(201).body(bookingService.createBooking(bookingDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Integer id, @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingDTO));
    }

    @PutMapping("/booking-payments/{id}/confirm")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<?> confirmPayment(@PathVariable Integer id) {
        Booking booking = bookingService.getBookingByIdRaw(id)
                .orElseThrow(() -> new RuntimeException("Booking does not existi"));
        booking.setPaymentStatus("paid");
        bookingService.save(booking);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("✅ Booking has been deleted successfully!");
    }

    @GetMapping("/revenue/stylists")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Map<String, Object>> getRevenueByStylist(HttpServletRequest request) {
        Integer accountId = jwtUtils.extractAccountIdFromRequest(request);
        return ResponseEntity.ok(bookingService.calculateRevenueByStylist(accountId));
    }

    @GetMapping("/revenue/services")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Map<String, Object>> getRevenueByService(HttpServletRequest request) {
        Integer accountId = jwtUtils.extractAccountIdFromRequest(request);
        return ResponseEntity.ok(bookingService.calculateRevenueByService(accountId));
    }

    @GetMapping("/revenue/shops")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Map<String, Object>> getRevenueByShop(HttpServletRequest request) {
        Integer accountId = jwtUtils.extractAccountIdFromRequest(request);
        return ResponseEntity.ok(bookingService.calculateRevenueByShop(accountId));
    }

    @GetMapping("/revenue/days")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Map<String, Object>> getRevenueByDay(HttpServletRequest request) {
        Integer accountId = jwtUtils.extractAccountIdFromRequest(request);
        return ResponseEntity.ok(bookingService.calculateRevenueByDay(accountId));
    }

    @GetMapping("/revenue/months")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Map<String, Object>> getRevenueByMonth(HttpServletRequest request) {
        Integer accountId = jwtUtils.extractAccountIdFromRequest(request);
        return ResponseEntity.ok(bookingService.calculateRevenueByMonth(accountId));
    }
    @GetMapping("/top-stylists")
    public ResponseEntity<List<HairstylistDTO>> getTopStylists() {
        return ResponseEntity.ok(bookingService.getTopBookedStylists(100));
    }
}
