package com.qingyuan.tianya.mrbuy.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.CarAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.CarBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CarActivity extends BaseActivity {

    private LinearLayout left_image;
    private ListView mListView;
    private List<CarBean> mList;
    private CheckBox checkAll;
    private CarAdapter adapter = null;
    private String m_id;
    private StringBuilder good_id = new StringBuilder();
    private StringBuilder good_num = new StringBuilder();
    private Handler mHanler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 20:
                    getCarMessage();
                    break;
                case 100:
                    getAllPrice();
                    break;
            }
        }
    };
    private RelativeLayout ac_car_rel;
    private TextView car_all_price;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        m_id = createSharedPreference(CarActivity.this, "user_custId").getValue("custId");
        HeaderView head = ((HeaderView) findViewById(R.id.ac_car_head));
        head.getHeadBackGround().setBackgroundColor(Color.rgb(254,96,0));
        left_image = ((LinearLayout) head.findViewById(R.id.ll_left_layout));
        ac_car_rel = (RelativeLayout)findViewById(R.id.ac_car_rel);
        mListView = ((ListView) findViewById(R.id.ac_car_list));
        checkAll = ((CheckBox) findViewById(R.id.car_check_all));
        car_all_price = ((TextView) findViewById(R.id.car_money_text));
    }

    @Override
    public void initData() {
        getCarMessage();
    }

    private void getCarMessage() {
        initProgressDialog();
        mList = new ArrayList<>();
        String urlString = ApiConstant.CARMESSAGE;
        RequestParams params = new RequestParams();
        params.put("mem_id", m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    Double allPrice = 0.0;
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray customer = jObj.getJSONArray("responseList");
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject cus = customer.getJSONObject(i);
                            int goods_id = cus.getInt("goods_id");
                            String pic = cus.getString("pic");
                            String goods_name = cus.getString("goods_name");
                            String price = cus.getString("price");
                            boolean isChecked = true;
                            int num = 1;
                            CarBean carBean = new CarBean(goods_id, goods_name, price, isChecked, num, pic);
                            mList.add(carBean);
                            allPrice += Double.valueOf(price);
                        }
                        car_all_price.setText("合计：￥" + allPrice);
                        /*if (adapter == null) {*/
                        adapter = new CarAdapter(CarActivity.this, mList, checkAll, m_id, mHanler);
                        mListView.setAdapter(adapter);
                       /* } else {
                            adapter.notifyDataSetChanged();
                        }*/
                        //mListView.setOnItemClickListener(this);
                        ac_car_rel.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(CarActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
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
        left_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarActivity.this.finish();
            }
        });
        checkAll.setOnClickListener(this);
        findViewById(R.id.car_pay_rel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.car_check_all:
                if (checkAll.isChecked()){
                    //checkAll.setChecked(false);
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setIschecked(true);
                    }
                    getAllPrice();
                    adapter.notifyDataSetChanged();
                }else {
                    //checkAll.setChecked(true);
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setIschecked(false);
                    }
                    car_all_price.setText("合计：￥ 0.0");
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.car_pay_rel:
                if (StringUtil.isNotEmpty(getNum())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_id", getGoods_id());
                    bundle.putString("goods_num", getNum());
                    skipActivity(CarActivity.this, AddressActivity.class, bundle);
                }else {
                    toast("请选择要支付的商品");
                }
                //setOrder();
                break;
        }
    }

    /**
     * 拼接商品id
     */
    public String getGoods_id(){
        good_id.replace(0,good_id.length(),"");
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).ischecked()){
                if (StringUtil.isNotEmpty(good_id.toString().trim())) {
                    good_id.append("/").append(mList.get(i).getGoods_id());
                }else {
                    good_id.append(mList.get(i).getGoods_id());
                }
            }
        }
        return good_id.toString();
    }

    /**
     * 拼接商品数量
     */
    public String getNum(){
        good_num.replace(0, good_num.length(), "");
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).ischecked()){
                if (StringUtil.isNotEmpty(good_num.toString().trim())) {
                    good_num.append("/").append(mList.get(i).getNum());
                }else {
                    good_num.append(mList.get(i).getNum());
                }
            }
        }
        return good_num.toString();
    }
    /**
     * 创建订单
     */
    private void setOrder() {
        initProgressDialog();
        String urlString = ApiConstant.ADDYORDER;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id);
        params.put("goods_id", getGoods_id());
        params.put("goods_nub", getNum());
        HttpUtil.post(urlString, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("o_id", jObj.getString("message"));
                        skipActivity(CarActivity.this, OrderActivity.class, bundle);
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
    public  void getAllPrice(){
        Double allPrice = 0.0;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).ischecked()){
                allPrice += Double.valueOf(mList.get(i).getPrice()) * mList.get(i).getNum();
                car_all_price.setText("合计：￥"+allPrice);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
