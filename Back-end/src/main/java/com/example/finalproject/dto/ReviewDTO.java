package com.example.finalproject.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private Integer reviewId;
    private Integer customerId;
    private Integer agentId;
    private Integer shopId;
    private Integer hairstylistId;
    private Integer rating;
    private String comment;
    private LocalDateTime timestamp;

    // 🆕 Tên để hiển thị
    private String agentName;
    private String shopName;
    private String hairstylistName;

    // 🆕 Thêm username và image của customer
    private String customerUsername;
    private String customerImage;

    public ReviewDTO() {}

    public ReviewDTO(Integer reviewId, Integer customerId, Integer agentId, Integer shopId,
                     Integer hairstylistId, Integer rating, String comment, LocalDateTime timestamp) {
        this.reviewId = reviewId;
        this.customerId = customerId;
        this.agentId = agentId;
        this.shopId = shopId;
        this.hairstylistId = hairstylistId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public Integer getReviewId() { return reviewId; }
    public void setReviewId(Integer reviewId) { this.reviewId = reviewId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getAgentId() { return agentId; }
    public void setAgentId(Integer agentId) { this.agentId = agentId; }

    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }

    public Integer getHairstylistId() { return hairstylistId; }
    public void setHairstylistId(Integer hairstylistId) { this.hairstylistId = hairstylistId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getHairstylistName() { return hairstylistName; }
    public void setHairstylistName(String hairstylistName) { this.hairstylistName = hairstylistName; }

    public String getCustomerUsername() { return customerUsername; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }

    public String getCustomerImage() { return customerImage; }
    public void setCustomerImage(String customerImage) { this.customerImage = customerImage; }
}
