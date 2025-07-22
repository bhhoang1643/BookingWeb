package com.example.finalproject.dto;

import java.util.List;

public class BookingDetailDTO {
    private Integer id;
    private Integer bookingId;
    private List<Integer> serviceIds;
    private Integer stylistId;

    public BookingDetailDTO() {}

    public BookingDetailDTO(Integer id, Integer bookingId, List<Integer> serviceIds, Integer stylistId) {
        this.id = id;
        this.bookingId = bookingId;
        this.serviceIds = serviceIds;
        this.stylistId = stylistId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public List<Integer> getServiceIds() {
        return serviceIds;
    }

    public Integer getStylistId() {
        return stylistId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public void setServiceIds(List<Integer> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public void setStylistId(Integer stylistId) {
        this.stylistId = stylistId;
    }
}
