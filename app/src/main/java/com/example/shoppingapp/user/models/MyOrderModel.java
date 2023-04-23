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
    private String Order_ID  ;

    public String getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(String order_ID) {
        Order_ID = order_ID;
    }

    public MyOrderModel(String totalPrice, String productDate,String productName, String productTime, String address,String totalQuantity, List<MyCartModel> cartList, int status) {
        this.totalPrice = totalPrice;
        this.productDate = productDate;
        this.productTime = productTime;
        this.address = address;
        this.cartList = cartList;
        this.status = status;
        this.totalQuantity = totalQuantity;
        this.productName = productName;
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
