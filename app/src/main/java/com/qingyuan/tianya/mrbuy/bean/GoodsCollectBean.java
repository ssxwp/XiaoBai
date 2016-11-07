package com.qingyuan.tianya.mrbuy.bean;

/**
 * Created by gaoyanjun on 2016/8/11.
 */
public class GoodsCollectBean {
    private int goods_id;
    private String img;
    private String name;
    private String price;

    public GoodsCollectBean(int goods_id, String img, String name, String price) {
        this.goods_id = goods_id;
        this.img = img;
        this.name = name;
        this.price = price;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
