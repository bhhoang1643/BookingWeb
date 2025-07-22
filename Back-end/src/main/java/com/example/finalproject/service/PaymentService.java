package com.example.finalproject.service;

import com.example.finalproject.dto.PaymentDTO;
import com.example.finalproject.entity.*;
import com.example.finalproject.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final BookingRepository bookingRepo;
    private final OrderRepository orderRepo;
    private final CustomerLoyaltyRepository loyaltyRepo;
    private final CustomerRepository customerRepo;
    private final AgentPackageSubscriptionRepository agentPackageRepo;
    private final AccountRepository accountRepo;

    public PaymentService(PaymentRepository paymentRepo,
                          BookingRepository bookingRepo,
                          OrderRepository orderRepo,
                          CustomerLoyaltyRepository loyaltyRepo,
                          CustomerRepository customerRepo,
                          AgentPackageSubscriptionRepository agentPackageRepo,
                          AccountRepository accountRepo) {
        this.paymentRepo = paymentRepo;
        this.bookingRepo = bookingRepo;
        this.orderRepo = orderRepo;
        this.loyaltyRepo = loyaltyRepo;
        this.customerRepo = customerRepo;
        this.agentPackageRepo = agentPackageRepo;
        this.accountRepo = accountRepo;
    }

    // -------------------------- BOOKING --------------------------
    @Transactional
    public PaymentDTO payBooking(Integer bookingId, String method, int usedPoints) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("❌ Booking does not exist"));

        if ("paid".equalsIgnoreCase(booking.getPaymentStatus())) {
            throw new RuntimeException("✅ Booking paid");
        }

        Customer customer = booking.getCustomer();
        int customerPoints = customer.getPoint() != null ? customer.getPoint() : 0;
        BigDecimal totalAmount = booking.getTotalPrice();

        usedPoints = Math.min(usedPoints, customerPoints);
        BigDecimal discount = BigDecimal.valueOf(usedPoints * 1000L);
        BigDecimal remainingAmount = totalAmount.subtract(discount).max(BigDecimal.ZERO);

        if (method.equalsIgnoreCase("POINT") && discount.compareTo(totalAmount) < 0) {
            throw new RuntimeException("❌ Not enough points to pay for the entire booking!");
        }

        if (usedPoints > 0) {
            customer.setPoint(customerPoints - usedPoints);
            customerRepo.save(customer);
            loyaltyRepo.save(new CustomerLoyalty(customer, usedPoints, LocalDateTime.now()));
        }

        if (!"CASH".equalsIgnoreCase(method)) {
            booking.setPaymentStatus("paid");

            int earnedPoints = totalAmount.multiply(BigDecimal.valueOf(0.01))
                    .divide(BigDecimal.valueOf(1000), 0, BigDecimal.ROUND_DOWN)
                    .intValue();

            customer.setPoint(customer.getPoint() + earnedPoints);
            customerRepo.save(customer);
        } else {
            booking.setPaymentStatus("unpaid");
        }

        booking.setTotalPrice(remainingAmount);
        bookingRepo.saveAndFlush(booking);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(method);
        payment.setTransactionDate(LocalDateTime.now());
        payment.setRemainingAmount(remainingAmount);
        Payment saved = paymentRepo.save(payment);

        return new PaymentDTO(saved.getPaymentId(), method, saved.getTransactionDate(),
                null, booking.getBookingId(), null, usedPoints, remainingAmount.intValue());
    }

    // -------------------------- ORDER --------------------------
    @Transactional
    public PaymentDTO payOrder(Integer orderId, String method, int usedPoints) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("❌ Order does not exist"));

        if ("paid".equalsIgnoreCase(order.getPaymentStatus())) {
            throw new RuntimeException("✅ Order paid");
        }

        Customer customer = order.getCustomer();
        int customerPoints = customer.getPoint() != null ? customer.getPoint() : 0;
        BigDecimal totalAmount = order.getTotalPrice();

        usedPoints = Math.min(usedPoints, customerPoints);
        BigDecimal discount = BigDecimal.valueOf(usedPoints * 1000L);
        BigDecimal remainingAmount = totalAmount.subtract(discount).max(BigDecimal.ZERO);

        if (method.equalsIgnoreCase("POINT") && discount.compareTo(totalAmount) < 0) {
            throw new RuntimeException("❌ Not enough points to pay for the entire order!");
        }

        if (usedPoints > 0) {
            customer.setPoint(customerPoints - usedPoints);
            customerRepo.save(customer);
            loyaltyRepo.save(new CustomerLoyalty(customer, usedPoints, LocalDateTime.now()));
        }

        if (!"CASH".equalsIgnoreCase(method)) {

            order.setPaymentStatus("paid");

            int earnedPoints = totalAmount.multiply(BigDecimal.valueOf(0.01))
                    .divide(BigDecimal.valueOf(1000), 0, BigDecimal.ROUND_DOWN)
                    .intValue();

            customer.setPoint(customer.getPoint() + earnedPoints);
            customerRepo.save(customer);
        } else {

            order.setPaymentStatus("unpaid");
        }

        order.setTotalPrice(remainingAmount);
        orderRepo.saveAndFlush(order);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(method);
        payment.setTransactionDate(LocalDateTime.now());
        payment.setRemainingAmount(remainingAmount);
        Payment saved = paymentRepo.save(payment);

        return new PaymentDTO(saved.getPaymentId(), method, saved.getTransactionDate(),
                order.getId(), null, null, usedPoints, remainingAmount.intValue());
    }

    @Transactional
    public PaymentDTO payAgentPackage(Integer subscriptionId, String method) {
        AgentPackageSubscription sub = agentPackageRepo.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("❌ Package does not exist"));

        if ("paid".equalsIgnoreCase(sub.getPaymentStatus())) {
            throw new RuntimeException("✅ Package has been paid in advance");
        }

        // Tạo bản ghi Payment
        Payment payment = new Payment();
        payment.setAgentPackage(sub);
        payment.setPaymentMethod(method);
        payment.setTransactionDate(LocalDateTime.now());
        payment.setRemainingAmount(sub.getPrice());

        Payment savedPayment = paymentRepo.save(payment);


        sub.setPaymentStatus("paid");
        agentPackageRepo.save(sub);


        Account account = accountRepo.findById(sub.getAccountId())
                .orElseThrow(() -> new RuntimeException("❌ Agent account not found"));
        account.setStatus("ACTIVE");
        accountRepo.save(account);


        return new PaymentDTO(
                savedPayment.getPaymentId(),
                method,
                savedPayment.getTransactionDate(),
                null,
                null,
                sub.getId(),
                0,
                sub.getPrice().intValue()
        );
    }

    // -------------------------- VNPAY CALLBACK --------------------------
    @Transactional
    public PaymentDTO handleVnpayCallback(Map<String, String> params) {
        String code = params.get("vnp_ResponseCode");
        String txnRef = params.get("vnp_TxnRef");

        if (!"00".equals(code)) {
            throw new RuntimeException("❌ Transaction failed from VNPay");
        }

        if (txnRef != null && txnRef.startsWith("BOOK_")) {
            String[] parts = txnRef.split("_");
            Integer bookingId = Integer.parseInt(parts[1]);
            int usedPoints = (parts.length >= 4 && parts[2].equals("PTS")) ? Integer.parseInt(parts[3]) : 0;
            return payBooking(bookingId, "VNPAY", usedPoints);
        } else if (txnRef != null && txnRef.startsWith("ORDER_")) {
            String[] parts = txnRef.split("_");
            Integer orderId = Integer.parseInt(parts[1]);
            int usedPoints = (parts.length >= 4 && parts[2].equals("PTS")) ? Integer.parseInt(parts[3]) : 0;
            return payOrder(orderId, "VNPAY", usedPoints);
        } else if (txnRef != null && txnRef.startsWith("AGENTPKG_")) {
            Integer pkgId = Integer.parseInt(txnRef.split("_")[1]);
            return payAgentPackage(pkgId, "VNPAY");
        } else {
            throw new RuntimeException("❌Transaction type not specified");
        }
    }

    // -------------------------- UTILS --------------------------
    private PaymentDTO convertToDTO(Payment payment) {
        Integer bookingId = payment.getBooking() != null ? payment.getBooking().getBookingId() : null;
        Integer orderId = payment.getOrder() != null ? payment.getOrder().getId() : null;
        Integer agentPackageId = payment.getAgentPackage() != null ? payment.getAgentPackage().getId() : null;

        return new PaymentDTO(
                payment.getPaymentId(),
                payment.getPaymentMethod(),
                payment.getTransactionDate(),
                orderId,
                bookingId,
                agentPackageId,
                0,
                payment.getRemainingAmount() != null ? payment.getRemainingAmount().intValue() : 0
        );
    }

    public Optional<PaymentDTO> getPaymentByBooking(Integer bookingId) {
        return paymentRepo.findByBooking_BookingId(bookingId).map(this::convertToDTO);
    }

    public Optional<PaymentDTO> getPaymentByOrder(Integer orderId) {
        return paymentRepo.findByOrder_Id(orderId).map(this::convertToDTO);
    }
}
