package com.qingyuan.tianya.mrbuy.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.VipPriceAdapter;
import com.qingyuan.tianya.mrbuy.alipay.PayResult;
import com.qingyuan.tianya.mrbuy.alipay.SignUtils;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.VipPriceBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

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
 * 我的会员
 */
public class VipActivity extends BaseActivity{


    private ImageView one,three,six,year;
    private TextView open;
    private int mon = 0;
    private int price;

    public static final String PARTNER = "2088802330143213";
    public static final String SELLER = "15803125513";
    public static final String RSA_PRIVATE = "MIICXwIBAAKBgQDMQ0ISqpTkTLdtm6+ukBYuRvcDJD8pyIozEVxzOzZe+qHmIRKj7TsCvpWyIII7C4V0GQIV5c/cVf9FN7uUduKktFslDvV2xL1e6Hl7IrfE9PuwI34ImgRaq2URLi1gIApSOxiKTS4y6ITcCmE1tJQR1bc06m0RqHjc3e5mLWRSUQIDAQABAoGBAMk6HTZsbB/439+QN2OeOOvAJ4olxD/yewtSOCShGbA58xLWVBnAFJlgjfKcig+HCyLTnDVcYAjtg3x6KkZZVG4wXBVR2RilK4RyC3ZIVnUtSsHxInaPp86secyJx6yM9jSLa8bp8vdM2YlrOnQmBWotI8WR4vxIaZanL0QcySYBAkEA9HYXNjz1SDpoDZS0gMzSEboZqYlD7fZbGtsdp/4sdWyQiXz2u8M0tJz/QhcRsEKpDn6Nqzo9Vnd2PY4UlUY84QJBANXnbzbc51VX7q9HF/1WrdUKoFiTfkFL3WBQLvJ1hJePk0mZ8wyYSBXm/TG290K8FAF3E8wd34AZugSOKIX203ECQQDMpVszJX60k2huDhG5TT2CZ4d6HJYITFbiOZIvAyGWv2aUIk+uyXCCzncFjahaJMO/hYP1VpDopKMQTrO+bwkhAkEAtK2JTkEnkfn1Xc5iYBYgPQx9xnPHOGZZdqokDZ7gQZj/4zKLjv7uHLoA8kZG8vKkv1wSt94f1Ddm3cQqTIXhsQJBAJTddmcbvMPuGxAFVpPdZRQ422Q2n28sZUBa69XRQ2Y5jgv5LYRAh/2bnVnFcuIV3WalZG0Ny5qts6VeGclhpWA=";
    //public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMQ0ISqpTkTLdtm6+ukBYuRvcDJD8pyIozEVxzOzZe+qHmIRKj7TsCvpWyIII7C4V0GQIV5c/cVf9FN7uUduKktFslDvV2xL1e6Hl7IrfE9PuwI34ImgRaq2URLi1gIApSOxiKTS4y6ITcCmE1tJQR1bc06m0RqHjc3e5mLWRSUQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    private String m_id;
    private TextView vip_name;
    private SimpleDraweeView vip_head_img;
    private AlertDialog dialog;
    private String o_id;
    private TextView pay_purse;
    private TextView vip_account;
    private RelativeLayout order;
    private TextView vip_lost;
    private ImageView img_status;
    private TextView tv_integral;
    private TextView tv_htime;
    private String status;
    private AlertDialog dialogs;
    private AlertDialog dialog2;
    private EditText ed;
    private GridView vip_grid;
    private ArrayList<VipPriceBean> mList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        HeaderView header = (HeaderView) findViewById(R.id.ac_vip_back_img);
        header.getHeadBackGround().setBackgroundColor(Color.rgb(38,42,59));
        /*one = (ImageView)findViewById(R.id.text_one);
        three = (ImageView)findViewById(R.id.text_two);
        six = (ImageView)findViewById(R.id.text_six);
        year = (ImageView)findViewById(R.id.text_ye);*/
        open = ((TextView) findViewById(R.id.open));
        pay_purse = (TextView)findViewById(R.id.pay_purse);
        vip_name = ((TextView) findViewById(R.id.vip_name));
        vip_account = ((TextView) findViewById(R.id.vip_account));
        vip_lost = ((TextView) findViewById(R.id.vip_lost));
        vip_head_img = ((SimpleDraweeView) findViewById(R.id.vip_head_img));
        order = (RelativeLayout)findViewById(R.id.order_rel);
        img_status = (ImageView)findViewById(R.id.vip_status);
        tv_integral = (TextView)findViewById(R.id.vip_integral);
        tv_htime = (TextView)findViewById(R.id.vip_time);
        vip_grid = (GridView)findViewById(R.id.vip_grid);
    }

    @Override
    public void initData() {
        m_id = createSharedPreference(VipActivity.this, "user_custId").getValue("custId");
        getPrice();
        getMessage();
    }

    private void getPrice() {
        String urlString = ApiConstant.VIPPRICE;
        RequestParams params = new RequestParams();
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    Log.i("TAG", "昵称信息：" + jObj.toString());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray array = jObj.getJSONArray("responseList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String time = object.getString("time");
                            String price = object.getString("price");
                            VipPriceBean bean = new VipPriceBean(time,price,false);
                            mList.add(bean);
                        }
                        VipPriceAdapter adapter = new VipPriceAdapter(VipActivity.this,mList);
                        vip_grid.setAdapter(adapter);

                    } else {
                        //toast(jObj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.e("hck", " onFailure" + arg0.toString());
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }

        });
    }

    private void getMessage() {
        String urlString = ApiConstant.QUERYMESSAGE;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject object = jObj.getJSONObject("responseList");
                        String username = object.getString("username");
                        String head_pic = object.getString("head_pic");
                        String phone = object.getString("account");
                        String black = object.getString("black");
                        status = object.getString("status");
                        String integral = object.getString("integral");
                        String htime = object.getString("htime");
                        if (StringUtil.isNotEmpty(username)) {
                            vip_name.setText("昵称："+username);
                        }
                        if (head_pic != null) {
                            Uri uri = Uri.parse(head_pic);
                            vip_head_img.setImageURI(uri);
                        } else {
                            vip_head_img.setImageResource(R.mipmap.default_round_head);
                        }
                        if (status.equals("0")) {
                            img_status.setImageResource(R.mipmap.vip_gray);
                            tv_integral.setText("积分：0");
                            tv_htime.setText("");
                        } else {
                            img_status.setImageResource(R.mipmap.vip_gold);
                            tv_integral.setText("积分：" + integral);
                            tv_htime.setText("到期时间：" + getStrTime(htime).substring(0, 11));
                        }

                        vip_account.setText("账号："+phone );
                        vip_lost.setText("￥" + black);
                    } else {
                        //toast(jObj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.e("hck", " onFailure" + arg0.toString());
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessage();
    }

    @Override
    public void initClick() {
        /*one.setOnClickListener(this);
        three.setOnClickListener(this);
        six.setOnClickListener(this);
        year.setOnClickListener(this);*/
        open.setOnClickListener(this);
        pay_purse.setOnClickListener(this);
        order.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
           /* case R.id.text_one:
                one.setImageResource(R.mipmap.one_moth_red);
                three.setImageResource(R.mipmap.three_months);
                six.setImageResource(R.mipmap.six_months);
                year.setImageResource(R.mipmap.one_year);
                    *//*one.setChecked(true);
                    three.setChecked(false);
                    six.setChecked(false);
                    year.setChecked(false);*//*
                    mon = 1;
                    price=10;
                open.setBackgroundColor(Color.rgb(234,98,0));
                break;
            case R.id.text_two:
                one.setImageResource(R.mipmap.onemonth);
                three.setImageResource(R.mipmap.three_moths_red);
                six.setImageResource(R.mipmap.six_months);
                year.setImageResource(R.mipmap.one_year);
                   *//* three.setChecked(true);
                    one.setChecked(false);
                    six.setChecked(false);
                    year.setChecked(false);*//*
                    mon = 3;
                    price=30;
                open.setBackgroundColor(Color.rgb(234,98,0));
                break;
            case R.id.text_six:
                one.setImageResource(R.mipmap.onemonth);
                three.setImageResource(R.mipmap.three_months);
                six.setImageResource(R.mipmap.six_moths_red);
                year.setImageResource(R.mipmap.one_year);
                    *//*six.setChecked(true);
                    three.setChecked(false);
                    one.setChecked(false);
                    year.setChecked(false);*//*
                    mon = 6;
                    price=60;
                open.setBackgroundColor(Color.rgb(234,98,0));
                break;
            case R.id.text_ye:
                one.setImageResource(R.mipmap.onemonth);
                three.setImageResource(R.mipmap.three_months);
                six.setImageResource(R.mipmap.six_months);
                year.setImageResource(R.mipmap.one_year_red);
                    *//*year.setChecked(true);
                    three.setChecked(false);
                    six.setChecked(false);
                    one.setChecked(false);*//*
                    mon = 12;
                    price=120;
                open.setBackgroundColor(Color.rgb(234,98,0));
                break;*/
            case R.id.pay_purse:
                inputTitleDialog();
                break;
            case R.id.open:
                addVip();
                break;
            case R.id.order_rel:
                skipActivity(this,OrderInfoActivity.class,null);
                break;
        }
    }
    private void inputTitleDialog() {

        /*final EditText inputServer = new EditText(this);
        inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputServer.setFocusable(true);
        //inputServer.setBackground(R.drawable.searchview);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入充值金额").setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        if (StringUtil.isNotEmpty(inputName)) {
                            payPurse(inputName);
                        } else {

                        }
                    }
                });
        builder.show();*/
        dialog2 = new AlertDialog.Builder(VipActivity.this).create();
        dialog2.setView(new EditText(VipActivity.this));
        dialog2.show();
        dialog2.getWindow().setContentView(R.layout.dialog_money);
        ed = (EditText)dialog2.getWindow().findViewById(R.id.ed);
        ed.setFocusable(true);
        dialog2.getWindow().findViewById(R.id.out_diss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.getWindow().findViewById(R.id.out_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isNotEmpty(ed.getText().toString().trim())) {
                    payPurse(ed.getText().toString().trim());
                    dialog2.dismiss();
                } else {
                    toast("请输入充值金额");
                }
            }
        });
    }

    private void payPurse(final String num) {
        String urlString = ApiConstant.PURSEMONEY;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id);
        params.put("price", num);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {

            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        o_id = jObj.getString("message");
                        alipayMsg("钱包充值", num);

                    } else {
                        Toast.makeText(VipActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void addVip() {
        String urlString = ApiConstant.ADDVIP;
        RequestParams params = new RequestParams();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isChecked()){
                mon = Integer.parseInt(mList.get(i).getMonth());
                price = Integer.parseInt(mList.get(i).getMoney());
            }
        }
        if (mon==0){
            toast("请选择开通时长");
            return;
        }
        initProgressDialog();
        params.put("m_id", m_id);
        params.put("time",mon+"");
        params.put("price",price+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {

            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        o_id = jObj.getString("message");
                        dialog = new AlertDialog.Builder(VipActivity.this).create();
                        dialog.show();
                        dialog.getWindow().setContentView(R.layout.paystyle);
                        dialog.getWindow().findViewById(R.id.dialog_out_diss).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pursePay();
                                dialog.dismiss();
                            }
                        });
                        dialog.getWindow().findViewById(R.id.dialog_out_ok).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alipayMsg("兔友vip" + mon + "月", price + "");
                                dialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(VipActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void pursePay() {
        dialogs = new AlertDialog.Builder(VipActivity.this).create();
        dialogs.show();
        dialogs.getWindow().setContentView(R.layout.dialog_pay);
        dialogs.getWindow().findViewById(R.id.dialog_diss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
                toast("取消支付");
            }
        });
        dialogs.getWindow().findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
                initProgressDialog();
                String urlString = ApiConstant.PURSEPAY;
                RequestParams params = new RequestParams();
                params.put("order_id", o_id);
                HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
                    public void onSuccess(String response) { // 获取数据成功会调用这里
                        try {
                            JSONObject jObj = new JSONObject(response.trim());
                            if (jObj.getString("flag").equals("success")) {
                                if (status.equals("0")) {
                                    toast("会员开通成功");
                                } else {
                                    toast("续费会员成功");
                                }
                            } else {
                                Toast.makeText(VipActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        });

    }

    private void payBack() {
        String urlString = ApiConstant.PAYBACK;
        RequestParams params = new RequestParams();
        params.put("order_id",o_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        toast(jObj.getString("message"));
                        getMessage();
                    } else {
                        Toast.makeText(VipActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
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
    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**==================================支付宝支付===================================================**/
    public void alipayMsg(String message,String price){
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo(message, message, price);
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
                PayTask alipay = new PayTask(VipActivity.this);
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
                        Toast.makeText(VipActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        payBack();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(VipActivity.this, "支付结果确认中",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(VipActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(VipActivity.this, "检查结果为：" + msg.obj,Toast.LENGTH_SHORT).show();
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
}
