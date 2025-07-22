package com.example.finalproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class CustomerLoyalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private Integer convertPoints;

    private LocalDateTime datetime;

    // Constructors
    public CustomerLoyalty() {}

    public CustomerLoyalty(Customer customer, Integer convertPoints, LocalDateTime datetime) {
        this.customer = customer;
        this.convertPoints = convertPoints;
        this.datetime = datetime;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
