package com.qingyuan.tianya.mrbuy.fragment;



import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.activity.DaodActivity;
import com.qingyuan.tianya.mrbuy.activity.HomeActivity;
import com.qingyuan.tianya.mrbuy.activity.InverActivity;
import com.qingyuan.tianya.mrbuy.activity.MerchantMessageActivity;
import com.qingyuan.tianya.mrbuy.activity.RegardtyyouActivity;
import com.qingyuan.tianya.mrbuy.adapter.HomeAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.application.MrBuyApplication;
import com.qingyuan.tianya.mrbuy.bean.DishMessageBean;
import com.qingyuan.tianya.mrbuy.view.view.MyGridView;
import com.qingyuan.tianya.mrbuy.view.view.ScrollTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主页fragment
 */
public class HomeFragment extends BaseFragment {


    private View view;
    private ViewPager adViewPager;
    private Thread thread;
    private AdPageAdapter adapter;
    private boolean isRunning = true;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private ImageView[] imageViews ;
    private List<SimpleDraweeView> pageViews = null;
    private ScrollTextView fr_home_food_rel;
    List<String> tBeans = new ArrayList<>();
    private ArrayList<DishMessageBean> mList;
    private ArrayList<DishMessageBean> mList1;
    private ArrayList<DishMessageBean> mList2;
    private ArrayList<DishMessageBean> mList3;
    private MyGridView mGridView = null;
    private MyGridView mGridView1;
    private MyGridView mGridView2;
    private MyGridView mGridView3;
    private FrameLayout fram;
    private FrameLayout fram1;
    private FrameLayout fram2;
    private FrameLayout fram3;
    private LinearLayout linear;
    private LinearLayout llguyututu;
    private ScrollView scroll;
    private RelativeLayout rlguang_yu,rl_chengyaotuyao,rl_daodtuyao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_right_layout, null);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        rl_daodtuyao=(RelativeLayout)view.findViewById(R.id.rl_daodiao);
        rlguang_yu=(RelativeLayout)view.findViewById(R.id.rl_guangyutuyou);
        rl_chengyaotuyao=(RelativeLayout)view.findViewById(R.id.rl_chengyaotuyou);
        llguyututu=(LinearLayout)view.findViewById(R.id.ll_guangyututu);
        rl_chengyaotuyao.setOnClickListener(this);
        rlguang_yu.setOnClickListener(this);
        rl_daodtuyao.setOnClickListener(this);
        scroll = ((ScrollView) view.findViewById(R.id.scroll));
        linear = (LinearLayout)view.findViewById(R.id.linear);
        fr_home_food_rel = ((ScrollTextView) view.findViewById(R.id.fr_home_food_rel));
        pageViews = new ArrayList<>();
        // 创建ViewPager
        adViewPager = new ViewPager(getActivity());
        fram = (FrameLayout)view.findViewById(R.id.fram);
        fram1 = (FrameLayout)view.findViewById(R.id.fram1);
        fram2 = (FrameLayout)view.findViewById(R.id.fram2);
        fram3 = (FrameLayout)view.findViewById(R.id.fram3);
        mGridView = (MyGridView) view.findViewById(R.id.grid);
        mGridView1 = (MyGridView) view.findViewById(R.id.grid1);
        mGridView2 = (MyGridView) view.findViewById(R.id.grid2);
        mGridView3 = (MyGridView) view.findViewById(R.id.grid3);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DishMessageBean bean = mList.get(position);
                int shop_id = bean.getShop_id();
                Bundle bundle = new Bundle();
                bundle.putString("sort","dish");
                bundle.putInt("shop_id",shop_id);
                skipActivityforClass(getActivity(), MerchantMessageActivity.class, bundle);
            }
        });
        mGridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DishMessageBean bean = mList.get(position);
                int shop_id = bean.getShop_id();
                Bundle bundle = new Bundle();
                bundle.putString("sort","fun");
                bundle.putInt("shop_id",shop_id);
                skipActivityforClass(getActivity(), MerchantMessageActivity.class, bundle);
            }
        });
        mGridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DishMessageBean bean = mList.get(position);
                int shop_id = bean.getShop_id();
                Bundle bundle = new Bundle();
                bundle.putString("sort","fun");
                bundle.putInt("shop_id",shop_id);
                skipActivityforClass(getActivity(), MerchantMessageActivity.class, bundle);
            }
        });
        mGridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("from","info");
                skipActivityforClassClose(getActivity(), HomeActivity.class, bundle);
            }
        });

    }

    @Override
    public void initonClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY <= 0) {
                        getActivity().findViewById(R.id.header_view).setBackgroundColor(Color.argb(0, 0, 139, 139));
                    } else if (scrollY > 0 && scrollY <= adViewPager.getHeight()) {
                        float scale = (float) scrollY / adViewPager.getHeight();
                        float alpha = (255 * scale);
                        // 只是layout背景透明(仿知乎滑动效果)
                        getActivity().findViewById(R.id.header_view).setBackgroundColor(Color.argb((int) alpha, 0, 139, 139));
                    } else {
                        getActivity().findViewById(R.id.header_view).setBackgroundColor(Color.argb( 255, 0, 139, 139));
                    }
                }
            });
        }
    }

    @Override
    public void initData() {
        getShop();
        getQueryBanner();
        initViewPager();
        for (int i = 0 ; i < 11 ; i ++){
            tBeans.add("兔友news：测试数据第" + i +" 行");
        }
        if (tBeans!=null&&tBeans.size()>0) {

            StringBuilder sBuilder = new StringBuilder();
            for (String threadlistBean : tBeans) {
                sBuilder.append(threadlistBean).append("k#");


            }
            sBuilder.deleteCharAt(sBuilder.lastIndexOf("#"));
            sBuilder.deleteCharAt(sBuilder.lastIndexOf("k"));
            fr_home_food_rel.setScrollText(sBuilder.toString().trim());

        }
    }

    private void getShop() {
        mList = new ArrayList<>();
        mList1 = new ArrayList<>();
        mList2 = new ArrayList<>();
        mList3 = new ArrayList<>();
        RequestParams params = new RequestParams();
        HttpUtil.gets("http://114.215.78.102/index.php?s=/Api/Index/home", params, new AsyncHttpResponseHandler() {

            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject customer = jObj.getJSONObject("responseList");
                        JSONArray array = customer.getJSONArray("meishi");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject cus = array.getJSONObject(i);
                            int custId = cus.getInt("s_id");
                            String img = cus.getString("head_pic");
                            String name = cus.getString("shopname");
                            String province = cus.getString("province");
                            String city = cus.getString("city");
                            String district = cus.getString("district");
                            String p_price = cus.getString("p_price");
                            String q_price = cus.getString("q_price");
                            String status = cus.getString("status");
                            String volume = cus.getString("volume");
                            String price = cus.getString("price");
                            String juli = "";
                            DishMessageBean dishBean = new DishMessageBean(custId, name, img, province, city, district, p_price, q_price, status, volume, price,juli);
                            mList.add(dishBean);
                        }
                        HomeAdapter adapter = new HomeAdapter(MrBuyApplication.getAppContext(), mList);
                        mGridView.setAdapter(adapter);
                        fram.setVisibility(View.VISIBLE);
                        JSONArray array1 = customer.getJSONArray("xiuxianyule");
                        for (int i = 0; i < array1.length(); i++) {
                            JSONObject cus = array1.getJSONObject(i);
                            int custId = cus.getInt("s_id");
                            String img = cus.getString("head_pic");
                            String name = cus.getString("shopname");
                            String province = cus.getString("province");
                            String city = cus.getString("city");
                            String district = cus.getString("district");
                            String p_price = cus.getString("p_price");
                            String q_price = cus.getString("q_price");
                            String status = cus.getString("status");
                            String volume = cus.getString("volume");
                            String price = cus.getString("price");
                            String juli = "";
                            DishMessageBean dishBean = new DishMessageBean(custId, name, img, province, city, district, p_price, q_price, status, volume, price,juli);
                            mList1.add(dishBean);
                        }
                        HomeAdapter adapter1 = new HomeAdapter(MrBuyApplication.getAppContext(), mList1);
                        mGridView1.setAdapter(adapter1);
                        fram1.setVisibility(View.VISIBLE);
                        JSONArray array2 = customer.getJSONArray("liren");
                        for (int i = 0; i < array2.length(); i++) {
                            JSONObject cus = array2.getJSONObject(i);
                            int custId = cus.getInt("s_id");
                            String img = cus.getString("head_pic");
                            String name = cus.getString("shopname");
                            String province = cus.getString("province");
                            String city = cus.getString("city");
                            String district = cus.getString("district");
                            String p_price = cus.getString("p_price");
                            String q_price = cus.getString("q_price");
                            String status = cus.getString("status");
                            String volume = cus.getString("volume");
                            String price = cus.getString("price");
                            String juli = "";
                            DishMessageBean dishBean = new DishMessageBean(custId, name, img, province, city, district, p_price, q_price, status, volume, price,juli);
                            mList2.add(dishBean);
                        }
                        HomeAdapter adapter2 = new HomeAdapter(MrBuyApplication.getAppContext(), mList2);
                        mGridView2.setAdapter(adapter2);
                        fram2.setVisibility(View.VISIBLE);
                        JSONArray array3 = customer.getJSONArray("shudian");
                        for (int i = 0; i < array3.length(); i++) {
                            JSONObject cus = array3.getJSONObject(i);
                            int custId = cus.getInt("s_id");
                            String img = cus.getString("head_pic");
                            String name = cus.getString("shopname");
                            String province = cus.getString("province");
                            String city = cus.getString("city");
                            String district = cus.getString("district");
                            String p_price = cus.getString("p_price");
                            String q_price = cus.getString("q_price");
                            String status = cus.getString("status");
                            String volume = cus.getString("volume");
                            String price = cus.getString("price");
                            String juli = "";
                            DishMessageBean dishBean = new DishMessageBean(custId, name, img, province, city, district, p_price, q_price, status, volume, price,juli);
                            mList3.add(dishBean);
                        }
                        HomeAdapter adapter3 = new HomeAdapter(MrBuyApplication.getAppContext(), mList3);
                        mGridView3.setAdapter(adapter3);
                        fram3.setVisibility(View.VISIBLE);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_guangyutuyou:
                skipActivityforClass(getActivity(), RegardtyyouActivity.class,null);
                break;
            case R.id.rl_chengyaotuyou:
                skipActivityforClass(getActivity(), InverActivity.class,null);
                break;
            case R.id.rl_daodiao:
                skipActivityforClass(getActivity(), DaodActivity.class,null);
                break;
        }

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
        String urlString = ApiConstant.ADVLIST;
        RequestParams params = new RequestParams();
        params.put("type",1+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray array = jObj.getJSONArray("responseList");
                        for (int i = 0;i < array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            SimpleDraweeView img = new SimpleDraweeView(MrBuyApplication.getAppContext());
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
                        linear.setVisibility(View.VISIBLE);
                        fr_home_food_rel.setVisibility(View.VISIBLE);
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
    public void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
        if (mList1!=null){
            mList1.clear();
        }
        if (mList2!=null){
            mList2.clear();
        }
        if (mList3!=null){
            mList3.clear();
        }
        if (pageViews!=null){
            pageViews.clear();
        }
    }
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
   /* private float x1 = 0;
    //private boolean isOpen;
   *//* *
     * Fragment中，注册
     * 接收HomeActivity的Touch回调的对象
     * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理*//*
    private HomeActivity.MyTouchListener mTouchListener = new HomeActivity.MyTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            //继承了Activity的onTouchEvent方法，直接监听点击事件
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                x1 = event.getX();
            }
            if(event.getAction() == MotionEvent.ACTION_UP) {
                //当手指离开的时候
                float x2 = event.getX();
               if(x2 - x1 > 30*//*&&!isOpen*//*) {
                   mDrawerLayout.openDrawer(Gravity.LEFT);
                   mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                           Gravity.LEFT);
                   *//*isOpen = true;
                   mDrawerLayout.openDrawer(Gravity.RIGHT);
                   mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                           Gravity.RIGHT);
                } else if(x2 - x1 > 30&& !isOpen) {
                   isOpen =true;*//*


                }
            }
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeActivity)this.getActivity()).unRegisterMyTouchListener(mTouchListener);
    }*/
}
