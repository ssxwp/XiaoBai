package com.qingyuan.tianya.mrbuy.bean;

/**
 * Created by gaoyanjun on 2016/6/23.
 */
public class DishMessageBean {

    private int shop_id;//商铺id
    private String shop_name;//商铺名字
    private String head_pic;//头像
    private String province;//省
    private String city;//市
    private String district;//区
    private String p_price;//配送费
    private String q_price;//起送价
    private String status;//0：开门 1：关门
    private String volume;//销量
    private String price;//平均消费
    private String juli;//距离

    public DishMessageBean(int shop_id, String shop_name, String head_pic, String province, String city, String district, String p_price, String q_price, String status, String volume, String price,String juli) {
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.head_pic = head_pic;
        this.province = province;
        this.city = city;
        this.district = district;
        this.p_price = p_price;
        this.q_price = q_price;
        this.status = status;
        this.volume = volume;
        this.price = price;
        this.juli = juli;
    }

    public String getJuli() {
        return juli;
    }

    public void setJuli(String juli) {
        this.juli = juli;
    }

    public String getP_price() {
        return p_price;
    }

    public void setP_price(String p_price) {
        this.p_price = p_price;
    }

    public String getQ_price() {
        return q_price;
    }

    public void setQ_price(String q_price) {
        this.q_price = q_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
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
}
