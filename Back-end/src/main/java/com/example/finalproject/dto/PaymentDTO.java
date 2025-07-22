package com.example.finalproject.dto;

import java.time.LocalDateTime;

public class PaymentDTO {
    private Integer paymentId;
    private String method;
    private LocalDateTime transactionDate;
    private Integer orderId;
    private Integer bookingId;
    private Integer agentPackageId;
    private Integer pointUsed;
    private Integer remainingAmount;

    public PaymentDTO(Integer paymentId, String method, LocalDateTime transactionDate,
                      Integer orderId, Integer bookingId, Integer agentPackageId,
                      Integer pointUsed, Integer remainingAmount) {
        this.paymentId = paymentId;
        this.method = method;
        this.transactionDate = transactionDate;
        this.orderId = orderId;
        this.bookingId = bookingId;
        this.agentPackageId = agentPackageId;
        this.pointUsed = pointUsed;
        this.remainingAmount = remainingAmount;
    }


    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getAgentPackageId() {
        return agentPackageId;
    }

    public void setAgentPackageId(Integer agentPackageId) {
        this.agentPackageId = agentPackageId;
    }

    public Integer getPointUsed() {
        return pointUsed;
    }

    public void setPointUsed(Integer pointUsed) {
        this.pointUsed = pointUsed;
    }

    public Integer getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
}
