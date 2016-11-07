package com.qingyuan.tianya.mrbuy.bean;

/**
 * Created by gaoyanjun on 2016/8/4.
 */
public class CarBean {
    private int goods_id;
    private String name;
    private String price;
    private boolean ischecked;
    private int num;
    private String pic;

    public CarBean(int goods_id, String name, String price,boolean ischecked,int num,String pic) {
        this.goods_id = goods_id;
        this.name = name;
        this.price = price;
        this.ischecked = ischecked;
        this.num = num;
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
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

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
