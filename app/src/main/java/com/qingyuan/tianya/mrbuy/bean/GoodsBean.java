package com.qingyuan.tianya.mrbuy.bean;

/**
 * Created by gaoyanjun on 2016/6/23.
 */
public class GoodsBean {
    private  int goods_id;//商品id
    private String goods_name;//商品名字
    private String price;//价格
    private String s_price;//市场价格
    private String p_score;//人均消费
    private String score;//评分
    private String ytime;//营业时间
    private String remark;//备注
    private String pic_1;//图片
    private int num;//数量

    public GoodsBean(int goods_id, String goods_name, String price, String s_price, String p_score, String score, String ytime, String remark, String pic_1,int num) {
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.price = price;
        this.s_price = s_price;
        this.p_score = p_score;
        this.score = score;
        this.ytime = ytime;
        this.remark = remark;
        this.pic_1 = pic_1;
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getS_price() {
        return s_price;
    }

    public void setS_price(String s_price) {
        this.s_price = s_price;
    }

    public String getP_score() {
        return p_score;
    }

    public void setP_score(String p_score) {
        this.p_score = p_score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getYtime() {
        return ytime;
    }

    public void setYtime(String ytime) {
        this.ytime = ytime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPic_1() {
        return pic_1;
    }

    public void setPic_1(String pic_1) {
        this.pic_1 = pic_1;
    }
}
