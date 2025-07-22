package com.example.finalproject.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @OneToOne
    @JoinColumn(name = "booking_id", unique = true)
    private Booking booking;
    @OneToOne
    @JoinColumn(name = "agent_package_id", unique = true)
    private AgentPackageSubscription agentPackage;

    @Column(nullable = false)
    private String paymentMethod; // VNPay | CASH

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = true)
    private BigDecimal remainingAmount;

    // Getters and Setters
    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    public AgentPackageSubscription getAgentPackage() {
        return agentPackage;
    }

    public void setAgentPackage(AgentPackageSubscription agentPackage) {
        this.agentPackage = agentPackage;
    }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
}
