package com.example.artie.client;


public class Order {

    private String items;
    private double price;
    private int id;
    private String status;

    public Order(){}

    public Order(int id, String items, double price, String status) {
        this.id = id;
        this.items = items;
        this.price = price;
        this.status = status;
    }


    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
