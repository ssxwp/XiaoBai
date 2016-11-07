package com.qingyuan.tianya.mrbuy.api;
/**
 * @description Android顾客端请求服务器的API
 */
public class ApiConstant {


	public static final String URL = "http://114.215.78.102/index.php/Api/";
	//此接口下都做了验证
	public static final String DATA_URL = "member/";
	public static final String RECHARGE_URL="Category/";
	public static final String CENTER_URL="MemberCenter/";
	public static final String ORDER="Order/";

	//获得大类
	public static final String CATE = RECHARGE_URL+"get_cate_1";
	//获得大类下的小类
	public static final String CATES = RECHARGE_URL+"get_cate_2";
	//广告
	public  static final String ADVER =DATA_URL + "get_advert";
	//登录
	public  static final String LOGIN =DATA_URL + "land";
	//注册
	public static final String REGIST= DATA_URL+"addMember";
	//修改密码
	public static final String PASSWORD= DATA_URL+"eidt_password";
	//修改昵称
	public static final String UPDATENICKNAME= DATA_URL+"editMember";
	//查询用户信息
	public static final String QUERYMESSAGE= DATA_URL+"getMemberInfo";
	//获取验证码
	public static final String SENDMESSAGE= DATA_URL+"send";
	//商店列表
	public static final String DISHMERCHANT= RECHARGE_URL+"get_shop";
	//查询商店信息
	public static final String SHOPMESSAGE= RECHARGE_URL+"get_shopinfo";
	//查询商品列表
	public static final String GOODSMESSAGE= RECHARGE_URL+"get_goods";
	//商品详情
	public static final String GOODSINFO = RECHARGE_URL+"get_goodsinfo";
	//添加购物车
	public static final String ADDCAR = CENTER_URL+"add_shopcar";
	//购物车列表
	public static final String CARMESSAGE = CENTER_URL+"shopcarList";
	//购物车删除
	public static final String CARDEL = CENTER_URL+"del_shopcar";
	//添加收藏商家
	public static final String ADDCOLLECTION = CENTER_URL+"add_shopcollect";
	//删除收藏商家
	public static final String DELCOLLECTION = CENTER_URL+"del_shopcollect";
	//查询收藏商家
	public static final String QUECOLLECTION = CENTER_URL+"shopcollectList";
	//判断是否收藏商家
	public static final String ISCOLLECTION = CENTER_URL+"is_shopcollect";
	//添加收藏商品
	public static final String ADDCOLLECT = CENTER_URL+"add_goodscollect";
	//删除收藏商品
	public static final String DELCOLLECT = CENTER_URL+"del_goodscollect";
	//查询收藏商品
	public static final String QUECOLLECT = CENTER_URL+"goodscollectList";
	//判断是否收藏商品
	public static final String ISCOLLECT = CENTER_URL+"is_goodscollect";
	//创建订单
	public static final String ADDORDER = ORDER+"addOrder";
	//创建配送订单
	public static final String ADDYORDER=ORDER+"addYorder";
	//订单详情
	public static final String ORDERMESSAGE= ORDER+"orderInfo";
	//订单列表
	public static final String ORDERLIST= ORDER+"orderList";
	//确认收货
	public static final String MAKESURE= ORDER+"affirmGoods";
	//搜索
	public static final String SEARCH = RECHARGE_URL+"search_to";
	//地址列表
	public static final String ADDRESS = ORDER+"siteList";
	//添加地址
	public static final String ADDADDRESS = ORDER+"add_site";
	//删除地址
	public static final String DELADDRESS = ORDER+"delSite";
	//会员价格
	public static final String VIPPRICE = ORDER+"priceList";
	//充值会员
	public static final String ADDVIP = ORDER+"addHOrder";
	//支付宝支付成功回调
	public static final String PAYBACK = ORDER+"succOrder";
	//钱包支付
	public static final String PURSEPAY=ORDER+"succOrdery";
	//钱包充值
	public static final String PURSEMONEY = ORDER+"addCorder";
	//轮播图
	public static final String ADVLIST = "index/advList";
}
