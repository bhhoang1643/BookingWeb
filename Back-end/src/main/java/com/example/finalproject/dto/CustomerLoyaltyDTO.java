package com.example.finalproject.dto;

import java.time.LocalDateTime;

public class CustomerLoyaltyDTO {
    private Integer id;
    private Integer customerId;
    private Integer convertPoints;
    private LocalDateTime datetime;

    public CustomerLoyaltyDTO() {}

    public CustomerLoyaltyDTO(Integer id, Integer customerId, Integer convertPoints, LocalDateTime datetime) {
        this.id = id;
        this.customerId = customerId;
        this.convertPoints = convertPoints;
        this.datetime = datetime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getConvertPoints() {
        return convertPoints;
    }

    public void setConvertPoints(Integer convertPoints) {
        this.convertPoints = convertPoints;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}
