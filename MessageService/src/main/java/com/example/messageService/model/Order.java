package com.example.messageService.model;

public class Order {

    private String orderId;
    private String email;
    private String product;

    public Order() {}

    public Order(String orderId, String email, String product) {
        this.orderId = orderId;
        this.email = email;
        this.product = product;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
}
