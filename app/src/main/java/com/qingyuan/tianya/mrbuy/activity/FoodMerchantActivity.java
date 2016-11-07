package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.DishMerchantAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.DishMessageBean;
import com.qingyuan.tianya.mrbuy.utils.AMapUtil;
import com.qingyuan.tianya.mrbuy.view.pulltorefresh.PullToRefreshListView;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 商家列表
 */
public class FoodMerchantActivity extends BaseActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private PullToRefreshListView mListView;
    private ArrayList<DishMessageBean> mList = new ArrayList<>();
    private int shop_id;
    private int page = 1;
    private ListView mReListView;
    private boolean isRefresh = true;
    private TextView midTextView;
    private TextView tv_poi;
    private TextView tv_num;
    private TextView tv_price;
    private int flag = 2;
    private int lastItem;
    DishMerchantAdapter adapter = null;
    private AMapUtil aMap;
    private double longitude;
    private double latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_merchant);
        initView();
        initClick();
        initData();
    }

    @Override
    public void initView() {
        HeaderView head = (HeaderView) findViewById(R.id.header_view);
        midTextView = head.getMidTextView();
        mListView = ((PullToRefreshListView)findViewById(R.id.list));
        mListView.setPullToRefreshEnabled(true);
        mReListView = mListView.getRefreshableView();
        mReListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        Bundle bundle = getIntent().getExtras();
        shop_id = bundle.getInt("merchant");
        mReListView.setOnScrollListener(FoodMerchantActivity.this);
        mReListView.setOnItemClickListener(FoodMerchantActivity.this);

        tv_poi = ((TextView) findViewById(R.id.sort_poi));
        tv_num = ((TextView) findViewById(R.id.sort_num));
        tv_price = ((TextView) findViewById(R.id.sort_price));
    }

    @Override
    public void initData() {
        getMessage(shop_id, page,2);
        aMap = new AMapUtil(this);
        aMap.setOption();
        //aMap.requestLocation();
        aMap.getData(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation!=null){
                    if (aMapLocation.getErrorCode() == 0) {
                        latitude = aMapLocation.getLatitude();//获取纬度
                        longitude = aMapLocation.getLongitude();//获取经度
                        //toast("纬度" + latitude + "经度"+longitude);
                        aMapLocation.getAccuracy();//获取精度信息
                        getMessage(shop_id, page,2);
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
        if (shop_id ==2 ){
            midTextView.setText("火锅");
        }else if (shop_id==7){
            midTextView.setText("自助餐");
        }else if (shop_id==10){
            midTextView.setText("小吃");
        }else if (shop_id==11){
            midTextView.setText("烧烤");
        }else if (shop_id==12){
            midTextView.setText("甜点");
        }else if (shop_id==13){
            midTextView.setText("外卖");
        }else if (shop_id==14){
            midTextView.setText("水果");
        }else if (shop_id==15){
            midTextView.setText("蔬菜");
        }
    }

    @Override
    public void initClick() {
        tv_poi.setOnClickListener(this);
        tv_num.setOnClickListener(this);
        tv_price.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sort_poi:
                tv_num.setTextColor(Color.rgb(105,105,105));
                tv_price.setTextColor(Color.rgb(105,105,105));
                tv_poi.setTextColor(Color.rgb(5,150,240));
                isRefresh = true;
                page =1;
                mList = new ArrayList<>();
                getMessage(shop_id, page,1);
                flag = 1;
                break;
            case R.id.sort_num:
                tv_poi.setTextColor(Color.rgb(105,105,105));
                tv_price.setTextColor(Color.rgb(105,105,105));
                tv_num.setTextColor(Color.rgb(5,150,240));
                mList = new ArrayList<>();
                isRefresh = true;
                page =1;
                getMessage(shop_id, page,2);
                flag = 2;
                break;
            case R.id.sort_price:
                tv_num.setTextColor(Color.rgb(105,105,105));
                tv_poi.setTextColor(Color.rgb(105,105,105));
                tv_price.setTextColor(Color.rgb(5,150,240));
                mList = new ArrayList<>();
                isRefresh = true;
                page =1;
                getMessage(shop_id, page,3);
                flag = 3;
                break;
        }
    }

    public void getMessage(int shop_id,int page,int sort){
        if (isRefresh) {
            initProgressDialog();
        }
        final String urlString = ApiConstant.DISHMERCHANT;
        final RequestParams params = new RequestParams();
        params.put("cate_id", shop_id + "");
        params.put("p", page + "");
        params.put("type",sort+"");
        //Log.i("ssssss",longitude+"维度"+latitude);
        params.put("latitude",latitude+"");
        params.put("longitude",longitude+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                Log.i("++++++++++++++++++++++++++++++++++++++++++++++++",response.toString());
                Log.i("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",urlString.toString()+params.toString());
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
                            DishMessageBean dishBean = new DishMessageBean(custId, name, img, province, city, district,p_price,q_price,status,volume,price,juli);
                            mList.add(dishBean);
                        }
                        /*if (isRefresh == true) {
                            mList.clear();
                        }*/
                        if (adapter == null) {
                            adapter = new DishMerchantAdapter(FoodMerchantActivity.this, mList);
                            mReListView.setAdapter(adapter);
                            //mReListView.setSelected(true);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        /*if (isRefresh == true) {
                            adapter.notifyDataSetChanged();
                        }*/
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DishMessageBean bean = mList.get(position);
        int shop_id = bean.getShop_id();
        Bundle bundle = new Bundle();
        bundle.putString("sort","dish");
        bundle.putInt("shop_id",shop_id);
        skipActivity(this, MerchantMessageActivity.class, bundle);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (lastItem ==adapter.getCount()&&scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            page++;
            isRefresh = false;
            if (flag == 1) {
                getMessage(shop_id, page, 1);
            }else if(flag == 2){
                getMessage(shop_id, page, 2);
            }else {
                getMessage(shop_id, page, 3);
            }
        }
        switch (scrollState){
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                Fresco.getImagePipeline().resume();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                Fresco.getImagePipeline().pause();
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItem = firstVisibleItem+visibleItemCount;
        /*if (firstVisibleItem + visibleItemCount == totalItemCount*//*&&isRefresh*//*) {//判断是不是最后一个
            page++;
            isRefresh = false;
            if (flag == 1) {
                getMessage(shop_id, page, 1);
            }else if(flag == 2){
                getMessage(shop_id, page, 2);
            }else {
                getMessage(shop_id, page, 3);
            }
        }*/

    }
    @Override
    protected void onPause() {
        super.onPause();
        aMap.stop();
        if (aMap.mLocationClient == null) return ;
        if (aMap.mLocationClient.isStarted()){
            aMap.mLocationClient.stopLocation();
        }
    }

    @Override
    public void onDestroy() {
        if (aMap.mLocationClient != null && aMap.mLocationClient.isStarted()) {
            aMap.stop();
            aMap.mLocationClient = null;
        }
        if (mList!=null){
            mList.clear();
        }
        super.onDestroy();
    }
}
