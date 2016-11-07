package com.qingyuan.tianya.mrbuy.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.activity.GoodsInfoActivity;
import com.qingyuan.tianya.mrbuy.adapter.GoodsCollectAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.GoodsCollectBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 商品收藏列表
 */
public class GoodsCollectionFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private View view;
    private String m_id;
    private ListView mListView;
    private ArrayList<GoodsCollectBean> mList;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 50:
                    getCollection();
                    break;
            }
        }
    };
    private GoodsCollectAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods_collection, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        mListView = (ListView)view.findViewById(R.id.list_goods);
    }

    @Override
    public void initonClick() {
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        m_id = createSharedPreference(getActivity(), "user_custId").getValue("custId");
        getCollection();
    }

    private void getCollection() {
        initProgressDialog();
        mList = new ArrayList<>();
        String urlString = ApiConstant.QUECOLLECT;
        RequestParams params = new RequestParams();
        params.put("mem_id", m_id+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray cus = jObj.getJSONArray("responseList");
                        for (int i = 0; i < cus.length(); i++) {
                            JSONObject customer = cus.getJSONObject(i);
                            int shop_id = customer.getInt("goods_id");
                            String img = customer.getString("pic");
                            String name = customer.getString("goods_name");
                            String price = customer.getString("price");
                            GoodsCollectBean bean = new GoodsCollectBean(shop_id, img, name, price);
                            mList.add(bean);
                        }
                        adapter = new GoodsCollectAdapter(getActivity(), mList, m_id, mHandler);
                        mListView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    close();
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putInt("info",mList.get(position).getGoods_id());
        skipActivityforClass(getActivity(), GoodsInfoActivity.class,bundle);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
