package com.example.finalproject.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class AgentPackageSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "unpaid";

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // 👉 Chuyển sang lưu accountId thay vì Agent
    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    // Constructors
    public AgentPackageSubscription() {}

    public AgentPackageSubscription(String name, BigDecimal price, String paymentStatus,
                                    LocalDate startDate, LocalDate endDate, Integer accountId) {
        this.name = name;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountId = accountId;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }
}
