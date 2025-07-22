package com.example.finalproject.entity;

import jakarta.persistence.*;

@Entity
public class BookingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    @ManyToOne
    @JoinColumn(name = "stylist_id", nullable = false)
    private Hairstylist hairstylist;

    public BookingDetail() {}

    public BookingDetail(Booking booking, ServiceEntity service, Hairstylist hairstylist) {
        this.booking = booking;
        this.service = service;
        this.hairstylist = hairstylist;
    }

    public Integer getId() {
        return id;
    }

    public Booking getBooking() {
        return booking;
    }

    public ServiceEntity getService() {
        return service;
    }

    public Hairstylist getHairstylist() {
        return hairstylist;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }

    public void setHairstylist(Hairstylist hairstylist) {
        this.hairstylist = hairstylist;
    }
}
