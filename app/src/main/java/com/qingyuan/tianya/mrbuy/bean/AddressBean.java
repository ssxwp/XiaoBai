package com.qingyuan.tianya.mrbuy.bean;

/**
 * Created by shensixiong on 2016/8/13.
 */
public class AddressBean {
    private int id;
    private  String province;//省
    private  String city;//市
    private String district;//区
    private String road;//街道
    private  String name;//姓名
    private  String phone;//手机号
    private boolean isChecked;

    public AddressBean(int id,String province, String city, String district, String road, String name, String phone,boolean isChecked) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.road = road;
        this.name = name;
        this.phone = phone;
        this.isChecked = isChecked;
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
