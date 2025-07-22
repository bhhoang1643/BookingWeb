package com.example.finalproject.entity;

import jakarta.persistence.*;

@Entity
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "style_tag_id", nullable = false)
    private StyleTag styleTag;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Constructors
    public Preferences() {}

    public Preferences(Customer customer, StyleTag styleTag) {
        this.customer = customer;
        this.styleTag = styleTag;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StyleTag getStyleTag() {
        return styleTag;
    }

    public void setStyleTag(StyleTag styleTag) {
        this.styleTag = styleTag;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
