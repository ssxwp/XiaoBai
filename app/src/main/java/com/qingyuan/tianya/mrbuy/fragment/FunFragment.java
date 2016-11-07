package com.qingyuan.tianya.mrbuy.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.activity.FunActivity;
import com.qingyuan.tianya.mrbuy.activity.MerchantMessageActivity;
import com.qingyuan.tianya.mrbuy.adapter.FunMerchantAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.DishMessageBean;
import com.qingyuan.tianya.mrbuy.utils.AMapUtil;
import com.qingyuan.tianya.mrbuy.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 休闲娱乐
 */
public class FunFragment extends BaseFragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private ViewPager adViewPager;
    private Thread thread;
    private AdPageAdapter adapter;
    private boolean isRunning = true;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private ImageView[] imageViews ;
    private List<SimpleDraweeView> pageViews = null;

    private View view;

    private PullToRefreshListView mListView;
    private ArrayList<DishMessageBean> mList = new ArrayList<>();;
    private int shop_id = 31;
    private int page = 1;
    private ListView mReListView;
    private boolean isRefresh = true;
    private LinearLayout fr_fun_lin2;
    private LinearLayout fr_fun_lin1;
    private ScrollView scroll;
    private AMapUtil aMap;
    private double latitude;
    private double longitude;
    private FunMerchantAdapter adapter1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fun, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }


    @Override
    public void initView() {
        scroll = ((ScrollView) view.findViewById(R.id.scroll));
        pageViews = new ArrayList<>();
        // 创建ViewPager
        adViewPager = new ViewPager(getActivity());
        mListView = ((PullToRefreshListView)view.findViewById(R.id.list_fun_merchant));
        mListView.setPullToRefreshEnabled(true);
        mReListView = mListView.getRefreshableView();
        mReListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        fr_fun_lin2 = ((LinearLayout) view.findViewById(R.id.fr_fun_lin2));
        fr_fun_lin1 = ((LinearLayout) view.findViewById(R.id.fr_fun_lin1));
        mReListView.setOnItemClickListener(FunFragment.this);
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
                            String p_price = "";
                            String q_price = cus.getString("q_price");
                            String status = cus.getString("status");
                            String volume = cus.getString("volume");
                            String price = "";
                            String juli = cus.getString("juli");
                            DishMessageBean dishBean = new DishMessageBean(custId, name, img, province, city, district,p_price,q_price,status,volume,price,juli);
                            mList.add(dishBean);
                        }
                        /*if (isRefresh == true) {
                            mList.clear();
                        }*/
                            adapter1 = new FunMerchantAdapter(getActivity(), mList);
                            mReListView.setAdapter(adapter1);
                            setListViewHeightBasedOnChildren(mReListView);
                        /*if (isRefresh == true) {
                            adapter.notifyDataSetChanged();
                        }*/
                        fr_fun_lin2.setVisibility(View.VISIBLE);
                        fr_fun_lin1.setVisibility(View.VISIBLE);
                        mReListView.setOnScrollListener(FunFragment.this);
                        mListView.onRefreshComplete();
                        mListView.scrollTo(0, 50);
                        isRefresh = true;
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

    @Override
    public void initonClick() {
        view.findViewById(R.id.fr_fun_ktv_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_fun_bath_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_fun_foot_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_fun_bar_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_fun_billiards_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_fun_farm_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_fun_coffee_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_fun_more_rel).setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY <= 0) {
                        getActivity().findViewById(R.id.header_view).setBackgroundColor(Color.argb(0, 0, 139, 255));
                    } else if (scrollY > 0 && scrollY <= adViewPager.getHeight()) {
                        float scale = (float) scrollY / adViewPager.getHeight();
                        float alpha = (255 * scale);
                        // 只是layout背景透明(仿知乎滑动效果)
                        getActivity().findViewById(R.id.header_view).setBackgroundColor(Color.argb((int) alpha, 0, 139, 255));
                    } else {
                        getActivity().findViewById(R.id.header_view).setBackgroundColor(Color.argb( 255, 0, 139, 255));
                    }
                }
            });
        }
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
                        //toast("纬度" + latitude + "经度"+longitude);
                        aMapLocation.getAccuracy();//获取精度信息
                        getMessage(shop_id, page);
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
        getQueryBanner();
        initViewPager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fr_fun_ktv_rel:
                Bundle bundle = new Bundle();
                bundle.putInt("merchant",17);
                skipActivityforClass(getActivity(), FunActivity.class,bundle);
                break;
            case R.id.fr_fun_bath_rel:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("merchant",18);
                skipActivityforClass(getActivity(), FunActivity.class,bundle1);
                break;
            case R.id.fr_fun_foot_rel:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("merchant",19);
                skipActivityforClass(getActivity(), FunActivity.class,bundle2);
                break;
            case R.id.fr_fun_bar_rel:
                Bundle bundle3 = new Bundle();
                bundle3.putInt("merchant",20);
                skipActivityforClass(getActivity(), FunActivity.class,bundle3);
                break;
            case R.id.fr_fun_billiards_rel:
                Bundle bundle4 = new Bundle();
                bundle4.putInt("merchant",21);
                skipActivityforClass(getActivity(), FunActivity.class,bundle4);
                break;
            case R.id.fr_fun_farm_rel:
                Bundle bundle5 = new Bundle();
                bundle5.putInt("merchant",22);
                skipActivityforClass(getActivity(), FunActivity.class,bundle5);
                break;
            case R.id.fr_fun_coffee_rel:
                Bundle bundle6 = new Bundle();
                bundle6.putInt("merchant",23);
                skipActivityforClass(getActivity(), FunActivity.class,bundle6);
                break;
            case R.id.fr_fun_more_rel:

                break;
        }
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

    private void initViewPager() {

        // 从布局文件中获取ViewPager父容器

        LinearLayout pagerLayout = (LinearLayout) view.findViewById(R.id.pro_view_pager_content);
        // 获取屏幕像素相关信息
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 根据屏幕信息设置ViewPager广告容器的宽高
        adViewPager.setLayoutParams(new ViewGroup.LayoutParams(dm.widthPixels,dm.widthPixels * 1 / 2));
        pagerLayout.removeAllViews();
        pagerLayout.addView(adViewPager);
        adViewPager.setAdapter(adapter);

        adViewPager.setOnPageChangeListener(new AdPageChangeListener());


        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning) {
                        viewHandler.sendEmptyMessage(atomicInteger.get());
                        atomicOption();
                    }
                }
            });

            thread.start();

        } else {
            isRunning = true;
        }

    }

    private void atomicOption() {
        atomicInteger.incrementAndGet();
        if (imageViews != null) {
            if (atomicInteger.get() > imageViews.length - 1) {
                atomicInteger.set(0);
            }
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {

        }
    }

    /*
     * 每隔固定时间切换广告栏图片
     */
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            adViewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };

    private void initCirclePoint() {
        ViewGroup group = (ViewGroup)view.findViewById(R.id.pro_viewGroup);
        group.setFocusable(false);
        imageViews = new ImageView[pageViews.size()];
        // 广告栏的小圆点图标
        for (int i = 0; i < pageViews.size(); i++) {
            // 创建一个ImageView, 并设置宽高. 将该对象放入到数组中
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageViews[i] = imageView;

            // 初始值, 默认第0个选中
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.mipmap.circle_b1);
            } else {
                imageViews[i].setBackgroundResource(R.mipmap.circle_a1);
            }
            // 将小圆点放入到布局中
            group.addView(imageViews[i]);

            // 间隔
            LinearLayout myLayout = new LinearLayout(getActivity());
            myLayout.setLayoutParams(new ViewGroup.LayoutParams(15, ViewGroup.LayoutParams.WRAP_CONTENT));
            myLayout.setGravity(Gravity.CENTER);

            group.addView(myLayout);
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount&&isRefresh) {//判断是不是最后一个
            page++;
            isRefresh = false;
            getMessage(shop_id, page);
        }
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
     * ViewPager 页面改变监听器
     */
    private final class AdPageChangeListener implements ViewPager.OnPageChangeListener {

        public AdPageChangeListener() {
            super();
        }

        /**
         * 页面滚动状态发生改变的时候触发
         */
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        /**
         * 页面滚动的时候触发
         */
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        /**
         * 页面选中的时候触发
         */
        @Override
        public void onPageSelected(int position) {
            // 获取当前显示的页面是哪个页面
            atomicInteger.getAndSet(position);
            // 重新设置原点布局集合
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[position].setBackgroundResource(R.mipmap.circle_b1);
                if (position != i) {
                    imageViews[i].setBackgroundResource(R.mipmap.circle_a1);
                }
            }

        }
    }

    private final class AdPageAdapter extends PagerAdapter {
        private List<SimpleDraweeView> views = null;
        /**
         * 初始化数据源, 即View数组
         */
        public AdPageAdapter(List<SimpleDraweeView> views) {
            this.views = views;
        }
        /**
         * 从ViewPager中删除集合中对应索引的View对象
         */
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(views.get(position));
        }
        /**
         * 获取ViewPager的个数
         */
        @Override
        public int getCount() {
            return views.size();
        }

        /**
         * 从View集合中获取对应索引的元素, 并添加到ViewPager中
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(views.get(position), 0);
            //views.get(position).setOnClickListener(new TemplateOnClick(views.get(position),strWeb.get(position)));
            return views.get(position);
        }

        /**
         * 是否将显示的ViewPager页面与instantiateItem返回的对象进行关联 这个方法是必须实现的
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /*class TemplateOnClick implements View.OnClickListener {
        private ImageView iv;

        private String strWeb;

        public TemplateOnClick(ImageView ivImg,String strWeb) {
            this.iv=ivImg;
            this.strWeb=strWeb;
        }

        @Override
        public void onClick(View v) {
            if (!"webservice".equals(strWeb)) {
                Bundle bundle = new Bundle();
                bundle.putString("eventurl", strWeb);
                skipActivityforClass(getActivity(), PersonActivity.class, bundle);
            }else{
                iv.setClickable(false);
            }

        }
    }*/
    public void getQueryBanner() {
        //initProgressDialog();
        String urlString = ApiConstant.ADVLIST;
        RequestParams params = new RequestParams();
        params.put("type",2+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray array = jObj.getJSONArray("responseList");
                        for (int i = 0;i < array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            SimpleDraweeView img = new SimpleDraweeView(getActivity());
                            String imgUri = object.getString("m_pic");
                            Uri uri = Uri.parse(imgUri);
                            img.setImageURI(uri);
                            img.setScaleType(ImageView.ScaleType.FIT_XY);
                            pageViews.add(img);
                        }
                        if (adapter == null) {
                            adapter = new AdPageAdapter(pageViews);
                            adViewPager.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        if (pageViews.size() != 1) {
                            initCirclePoint();
                        }
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

       /* for (int i = 0;i < str.length;i++){
            SimpleDraweeView img = new SimpleDraweeView(getActivity());
            Uri uri = Uri.parse(str[i]);
            img.setImageURI(uri);
            //img.setImageResource(R.mipmap.a1);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            pageViews.add(img);
        }*/

                      /*      String webUrl = jsob.getString("webUrl");
                            if (StringUtil.isNotEmpty(webUrl)) {
                                strWeb.add(jsob.getString("webUrl"));
                            } else {
                                strWeb.add("webservice");
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                    }*/

       /* if (adapter == null) {
            adapter = new AdPageAdapter(pageViews);
            adViewPager.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        if (pageViews.size() != 1) {
            initCirclePoint();
        }*/

/*
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(Throwable arg0) { // 失败，调用
                Log.e("hck", " onFailure" + arg0.toString());
                close();
            };

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            };
        });*/
    }

    @Override
    public void onPause() {
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
