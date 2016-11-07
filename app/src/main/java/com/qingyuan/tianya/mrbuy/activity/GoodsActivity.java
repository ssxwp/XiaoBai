package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.GoodsAdapters;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.GoodsBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.pulltorefresh.PullToRefreshListView;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoodsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ArrayList<GoodsBean> mList;
    private ListView mReListView;
    private SimpleDraweeView goods_head;
    private RelativeLayout rel;
    private String m_id;
    private HeaderView header;
    private TextView goods_address;
    private TextView goods_price_text1;
    private TextView goods_price_text2;
    private int shop_id;
    private GoodsAdapters adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        Log.e("Main","--------------------------------------------GoodsActivity");
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        header = (HeaderView)findViewById(R.id.goods_headview);
        goods_head = ((SimpleDraweeView) findViewById(R.id.goods_head));
        rel = (RelativeLayout)findViewById(R.id.rel);
        goods_address = (TextView)findViewById(R.id.goods_address);
        goods_price_text1 = (TextView)findViewById(R.id.goods_price_text1);
        goods_price_text2 = (TextView)findViewById(R.id.goods_price_text2);
        PullToRefreshListView mListView = (PullToRefreshListView) findViewById(R.id.goods_list);
        mListView.setPullToRefreshEnabled(true);
        mReListView = mListView.getRefreshableView();
        mReListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        m_id = createSharedPreference(this, "user_custId").getValue("custId");
        mReListView.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        shop_id = bundle.getInt("shop_id");
        getMerchant();
    }

    private void getMerchant() {
        initProgressDialog();
        String urlString = ApiConstant.SHOPMESSAGE;
        RequestParams params = new RequestParams();
        params.put("shop_id", shop_id+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject customer = jObj.getJSONObject("responseList");
                        //int shop_id = customer.getInt("shop_id");
                        String img = customer.getString("head_pic");
                        String name = customer.getString("shop_name");
                        String province = customer.getString("province");
                        String city = customer.getString("city");
                        String district = customer.getString("district");
                        String p_price = customer.getString("p_price");
                        String q_price = customer.getString("q_price");
                        //String status = customer.getString("status");
                        if (StringUtil.isNotEmpty(img)) {
                            Uri uri = Uri.parse(img);
                            goods_head.setImageURI(uri);
                        } else {
                            goods_head.setImageResource(R.mipmap.buffer_pic);
                        }
                        goods_price_text1.setText("起送价：￥" + q_price);
                        goods_price_text2.setText("配送费：￥" + p_price);
                        //mTv_name.setText(name);
                        //mImg_poi.setImageResource(R.mipmap.address_03);
                        goods_address.setText(province + "" + city + "" + district);
                        header.getMidTextView().setText(name);
                        Log.e("Main","----------------------------------------------title="+name);
                        rel.setVisibility(View.VISIBLE);
                        queryGoods();
                        //mImg_collect.setImageResource(R.mipmap.collection_red);
                    } else {
                        Toast.makeText(GoodsActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.i("erro", arg0.toString());
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                //close();
            }
        });
    }

    private void queryGoods() {
        //initProgressDialog();
        mList = new ArrayList<>();
        String urlString = ApiConstant.GOODSMESSAGE;
        RequestParams params = new RequestParams();
        params.put("shop_id", shop_id + "");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray customer = jObj.getJSONArray("responseList");
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject cus = customer.getJSONObject(i);
                            int goods_id = cus.getInt("goods_id");
                            String goods_name = cus.getString("goods_name");
                            String price = cus.getString("price");
                            String p_score = cus.getString("p_score");
                            String s_price = cus.getString("s_price");
                            String score = cus.getString("score");
                            String ytime = cus.getString("ytime");
                            String remark = cus.getString("remark");
                            String pic_1 = cus.getString("pic_1");
                            int num = 0;
                            GoodsBean goodsBean = new GoodsBean(goods_id, goods_name, price, s_price, p_score, score, ytime, remark, pic_1, num);
                            mList.add(goodsBean);
                        }
                        if (adapter == null) {
                            adapter = new GoodsAdapters(GoodsActivity.this, mList, m_id);
                            mReListView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        rel.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(GoodsActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putInt("info",mList.get(position).getGoods_id());
        skipActivity(this, GoodsInfoActivity.class,bundle);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
