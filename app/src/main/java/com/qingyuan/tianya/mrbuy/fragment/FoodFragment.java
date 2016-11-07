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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.activity.FoodMerchantActivity;
import com.qingyuan.tianya.mrbuy.activity.MerchantMessageActivity;
import com.qingyuan.tianya.mrbuy.adapter.DishMerchantAdapter;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 美食商家,点击“美食”菜单进入的页面
 */
public class FoodFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
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
    private int cate_id = 7;
    private int p = 1;
    private ListView mReListView;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private ScrollView scroll;
    private AMapUtil aMap;
    private double latitude;
    private double longitude;
    DishMerchantAdapter adapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        scroll = (ScrollView)view.findViewById(R.id.scroll);
        mListView = ((PullToRefreshListView) view.findViewById(R.id.fr_food_list));
        linear1 = ((LinearLayout) view.findViewById(R.id.fr_food_lin1));
        linear2 = ((LinearLayout) view.findViewById(R.id.fr_food_lin2));
        //grid_food = ((GridView) view.findViewById(R.id.grid_food));
        mReListView = mListView.getRefreshableView();
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        params.topMargin=50;
        mReListView.setLayoutParams(params);
        mReListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        pageViews = new ArrayList<>();
        // 创建ViewPager
        adViewPager = new ViewPager(getActivity());
        mReListView.setOnItemClickListener(FoodFragment.this);


    }

    @Override
    public void initonClick() {
        //mListView.setOnScrollListener(FoodFragment.this);
        view.findViewById(R.id.fr_food_dish_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_food_all_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_food_snack_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_food_quick_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_food_buffet_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_food_bbq_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_food_dessert_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_food_west_rel).setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY <= 0) {
                        getActivity().findViewById(R.id.header_view).setBackgroundColor(Color.argb(0, 241, 144, 5));
                    } else if (scrollY > 0 && scrollY <= adViewPager.getHeight()) {
                        float scale = (float) scrollY / adViewPager.getHeight();
                        float alpha = (255 * scale);
                        // 只是layout背景透明(仿知乎滑动效果)
                        getActivity().findViewById(R.id.header_view).setBackgroundColor(Color.argb((int) alpha, 241, 144, 5));
                    } else {
                        getActivity().findViewById(R.id.header_view).setBackgroundColor(Color.argb( 255, 241, 144, 5));
                    }
                }
            });
        }


    }

    @Override
    public void initData() {
        getQueryBanner();
        initViewPager();
        getMessage(cate_id, p);
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
                        getMessage(cate_id, p);
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
            case R.id.fr_food_dish_rel:
            Bundle bundle = new Bundle();
            bundle.putInt("merchant",2);
            skipActivityforClass(getActivity(), FoodMerchantActivity.class,bundle);
            break;
            case R.id.fr_food_snack_rel:
            Bundle bundle1 = new Bundle();
            bundle1.putInt("merchant",10);
            skipActivityforClass(getActivity(), FoodMerchantActivity.class,bundle1);
            break;
            case R.id.fr_food_quick_rel:
            Bundle bundle2 = new Bundle();
            bundle2.putInt("merchant",14);
            skipActivityforClass(getActivity(), FoodMerchantActivity.class,bundle2);
            break;

            case R.id.fr_food_buffet_rel:
            Bundle bundle3 = new Bundle();
            bundle3.putInt("merchant",7);
            skipActivityforClass(getActivity(), FoodMerchantActivity.class,bundle3);
            break;
            case R.id.fr_food_bbq_rel:
            Bundle bundle4 = new Bundle();
            bundle4.putInt("merchant",11);
            skipActivityforClass(getActivity(), FoodMerchantActivity.class,bundle4);
            break;
            case R.id.fr_food_dessert_rel:
            Bundle bundle5 = new Bundle();
            bundle5.putInt("merchant",12);
            skipActivityforClass(getActivity(), FoodMerchantActivity.class,bundle5);
            break;
            case R.id.fr_food_west_rel:
            Bundle bundle6 = new Bundle();
            bundle6.putInt("merchant",13);
            skipActivityforClass(getActivity(), FoodMerchantActivity.class, bundle6);
            break;
            case R.id.fr_food_all_rel:
                Bundle bundle7 = new Bundle();
                bundle7.putInt("merchant",15);
                skipActivityforClass(getActivity(), FoodMerchantActivity.class,bundle7);
                break;

        }
    }

    public void getMessage(int cate_id, int p) {
        String urlString = ApiConstant.DISHMERCHANT;
        RequestParams params = new RequestParams();
        params.put("cate_id", cate_id + "");
        params.put("p", p + "");
        params.put("latitude",latitude+"");
        params.put("longitude",longitude+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                Log.i("===============================================",response.toString());
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

                       /* if (isRefresh == true) {
                            mList.clear();
                        }*/
//                        DishMerchantAdapter adapters = null;
                        if (adapters == null) {
                            adapters = new DishMerchantAdapter(MrBuyApplication.getAppContext(), mList);
                            mReListView.setAdapter(adapters);
                            setListViewHeightBasedOnChildren(mReListView);
                        } else {
                            adapters.notifyDataSetChanged();
                        }
                        linear1.setVisibility(View.VISIBLE);
                        linear2.setVisibility(View.VISIBLE);
                        mReListView.setOnScrollListener(FoodFragment.this);
                       /* if (isRefresh == true) {
                            adapter.notifyDataSetChanged();
                        }*/
                        mListView.onRefreshComplete();
                        mListView.scrollTo(0, 50);
                        return;
                    } /*else {
                        Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }*/
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DishMessageBean bean = mList.get(position);
        int shop_id = bean.getShop_id();
        Bundle bundle = new Bundle();
        bundle.putString("sort", "dish");
        bundle.putInt("shop_id", shop_id);
        skipActivityforClass(getActivity(), MerchantMessageActivity.class, bundle);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState){
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                Fresco.getImagePipeline().resume();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                Fresco.getImagePipeline().pause();
                break;
        }
        /*if (lastItem == adapters.getCount()&&scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            p++;
            isRefresh = false;
            getMessage(cate_id, p);
        }*/
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        //lastItem = firstVisibleItem+visibleItemCount;
        //Log.i("ssssss",lastItem+"======"+totalItemCount);
        if (firstVisibleItem + visibleItemCount == totalItemCount/*view.getLastVisiblePosition()==view.getCount()-1&&isscroll ==false*/) {//判断是不是最后一个
            p++;
            getMessage(cate_id, p);
        }
    }

    /**
     * 解决scrollview嵌套listview数据显示不全问题
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

    /*===========================================viewpager=====================================================*/
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
    public void getQueryBanner() {
        initProgressDialog();
        String urlString = ApiConstant.ADVLIST;
        RequestParams params = new RequestParams();
        params.put("type",3+"");
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
                            if (StringUtil.isNotEmpty(imgUri)) {
                                Uri uri = Uri.parse(imgUri);
                                img.setImageURI(uri);
                                img.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
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
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mList!=null){
            mList.clear();
        }
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
        if (pageViews!=null){
            pageViews.clear();
        }
        super.onDestroy();
    }
}
