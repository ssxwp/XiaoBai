package com.qingyuan.tianya.mrbuy.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.OrderGoodsAdapter;
import com.qingyuan.tianya.mrbuy.alipay.PayResult;
import com.qingyuan.tianya.mrbuy.alipay.SignUtils;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.OrderGoodsBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 订单信息
 */
public class OrderActivity extends BaseActivity{

    private ArrayList<OrderGoodsBean> mList;
    private TextView order_pay_rel;
    private String o_id;
    private ListView mListView;
    private String price;

    public static final String PARTNER = "2088802330143213";
    public static final String SELLER = "15803125513";
    public static final String RSA_PRIVATE = "MIICXwIBAAKBgQDMQ0ISqpTkTLdtm6+ukBYuRvcDJD8pyIozEVxzOzZe+qHmIRKj7TsCvpWyIII7C4V0GQIV5c/cVf9FN7uUduKktFslDvV2xL1e6Hl7IrfE9PuwI34ImgRaq2URLi1gIApSOxiKTS4y6ITcCmE1tJQR1bc06m0RqHjc3e5mLWRSUQIDAQABAoGBAMk6HTZsbB/439+QN2OeOOvAJ4olxD/yewtSOCShGbA58xLWVBnAFJlgjfKcig+HCyLTnDVcYAjtg3x6KkZZVG4wXBVR2RilK4RyC3ZIVnUtSsHxInaPp86secyJx6yM9jSLa8bp8vdM2YlrOnQmBWotI8WR4vxIaZanL0QcySYBAkEA9HYXNjz1SDpoDZS0gMzSEboZqYlD7fZbGtsdp/4sdWyQiXz2u8M0tJz/QhcRsEKpDn6Nqzo9Vnd2PY4UlUY84QJBANXnbzbc51VX7q9HF/1WrdUKoFiTfkFL3WBQLvJ1hJePk0mZ8wyYSBXm/TG290K8FAF3E8wd34AZugSOKIX203ECQQDMpVszJX60k2huDhG5TT2CZ4d6HJYITFbiOZIvAyGWv2aUIk+uyXCCzncFjahaJMO/hYP1VpDopKMQTrO+bwkhAkEAtK2JTkEnkfn1Xc5iYBYgPQx9xnPHOGZZdqokDZ7gQZj/4zKLjv7uHLoA8kZG8vKkv1wSt94f1Ddm3cQqTIXhsQJBAJTddmcbvMPuGxAFVpPdZRQ422Q2n28sZUBa69XRQ2Y5jgv5LYRAh/2bnVnFcuIV3WalZG0Ny5qts6VeGclhpWA=";
    //public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMQ0ISqpTkTLdtm6+ukBYuRvcDJD8pyIozEVxzOzZe+qHmIRKj7TsCvpWyIII7C4V0GQIV5c/cVf9FN7uUduKktFslDvV2xL1e6Hl7IrfE9PuwI34ImgRaq2URLi1gIApSOxiKTS4y6ITcCmE1tJQR1bc06m0RqHjc3e5mLWRSUQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    private TextView order_money_text1;
    private String order_id;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_address;
    private TextView purse;
    private TextView alipay;
    private int type;
    private OrderGoodsAdapter adapter;
    private String orderfrom;
    private RelativeLayout ac_order_men_rel;
    private TextView order_sure;
    private String status;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        mListView = ((ListView) findViewById(R.id.order_goods_list));
        purse = ((TextView) findViewById(R.id.order_type_text));
        alipay = ((TextView) findViewById(R.id.order_aliy_text));
        //order_money_text = ((TextView) findViewById(R.id.order_money_text));
        order_money_text1 = ((TextView) findViewById(R.id.order_money_text1));
        order_pay_rel = ((TextView) findViewById(R.id.order_pay_rel));
        ac_order_men_rel = ((RelativeLayout) findViewById(R.id.ac_order_men_rel));
        tv_name = (TextView)findViewById(R.id.ac_order_name_text);
        tv_phone = (TextView)findViewById(R.id.ac_order_phone_text);
        tv_address = (TextView)findViewById(R.id.ac_order_address);
        order_sure = (TextView)findViewById(R.id.order_sure);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        o_id = bundle.getString("o_id");
        orderfrom = bundle.getString("orderfrom");
        if (StringUtil.isNotEmpty(orderfrom)) {
            if (orderfrom.equals("2")) {
                ac_order_men_rel.setVisibility(View.GONE);
                mListView.setVisibility(View.GONE);
            }
        }
        getOrderMessage();
    }

    /**
     * 查询订单信息
     */
    private void getOrderMessage() {
        mList = new ArrayList<>();
        initProgressDialog();
        String urlString = ApiConstant.ORDERMESSAGE;
        RequestParams params = new RequestParams();
        params.put("order_id", o_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {


            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject customer = jObj.getJSONObject("responseList");
                        order_id = customer.getString("order_id");
                        price = customer.getString("price");
                        status = customer.getString("status");
                        switch (status) {
                            case "0":
                                order_pay_rel.setText("支付");
                                order_sure.setVisibility(View.GONE);
                                break;
                            case "8":
                                order_pay_rel.setText("确认收货");
                                order_sure.setVisibility(View.VISIBLE);
                                break;
                            default:
                                order_pay_rel.setVisibility(View.GONE);
                                order_sure.setVisibility(View.GONE);
                                break;
                        }
                        //old_price = customer.getString("old_price");
                        //order_money_text.setText("原价:￥" + old_price);
                        order_money_text1.setText("付款:￥" + price);
                        JSONObject object = customer.getJSONObject("where");
                        String name = object.getString("name");
                        String phone = object.getString("phone");
                        String road = object.getString("road");
                        tv_name.setText("收件人：" + name);
                        tv_phone.setText("联系电话：" + phone);
                        tv_address.setText("地址：" + road);
                        JSONArray array = customer.getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject cus = array.getJSONObject(i);
                            String num = cus.getString("nub");
                            String goods_name = cus.getString("name");
                            String price = cus.getString("price");
                            String hprice = cus.getString("h_price");
                            String pic = cus.getString("pic");
                            //String pic = "http://m2.quanjing.com/2m/fod_liv002/fo-11171537.jpg";
                            OrderGoodsBean orderBean = new OrderGoodsBean(num, goods_name, price, hprice,pic);
                            mList.add(orderBean);
                        }

                        if (adapter == null) {
                            adapter = new OrderGoodsAdapter(OrderActivity.this, mList);
                            mListView.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(mListView);
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                        //mListView.setOnItemClickListener(this);
                    } else {
                        Toast.makeText(OrderActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
                close();
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }

    @Override
    public void initClick() {
        order_pay_rel.setOnClickListener(this);
        purse.setOnClickListener(this);
        alipay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_type_text:
                alipay.setTextColor(Color.GRAY);
                purse.setTextColor(Color.RED);
                type= 1;
                break;
            case R.id.order_aliy_text:
                alipay.setTextColor(Color.RED);
                purse.setTextColor(Color.GRAY);
                type=2;
                break;
            case R.id.order_pay_rel:
                if (status.equals("0")) {
                    if (type == 2) {
                        alipayMsg();
                    } else if (type == 1) {
                        pursePay();
                    } else {
                        toast("请选择支付方式");
                    }
                }else if (status.equals("8")){
                    dialog = new AlertDialog.Builder(OrderActivity.this).create();
                    dialog.show();
                    dialog.getWindow().setContentView(R.layout.dialog_sure);
                    dialog.getWindow().findViewById(R.id.dialog_out_diss).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.getWindow().findViewById(R.id.dialog_out_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            makeSure();
                        }
                    });
                }
                break;
        }
    }

    private void makeSure() {
        initProgressDialog();
        String urlString = ApiConstant.MAKESURE;
        RequestParams params = new RequestParams();
        params.put("order_id",order_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                       OrderActivity.this.finish();
                    } else {
                        Toast.makeText(OrderActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
                close();
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }

    //钱包支付
    private void pursePay() {
        initProgressDialog();
        String urlString = ApiConstant.PURSEPAY;
        RequestParams params = new RequestParams();
        params.put("order_id",order_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        toast("支付成功");
                        skipActivityForClose(OrderActivity.this,OrderInfoActivity.class,null);
                    } else {
                        Toast.makeText(OrderActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
                close();
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }

    private void payBack() {
        String urlString = ApiConstant.PAYBACK;
        RequestParams params = new RequestParams();
        //params.put("mem_id", m_id);
        params.put("order_id",order_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        toast(jObj.getString("message"));
                        skipActivityForClose(OrderActivity.this,OrderInfoActivity.class,null);
                    } else {
                        Toast.makeText(OrderActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }

    /**
     * 解决scrollview嵌套listview问题
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

/**==================================支付宝支付===================================================**/
    public void alipayMsg(){
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo("兔友", "购物车", price);
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"+ getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OrderActivity.this);
                // 调用支付接口
                String result = alipay.pay(payInfo,true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult resultObj = new PayResult((String) msg.obj);
                    String resultStatus = resultObj.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OrderActivity.this, "支付成功",Toast.LENGTH_SHORT).show();
                        payBack();
                        /*Bundle bundle=new Bundle();
                        if("submitOrder".equals(suborder)){
                            bundle.putString("submitOrder", "submitOrder");
                            bundle.putSerializable("subOrderSucceedMsg", subOrderSucceed);
                        }else if("orderPayment".equals(orpayment)){
                            bundle.putString("orderPayment", "orderPayment");
                            bundle.putSerializable("queryOrder", queryOrder);
                        }
                        skipActivityforClass(AllpayActivity.this, CompleteOrderActivity.class, bundle);*/
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderActivity.this, "支付结果确认中",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(OrderActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(OrderActivity.this, "检查结果为：" + msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };


    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://114.215.78.102/index.php/Alipay/notifyUrl" + "\"";

        // 接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
//		orderInfo += "&it_b_pay=\"30m\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//		orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值
        // orderInfo += "&paymethod=\"expressGateway\"";


        return orderInfo;
    }
    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
