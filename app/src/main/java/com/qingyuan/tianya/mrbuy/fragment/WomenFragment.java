package com.qingyuan.tianya.mrbuy.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.activity.FunActivity;
import com.qingyuan.tianya.mrbuy.activity.MerchantMessageActivity;
import com.qingyuan.tianya.mrbuy.adapter.WomenMerchantAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.application.MrBuyApplication;
import com.qingyuan.tianya.mrbuy.bean.DishMessageBean;
import com.qingyuan.tianya.mrbuy.utils.AMapUtil;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 丽人
 */
public class WomenFragment extends BaseFragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private PullToRefreshListView mListView;
    private ArrayList<DishMessageBean> mList = new ArrayList<>();;
    private int page = 1;
    private ListView mReListView;
    private boolean isRefresh = true;
    private View view;
    private SimpleDraweeView fr_women_head;
    private LinearLayout fr_women_lin3;
    private AMapUtil aMap;
    private double latitude;
    private double longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_women, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
       /* HeaderView headerView = (HeaderView) getActivity().findViewById(R.id.header_view);
        headerView.getMidTextView().setVisibility(View.VISIBLE);
        headerView.getMidTextView().setText("丽人");
        headerView.getHeadBackGround().setBackgroundColor(Color.rgb(236, 17, 97));
        getActivity().findViewById(R.id.home_head_search).setVisibility(View.GONE);*/
        fr_women_lin3 = ((LinearLayout) view.findViewById(R.id.fr_women_lin3));
        mListView = ((PullToRefreshListView)view.findViewById(R.id.women_list));
        fr_women_head = ((SimpleDraweeView) view.findViewById(R.id.fr_women_head));
        mListView.setPullToRefreshEnabled(true);
        mReListView = mListView.getRefreshableView();
        mReListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        getHeadImage();
        mReListView.setOnItemClickListener(this);
    }
    private void getHeadImage(){
        //initProgressDialog();
        String urlString = ApiConstant.ADVER;
        RequestParams params = new RequestParams();
        params.put("a_id", 39 + "");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject cus = jObj.getJSONObject("responseList");
                        String img = cus.getString("m_pic");
                        if (StringUtil.isNotEmpty(img)) {
                            Uri uri = Uri.parse(img);
                            fr_women_head.setImageURI(uri);
                        } else {
                            fr_women_head.setImageResource(R.mipmap.buffer_pic);
                        }
                    } /*else {
                        Toast.makeText(FoodMerchantActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }*/
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
    private void getMessage(int shop_id, int page) {
        if (isRefresh) {
            initProgressDialog();
        }
        String urlString = ApiConstant.DISHMERCHANT;
        RequestParams params = new RequestParams();
        params.put("cate_id", shop_id + "");
        params.put("p", page + "");
        params.put("latitude",latitude+"");
        params.put("longitude",longitude+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray customer = jObj.getJSONArray("responseList");
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject cus = customer.getJSONObject(i);
                            int custId = cus.getInt("shop_id");
                            String img = cus.getString("head_pic");
                            String name = cus.getString("shop_name");
                            String province = cus.getString("province");
                            String city = cus.getString("city");
                            String district = cus.getString("district");
                            String p_price = cus.getString("p_price");
                            String q_price = cus.getString("q_price");
                            String status = cus.getString("status");
                            String volume = cus.getString("volume");
                            String price = cus.getString("price");
                            String juli = cus.getString("juli");
                            DishMessageBean dishBean = new DishMessageBean(custId, name, img, province, city, district, p_price, q_price, status, volume, price,juli);
                            mList.add(dishBean);
                        }
                        /*if (isRefresh == true) {
                            mList.clear();
                        }*/
                        WomenMerchantAdapter adapter = null;
                        if (adapter == null) {
                            adapter = new WomenMerchantAdapter(MrBuyApplication.getAppContext(), mList);
                            mReListView.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(mReListView);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        /*if (isRefresh == true) {
                            adapter.notifyDataSetChanged();
                        }*/
                        fr_women_lin3.setVisibility(View.VISIBLE);
                        mReListView.setOnScrollListener(WomenFragment.this);
                        mListView.onRefreshComplete();
                        mListView.scrollTo(0, 50);
                        isRefresh = true;
                        close();
                    } /*else {
                        Toast.makeText(FoodMerchantActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }*/
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
    public void initonClick() {
        view.findViewById(R.id.hair_rel).setOnClickListener(this);
        view.findViewById(R.id.face_rel).setOnClickListener(this);
        view.findViewById(R.id.finger_rel).setOnClickListener(this);
    }

    @Override
    public void initData() {
        aMap = new AMapUtil(getActivity());
        aMap.setOption();
        //aMap.requestLocation();
        aMap.getData(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation!=null){
                    if (aMapLocation.getErrorCode() == 0) {
                        latitude = aMapLocation.getLatitude();//获取纬度
                        longitude = aMapLocation.getLongitude();//获取经度
                        getMessage(30, page);
                        aMap.stop();
                    }else{
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }

        });
        aMap.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hair_rel:
                Bundle bundle = new Bundle();
                bundle.putInt("merchant", 33);
                skipActivityforClass(getActivity(), FunActivity.class,bundle);
                break;
            case R.id.face_rel:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("merchant", 32);
                skipActivityforClass(getActivity(), FunActivity.class,bundle1);
                break;
            case R.id.finger_rel:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("merchant", 34);
                skipActivityforClass(getActivity(), FunActivity.class,bundle2);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DishMessageBean bean = mList.get(position);
        int shop_id = bean.getShop_id();
        Bundle bundle = new Bundle();
        bundle.putString("sort","fun");
        bundle.putInt("shop_id",shop_id);
        skipActivityforClass(getActivity(), MerchantMessageActivity.class, bundle);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
