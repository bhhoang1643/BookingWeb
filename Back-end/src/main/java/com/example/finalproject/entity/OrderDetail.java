package com.example.finalproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public OrderDetail() {}

    public OrderDetail(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        updateOrderTotalPrice();
    }


    private void updateOrderTotalPrice() {
        if (order != null) {
            order.recalculateTotalPrice();
        }
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) {
        this.order = order;
        updateOrderTotalPrice();
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) {
        this.product = product;
        updateOrderTotalPrice();
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        updateOrderTotalPrice();
    }
}
