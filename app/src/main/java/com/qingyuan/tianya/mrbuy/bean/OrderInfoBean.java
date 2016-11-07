package com.qingyuan.tianya.mrbuy.bean;

/**
 * Created by gaoyanjun on 2016/8/17.
 */
public class OrderInfoBean {
    private String order_id;
    private String order_sn;
    private String oldprice;
    private String price;
    private String ctime;
    private String status;
    private String type;

    public OrderInfoBean(String order_id, String order_sn, String oldprice, String price, String ctime, String status, String type) {
        this.order_id = order_id;
        this.order_sn = order_sn;
        this.oldprice = oldprice;
        this.price = price;
        this.ctime = ctime;
        this.status = status;
        this.type = type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOldprice() {
        return oldprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
