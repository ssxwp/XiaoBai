package com.qingyuan.tianya.mrbuy.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.activity.OrderActivity;
import com.qingyuan.tianya.mrbuy.adapter.OrderInfoAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.OrderInfoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 已取消订单
 */
public class CancelOrderFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private View view;
    private String m_id;
    private ListView mListView;
    private ArrayList<OrderInfoBean> mList;
    private OrderInfoAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cancel_order, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        mListView = ((ListView) view.findViewById(R.id.order_cancel_list));
    }

    @Override
    public void initonClick() {
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        m_id = createSharedPreference(getActivity(), "user_custId").getValue("custId");
        getOrder();
    }

    private void getOrder() {
        initProgressDialog();
        mList = new ArrayList<>();
        String urlString = ApiConstant.ORDERLIST;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id+"");
        params.put("type",3+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray cus = jObj.getJSONArray("responseList");
                        for (int i = 0; i < cus.length(); i++) {
                            JSONObject customer = cus.getJSONObject(i);
                            String order_id = customer.getString("order_id");
                            String order_sn = customer.getString("order_sn");
                            String old_price = customer.getString("old_price");
                            String price = customer.getString("price");
                            String ctime = customer.getString("ctime");
                            String status = customer.getString("status");
                            String type = customer.getString("type");
                            OrderInfoBean bean = new OrderInfoBean(order_id, order_sn, old_price, price, ctime, status, type);
                            mList.add(bean);
                        }
                        adapter = new OrderInfoAdapter(getActivity(), mList);
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
        OrderInfoBean bean = mList.get(position);
        String order_id = bean.getOrder_id();
        Bundle bundle = new Bundle();
        bundle.putString("o_id",order_id);
        skipActivityforClass(getActivity(), OrderActivity.class,null);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
