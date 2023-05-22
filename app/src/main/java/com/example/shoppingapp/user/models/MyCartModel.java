package com.example.shoppingapp.user.models;

import java.io.Serializable;

public class MyCartModel implements Serializable {
    String productName, productDate, productTime, totalQuantity, img_url;
    int size;
    int totalPrice,productPrice;
    String documentId;
    String productType;
    public MyCartModel() {
    }

    public MyCartModel(String productName, String productDate, String productTime, String totalQuantity, String img_url, int totalPrice, int productPrice, String documentId, String productType, int size) {
        this.productName = productName;
        this.productDate = productDate;
        this.productTime = productTime;
        this.totalQuantity = totalQuantity;
        this.img_url = img_url;
        this.totalPrice = totalPrice;
        this.productPrice = productPrice;
        this.documentId = documentId;
        this.productType = productType;
        this.size = size;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
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

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
