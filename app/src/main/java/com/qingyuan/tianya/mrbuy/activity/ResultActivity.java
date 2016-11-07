package com.qingyuan.tianya.mrbuy.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.ResultAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.GoodsBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList<GoodsBean> mList;
    private ArrayList<String> list;
    private ResultAdapter adapter;
    // private String m_id;
    //private ArrayList<String> list1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        mListView = ((ListView) findViewById(R.id.result_list));
    }

    @Override
    public void initData() {
        //m_id = createSharedPreference(this, "user_custId").getValue("custId");
        Bundle bundle = getIntent().getExtras();
        list = bundle.getStringArrayList("list");
        //list1 = bundle.getStringArrayList("list1");
        getGoods();
    }

    private void getGoods() {
        initProgressDialog();
        mList = new ArrayList<>();
        String urlString = ApiConstant.GOODSINFO;
        for (int i = 0; i < list.size(); i++) {
            RequestParams params = new RequestParams();
            params.put("goods_id", list.get(i));
            HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
                public void onSuccess(String response) { // 获取数据成功会调用这里
                    try {
                        JSONObject jObj = new JSONObject(response.trim());
                        if (jObj.getString("flag").equals("success")) {
                                JSONObject cus = jObj.getJSONObject("responseList");
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
                        } else {
                            Toast.makeText(ResultActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        if (mList.size()==list.size()){
                            if (adapter == null) {
                                adapter = new ResultAdapter(ResultActivity.this, mList);
                                mListView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }
                        close();
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
    }

    @Override
    public void initClick() {
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putInt("info",mList.get(position).getGoods_id());
        skipActivity(this, GoodsInfoActivity.class, bundle);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
