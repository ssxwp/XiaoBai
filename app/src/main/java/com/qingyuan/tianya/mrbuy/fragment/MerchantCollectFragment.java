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
import com.qingyuan.tianya.mrbuy.activity.MerchantMessageActivity;
import com.qingyuan.tianya.mrbuy.adapter.MerCollectAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.MerCollectBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 商家收藏列表
 */
public class MerchantCollectFragment extends BaseFragment implements AdapterView.OnItemClickListener {


    private View view;
    private String m_id;
    private ListView mListView;
    private ArrayList<MerCollectBean> mList;
    private MerCollectAdapter adapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 101:
                    getCollection();
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_merchant_collect, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initonClick();
    }

    @Override
    public void initView() {
        mListView = (ListView)view.findViewById(R.id.list);
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
        String urlString = ApiConstant.QUECOLLECTION;
        RequestParams params = new RequestParams();
        params.put("mem_id", m_id+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.toString().trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray cus = jObj.getJSONArray("responseList");
                        for (int i = 0; i < cus.length(); i++) {
                            JSONObject customer = cus.getJSONObject(i);
                            int shop_id = customer.getInt("shop_id");
                            String img = customer.getString("head_pic");
                            String name = customer.getString("shopname");
                            MerCollectBean bean = new MerCollectBean(shop_id, name, img);
                            mList.add(bean);
                        }
                        adapter = new MerCollectAdapter(getActivity(), mList, m_id, mHandler);
                        mListView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MerCollectBean bean = mList.get(position);
        int shop_id = bean.getShop_id();
        Bundle bundle = new Bundle();
        bundle.putString("sort","dish");
        bundle.putInt("shop_id",shop_id);
        skipActivityforClass(getActivity(), MerchantMessageActivity.class, bundle);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
