package com.example.finalproject.controller;

import com.example.finalproject.dto.PaymentDTO;
import com.example.finalproject.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<?> payBooking(
            @PathVariable Integer bookingId,
            @RequestParam String method,
            @RequestParam(defaultValue = "0") int usedPoints) {
        try {
            method = method.trim().toUpperCase();
            if (!method.equals("CASH") && !method.equals("VNPAY") && !method.equals("POINT")) {
                return ResponseEntity.badRequest().body("❌ Only support payment by CASH, VNPAY or POINT");
            }
            PaymentDTO dto = paymentService.payBooking(bookingId, method, usedPoints);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ error: " + e.getMessage());
        }
    }


    @PostMapping("/order/{orderId}")
    public ResponseEntity<?> payOrder(
            @PathVariable Integer orderId,
            @RequestParam String method,
            @RequestParam(defaultValue = "0") int usedPoints) {
        try {
            method = method.trim().toUpperCase();
            if (!method.equals("CASH") && !method.equals("VNPAY") && !method.equals("POINT")) {
                return ResponseEntity.badRequest().body("❌ Only support payment by CASH, VNPAY or POINT");
            }
            PaymentDTO dto = paymentService.payOrder(orderId, method, usedPoints);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ error: " + e.getMessage());
        }
    }


    @PostMapping("/agent-package/{subscriptionId}")
    public ResponseEntity<?> payAgentPackage(
            @PathVariable Integer subscriptionId,
            @RequestParam String method) {
        try {
            method = method.trim().toUpperCase();
            if (!method.equals("CASH") && !method.equals("VNPAY")) {
                return ResponseEntity.badRequest().body("❌ Only support payment by CASH, VNPAY or POINT");
            }
            PaymentDTO dto = paymentService.payAgentPackage(subscriptionId, method);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ Lỗi: " + e.getMessage());
        }
    }


    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBooking(@PathVariable Integer bookingId) {
        Optional<PaymentDTO> dto = paymentService.getPaymentByBooking(bookingId);
        return dto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getPaymentByOrder(@PathVariable Integer orderId) {
        Optional<PaymentDTO> dto = paymentService.getPaymentByOrder(orderId);
        return dto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/vnpay/callback")
    public ResponseEntity<?> handleVnpayFakeReturn(@RequestParam Map<String, String> params) {
        return handleVnpayCallback(params);
    }

    @PostMapping("/vnpay/callback")
    public ResponseEntity<?> handleVnpayCallback(@RequestParam Map<String, String> params) {
        try {
            PaymentDTO dto = paymentService.handleVnpayCallback(params);
            return ResponseEntity.ok(dto != null ? dto : "✅ Agent package payment successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Payment failed: " + e.getMessage());
        }
    }
}
