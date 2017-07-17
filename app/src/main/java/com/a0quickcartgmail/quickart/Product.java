package com.a0quickcartgmail.quickart;



public class Product {

    public String id;
    public String name;
    public int price;

    public Product() {

    }

    public Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }
    public String getName() {return name;}
    public int getPrice() {
        return price;
    }
}