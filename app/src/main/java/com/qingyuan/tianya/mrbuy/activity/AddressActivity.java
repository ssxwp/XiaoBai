package com.qingyuan.tianya.mrbuy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.AddressAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.AddressBean;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 地址
 */
public class AddressActivity extends BaseActivity {

    private ListView mListView;
    private ArrayList<AddressBean> mList;
    private String m_id;
    private ImageView rightPic;
    private Handler mHanler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 120:
                    getAddress(0);
                    break;
            }
        }
    };
    private String goods_id;
    private String goods_num;
    private int o_id;
    private AddressAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        m_id = createSharedPreference(AddressActivity.this, "user_custId").getValue("custId");
        HeaderView header = ((HeaderView) findViewById(R.id.commen_address_head));
        rightPic = header.getRightPic();
        rightPic.setImageResource(R.mipmap.add_address);
        mListView = ((ListView) findViewById(R.id.commen_address_listview));
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        goods_id = bundle.getString("goods_id");
        goods_num = bundle.getString("goods_num");
        getAddress(0);
    }

    private void getAddress(int i) {
        if (i==0) {
            initProgressDialog();
        }
        mList.clear();
        mList = new ArrayList<>();
        String urlString = ApiConstant.ADDRESS;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray customer = jObj.getJSONArray("responseList");
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject cus = customer.getJSONObject(i);
                            int d_id = cus.getInt("id");
                            String province = cus.getString("province");//省
                            String city = cus.getString("city");//市
                            String district = cus.getString("district");//区
                            String road = cus.getString("road");//街道
                            String name = cus.getString("name");//姓名
                            String phone = cus.getString("phone");//手机号
                            AddressBean carBean = new AddressBean(d_id, province, city, district, road, name, phone, false);
                            mList.add(carBean);
                        }
                        adapter = new AddressAdapter(AddressActivity.this, mList, mHanler);
                        mListView.setAdapter(adapter);
                    } else {
                        Toast.makeText(AddressActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
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

    @Override
    public void initClick() {
        findViewById(R.id.pay_text).setOnClickListener(this);
        rightPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this,AddAddressActivity.class);
                startActivityForResult(intent,0);
                //skipActivity(AddressActivity.this, AddAddressActivity.class, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0&&resultCode==1){
            getAddress(0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pay_text:
                geto_id();
                setOrder();
                break;
        }
    }

    private void geto_id() {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isChecked()){
                o_id = mList.get(i).getId();
            }
        }
    }

    /**
     * 创建订单
     */
    private void setOrder() {
        initProgressDialog();
        String urlString = ApiConstant.ADDYORDER;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id);
        params.put("goods_id", goods_id);
        params.put("goods_nub", goods_num);
        params.put("d_id",o_id+"");
        HttpUtil.post(urlString, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        Bundle bundle = new Bundle();
                        toast(jObj.getString("message"));
                        bundle.putString("o_id", jObj.getString("message"));
                        skipActivity(AddressActivity.this, OrderActivity.class, bundle);
                        close();
                    } else {
                        toast(jObj.getString("message"));
                        close();
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
    protected void onResume() {
        super.onResume();
        getAddress(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
