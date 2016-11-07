package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.utils.QRUtils;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 会员卡
 */
public class VipCarActivity extends BaseActivity{

    private HeaderView header;
    private ImageView qr_show;
    private String m_id;
    private RelativeLayout vip_cart;
    private TextView vip_name;
    private TextView vip_account;
    private SimpleDraweeView vip_head_img;
    private ImageView img_status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_car);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        header = (HeaderView)findViewById(R.id.vip_card_head);
        header.getHeadBackGround().setBackgroundColor(Color.rgb(43, 43, 58));
        qr_show = ((ImageView) findViewById(R.id.qr_img));
        vip_cart = ((RelativeLayout) findViewById(R.id.vip_rel));

        vip_name = ((TextView) findViewById(R.id.vip_name));
        vip_account = ((TextView) findViewById(R.id.vip_account));
        vip_head_img = ((SimpleDraweeView) findViewById(R.id.vip_head_img));
        img_status = (ImageView)findViewById(R.id.vip_status);

    }

    @Override
    public void initData() {
        m_id = createSharedPreference(VipCarActivity.this, "user_custId").getValue("custId");
        getMessage();
        getId();

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
                        String status = object.getString("status");
                        String integral = object.getString("integral");
                        String htime = object.getString("htime");
                        if (StringUtil.isNotEmpty(username)) {
                            vip_name.setText("昵称:"+username);

                        }
                        if (head_pic != null) {
                            Uri uri = Uri.parse(head_pic);
                            vip_head_img.setImageURI(uri);
                        } else {
                            vip_head_img.setImageResource(R.mipmap.default_round_head);
                        }
                        if (status.equals("0")) {
                            img_status.setImageResource(R.mipmap.vip_gray);
                        } else {
                            img_status.setImageResource(R.mipmap.vip_gold);
                            vip_name.setTextColor(Color.RED);
                        }

                        vip_account.setText("账号:" + phone);
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

    private void getId() {
        RequestParams params = new RequestParams();
        params.put("string",m_id);
        HttpUtil.gets("http://114.215.78.102/index.php?s=/Api/Index/encryption", params, new AsyncHttpResponseHandler() {

            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        DisplayMetrics outMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
                        int w = outMetrics.widthPixels * 6 / 11;//设置宽度
                        ViewGroup.LayoutParams layoutParams = qr_show.getLayoutParams();
                        layoutParams.height = layoutParams.width = w;//设置高度
                        qr_show.setLayoutParams(layoutParams);

                        try {
                            Bitmap bitmap = QRUtils.encodeToQRWidth(jObj.getString("message"), w);//要生成二维码的内容，我这就是一个网址
                            qr_show.setImageBitmap(bitmap);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(VipCarActivity.this, "生成二维码失败", Toast.LENGTH_SHORT);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
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

    @Override
    public void initClick() {
        vip_cart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vip_rel:
                skipActivity(this,VipActivity.class,null);
                break;
        }
    }
}
