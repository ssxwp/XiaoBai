package com.qingyuan.tianya.mrbuy.bean;

/**
 * Created by gaoyanjun on 2016/8/6.
 */
public class MerCollectBean {
    private int shop_id;//商铺id
    private String shop_name;//商铺名字
    private String head_pic;//头像

    public MerCollectBean(int shop_id, String shop_name, String head_pic) {
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.head_pic = head_pic;
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

}
