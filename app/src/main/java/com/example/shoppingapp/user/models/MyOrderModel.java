package com.example.shoppingapp.user.models;

import java.util.List;

public class MyOrderModel {
    private String totalPrice ;
    private String productDate;
    private String productTime;
    private String productName;
    private String address ;
    private String totalQuantity;
    private List <MyCartModel> cartList ;
    private int status  ;
    private String Order_ID ;
    private int size;

    public MyOrderModel(String totalPrice, String productDate, String productTime, String productName, String address, String totalQuantity, List<MyCartModel> cartList, int status, String order_ID, int size) {
        this.totalPrice = totalPrice;
        this.productDate = productDate;
        this.productTime = productTime;
        this.productName = productName;
        this.address = address;
        this.totalQuantity = totalQuantity;
        this.cartList = cartList;
        this.status = status;
        Order_ID = order_ID;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(String order_ID) {
        Order_ID = order_ID;
    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MyOrderModel() {
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<MyCartModel> getCartList() {
        return cartList;
    }

    public void setCartList(List<MyCartModel> cartList) {
        this.cartList = cartList;
    }
}
