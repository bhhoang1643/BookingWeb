package com.example.finalproject.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @ManyToOne @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne @JoinColumn(name = "hairstylist_id")
    private Hairstylist hairstylist;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Review() {}

    public Review(Customer customer, Agent agent, Shop shop, Hairstylist hairstylist,
                  Integer rating, String comment, LocalDateTime timestamp) {
        this.customer = customer;
        this.agent = agent;
        this.shop = shop;
        this.hairstylist = hairstylist;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    // Getters and setters...
    public Integer getReviewId() { return reviewId; }
    public void setReviewId(Integer reviewId) { this.reviewId = reviewId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) { this.agent = agent; }

    public Shop getShop() { return shop; }
    public void setShop(Shop shop) { this.shop = shop; }

    public Hairstylist getHairstylist() { return hairstylist; }
    public void setHairstylist(Hairstylist hairstylist) { this.hairstylist = hairstylist; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}