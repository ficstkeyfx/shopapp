package com.example.shoppingapp.user.models;

public class UserModel {
    private String name, email, password, gender, birth, CCCD, ngayCap, noiCap, ngayHetHan, address, job, position, phone;
    private String avatar;
    public UserModel()
    {
        this.name = "Chưa cập nhật";
        this.email = "Chưa cập nhật";
        this.password = "Chưa cập nhật";
        this.gender = "Chưa cập nhật";
        this.birth = "Chưa cập nhật";
        this.CCCD = "Chưa cập nhật";
        this.ngayCap = "Chưa cập nhật";
        this.noiCap = "Chưa cập nhật";
        this.ngayHetHan = "Chưa cập nhật";
        this.address = "Chưa cập nhật";
        this.job = "Chưa cập nhật";
        this.position = "Chưa cập nhật";
        this.phone = "Chưa cập nhật";
    }

    public UserModel(String name, String email, String password, String gender, String birth, String CCCD, String ngayCap, String noiCap, String ngayHetHan, String address, String job, String position, String phone, String avatar) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birth = birth;
        this.CCCD = CCCD;
        this.ngayCap = ngayCap;
        this.noiCap = noiCap;
        this.ngayHetHan = ngayHetHan;
        this.address = address;
        this.job = job;
        this.position = position;
        this.phone = phone;
        this.avatar = avatar;
    }

    public UserModel(String name, String email, String password)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = "Chưa cập nhật";
        this.birth = "Chưa cập nhật";
        this.CCCD = "Chưa cập nhật";
        this.ngayCap = "Chưa cập nhật";
        this.noiCap = "Chưa cập nhật";
        this.ngayHetHan = "Chưa cập nhật";
        this.address = "Chưa cập nhật";
        this.job = "Chưa cập nhật";
        this.position = "Chưa cập nhật";
        this.phone = "Chưa cập nhật";
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public String getBirth() {
        return birth;
    }

    public String getCCCD() {
        return CCCD;
    }

    public String getGender() {
        return gender;
    }

    public String getJob() {
        return job;
    }

    public String getNgayCap() {
        return ngayCap;
    }

    public String getNgayHetHan() {
        return ngayHetHan;
    }

    public String getNoiCap() {
        return noiCap;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setNgayCap(String ngayCap) {
        this.ngayCap = ngayCap;
    }

    public void setNgayHetHan(String ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public void setNoiCap(String noiCap) {
        this.noiCap = noiCap;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
