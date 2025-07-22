package com.example.finalproject.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AgentPackageDTO {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String paymentStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer accountId;


    public AgentPackageDTO() {}

    public AgentPackageDTO(Integer id, String name, BigDecimal price, String paymentStatus,
                           LocalDate startDate, LocalDate endDate, Integer accountId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountId = accountId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
