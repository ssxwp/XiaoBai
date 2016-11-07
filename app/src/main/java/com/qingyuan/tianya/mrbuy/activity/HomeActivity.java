package com.qingyuan.tianya.mrbuy.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.slidingmenu.SlidingMenu;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.application.MrBuyApplication;
import com.qingyuan.tianya.mrbuy.fragment.BookFragment;
import com.qingyuan.tianya.mrbuy.fragment.FoodFragment;
import com.qingyuan.tianya.mrbuy.fragment.FunFragment;
import com.qingyuan.tianya.mrbuy.fragment.HomeFragment;
import com.qingyuan.tianya.mrbuy.fragment.MarketFragment;
import com.qingyuan.tianya.mrbuy.fragment.ShoppingFragment;
import com.qingyuan.tianya.mrbuy.fragment.WomenFragment;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.Update;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


/**
 * 主页
 */
public class HomeActivity extends BaseActivity implements TextWatcher {
    private Fragment fragment_home,fragment_fun,fragment_food,fragment_women,fragment_market,fragment_book;
    //private FragmentManager fManager;
    private SlidingMenu menu;
    private HeaderView head;
    private LinearLayout ll_left_layout;
    private ShoppingFragment fragment_shopping;
    private TextView search;
    private ImageView rightPic;
    private long mExitTime;
    private String from ="home";
    //private FragmentTransaction ft;
    private RelativeLayout home_head_search;
    private int vcode;
    private String vname;
    private String vdesc;
    private AlertDialog dialog1;
    // private String m_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addActivity(this);
        getTextUpdate();
        initView();
        initData();
        initClick();
        initSlidingMenu();
    }
    /**
     * 侧滑
     */
    private void initSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.menuview);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        //依附于activity
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        if (menu!=null) {
            menu.setMenu(R.layout.leftmenu);
        }
        init();
    }

    /**
     * 侧滑菜单点击事件
     */
    private void init() {
        findViewById(R.id.menu_food_rel).setOnClickListener(this);
        findViewById(R.id.menu_fun_rel).setOnClickListener(this);
        findViewById(R.id.menu_home_rel).setOnClickListener(this);
        findViewById(R.id.menu_shopping_rel).setOnClickListener(this);
        findViewById(R.id.menu_women_rel).setOnClickListener(this);
        findViewById(R.id.menu_market_rel).setOnClickListener(this);
        findViewById(R.id.menu_book_rel).setOnClickListener(this);
        //findViewById(R.id.menu_travel_rel).setOnClickListener(this);
        findViewById(R.id.leftmenu_personnal).setOnClickListener(this);
        findViewById(R.id.leftmenu_collect).setOnClickListener(this);
    }
    @Override
    public void initView() {
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"iconfont/iconfont.ttf");
        ((TextView)findViewById(R.id.fr_right_search)).setTypeface(iconfont);
        head = ((HeaderView) findViewById(R.id.header_view));
        head.getLeftImageView().setImageResource(R.mipmap.back);
        head.getRightText().setVisibility(View.GONE);
        head.getHeadBackGround().setBackgroundColor(Color.argb(0, 255, 255, 255));
        rightPic = head.getRightPic();
        rightPic.setImageResource(R.mipmap.car1);
        ll_left_layout=(LinearLayout) head.findViewById(R.id.ll_left_layout);
        search = ((TextView) findViewById(R.id.fr_right_ed));
        home_head_search = (RelativeLayout)findViewById(R.id.home_head_search);
       /* tab_poi_lin = ((RelativeLayout) view.findViewById(R.id.tab_poi_lin));
        tab_qr_lin = ((RelativeLayout) view.findViewById(R.id.tab_qr_lin));
        tab_car_lin = ((RelativeLayout) view.findViewById(R.id.tab_car_lin));
        tab_order_lin = ((RelativeLayout) view.findViewById(R.id.tab_order_lin));*/
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            from = bundle.getString("from");
        }
        //fManager =getSupportFragmentManager();
        //FragmentTransaction transaction = fManager.beginTransaction();
        if (from != null) {
            switch (from) {
                case "info":
                    if (fragment_book == null) {
                        fragment_book = new BookFragment();
                    }
                    switchContent(fragment_book);
                    //transaction.replace(R.id.ac_home_content, fragment_book);

                    break;
                case "info1":
                    if (fragment_shopping == null) {
                        fragment_shopping = new ShoppingFragment(menu);
                    }
                    switchContent(fragment_shopping);
                    //transaction.replace(R.id.ac_home_content, fragment_shopping);
                    break;
                default:
                    if (fragment_home == null) {
                        fragment_home = new HomeFragment();
                    }
                    switchContent(fragment_home);
                    //transaction.replace(R.id.ac_home_content, fragment_home);
                    break;
            }
        }
        //transaction.commit();
    }
    /**
     * 隐藏所有的fragment
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (fragment_home!=null){
            transaction.hide(fragment_home);
        }
        if (fragment_food!=null){
            transaction.hide(fragment_food);
        }
        if (fragment_fun!=null){
            transaction.hide(fragment_fun);
        }
        if (fragment_book!=null){
            transaction.hide(fragment_book);
        }
        if (fragment_women!=null){
            transaction.hide(fragment_women);
        }
        if (fragment_shopping!=null){
            transaction.hide(fragment_shopping);
        }
    }
    public void switchContent( Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragments(ft);
        if (!fragment.isAdded()) {
            ft.add(R.id.ac_home_content, fragment);
        }
        ft.show(fragment);
        ft.commit();
    }
    @Override
    public void initClick() {
        findViewById(R.id.ac_home_qr).setOnClickListener(this);
        findViewById(R.id.logo_vip).setOnClickListener(this);
        search.setOnClickListener(this);
        //search.addTextChangedListener(this);
        findViewById(R.id.fr_right_search).setOnClickListener(this);
        head.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });
        ll_left_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });
        rightPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isNotEmpty(createSharedPreference(HomeActivity.this, "user_custId").getValue("custId"))) {
                    skipActivity(HomeActivity.this, CarActivity.class, null);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("from","car");
                    skipActivity(HomeActivity.this, LoginActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_fun_rel:
                setItemChioce(0);
                break;
            case R.id.menu_food_rel:
                setItemChioce(1);
                break;
            case R.id.menu_home_rel:
                setItemChioce(2);
                break;
            case R.id.menu_shopping_rel:
                setItemChioce(3);
                break;
            case R.id.menu_women_rel:
                setItemChioce(4);
                break;
            case R.id.menu_market_rel:
                toast("等待开通，开通后我们将第一时间通知您");
                //setItemChioce(5);
                break;
            case R.id.menu_book_rel:
                setItemChioce(6);
                break;
            case R.id.leftmenu_personnal:
                if(StringUtil.isNotEmpty(createSharedPreference(HomeActivity.this, "user_custId").getValue("custId"))) {
                    skipActivity(this,PersonalInfoActivity.class,null);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("from","personal");
                    skipActivity(HomeActivity.this, LoginActivity.class, bundle);
                }
                break;
            case R.id.leftmenu_collect:
                if(StringUtil.isNotEmpty(createSharedPreference(HomeActivity.this, "user_custId").getValue("custId"))) {
                    skipActivity(this,CollectActivity.class,null);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("from","collect");
                    skipActivity(HomeActivity.this, LoginActivity.class, bundle);
                }
                break;
            case R.id.ac_home_qr:
                skipActivity(this,MipcaActivityCapture.class,null);
                break;
            case R.id.fr_right_ed:
                skipActivity(this,SearchActivity.class,null);
                break;
            case R.id.fr_right_search:
                skipActivity(this,SearchActivity.class,null);
                break;
            case R.id.logo_vip:
                if(StringUtil.isNotEmpty(createSharedPreference(HomeActivity.this, "user_custId").getValue("custId"))) {
                    skipActivity(this,VipCarActivity.class,null);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("from","vip");
                    skipActivity(this, LoginActivity.class, bundle);
                }
                break;
           /* case R.id.tab_poi_lin:
                Bundle bundle=new Bundle();
                bundle.putString("home", "home");
                skipActivity(this, PoiActivity.class, bundle);
                break;
            case R.id.tab_qr_lin:
                skipActivity(this,MipcaActivityCapture.class,null);
                break;
            case R.id.tab_car_lin:
                skipActivity(this,CarActivity.class,null);
                break;
            case R.id.tab_order_lin:
                skipActivity(this,OrderActivity.class,null);
                break;*/
        }
    }



    private void setItemChioce(int i) {
        //FragmentTransaction transaction = fManager.beginTransaction();
        switch (i){
            case 0:
                    setMenuMode();
                if (fragment_fun == null) {
                    fragment_fun = new FunFragment();
                }
                head.getMidTextView().setVisibility(View.VISIBLE);
                head.getMidTextView().setText("休闲娱乐");
                head.getHeadBackGround().setBackgroundColor(Color.argb(0, 0, 139, 255));
                findViewById(R.id.home_head_search).setVisibility(View.GONE);
                switchContent(fragment_fun);
                break;
            case 1:
                    setMenuMode();
                if (fragment_food == null) {
                    fragment_food = new FoodFragment();
                }
                head.getMidTextView().setVisibility(View.VISIBLE);
                head.getMidTextView().setText("美食");
                home_head_search.setVisibility(View.GONE);
                head.getHeadBackGround().setBackgroundColor(Color.argb(0,241, 144, 5));
                switchContent(fragment_food);
                    //transaction.replace(R.id.ac_home_content, fragment_food);
                break;
            case 2:
//                点击首页
                    setMenuMode();
                if (fragment_home==null) {
                    fragment_home = new HomeFragment();
                }
                home_head_search.setVisibility(View.VISIBLE);
                head.getMidTextView().setVisibility(View.GONE);
                head.getHeadBackGround().setBackgroundColor(Color.argb(0, 0, 139, 139));
                switchContent(fragment_home);
                //transaction.replace(R.id.ac_home_content, fragment_home);
                break;
            case 3:
                if (fragment_shopping==null) {
                    fragment_shopping = new ShoppingFragment(menu);
                }
                head.getMidTextView().setVisibility(View.VISIBLE);
                head.getMidTextView().setText("购物");
                head.getHeadBackGround().setBackgroundColor(getResources().getColor(R.color.red));
                home_head_search.setVisibility(View.GONE);
                switchContent(fragment_shopping);
                //transaction.replace(R.id.ac_home_content, fragment_shopping);
                break;
            case 4:
                setMenuMode();
                if (fragment_women == null){
                    fragment_women = new WomenFragment();
                }
                head.getMidTextView().setVisibility(View.VISIBLE);
                head.getMidTextView().setText("丽人");
                head.getHeadBackGround().setBackgroundColor(Color.rgb(236, 17, 97));
                home_head_search.setVisibility(View.GONE);
                switchContent(fragment_women);
                //transaction.replace(R.id.ac_home_content, fragment_women);

                break;
            case 5:
                setMenuMode();
                if (fragment_market==null) {
                    fragment_market = new MarketFragment();
                }
                head.getMidTextView().setVisibility(View.VISIBLE);
                head.getMidTextView().setText("超市");
                home_head_search.setVisibility(View.GONE);
                switchContent(fragment_market);
                //transaction.replace(R.id.ac_home_content, fragment_market);
                break;
            case 6:
                setMenuMode();
                if (fragment_book == null) {
                    fragment_book = new BookFragment();
                }
                head.getMidTextView().setVisibility(View.VISIBLE);
                head.getMidTextView().setText("书店");
                head.getHeadBackGround().setBackgroundColor(Color.rgb(0, 139, 139));
                home_head_search.setVisibility(View.GONE);
                switchContent(fragment_book);
                //transaction.replace(R.id.ac_home_content, fragment_book);
                break;
            case 7:
                setMenuMode();
                    //fragment_travel = new TravelFragment();
                    //transaction.replace(R.id.ac_home_content, fragment_travel);
                break;
        }
        //transaction.commit();
        menu.toggle();
    }
    private void setMenuMode(){
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //search();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finshAllActivity();
                System.exit(0);
            }
        }
        return true;
    }

    public void getTextUpdate() {
        String urlString = ApiConstant.RECHARGE_URL;
        RequestParams params=new RequestParams();
        HttpUtil.post(urlString,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                Log.i("TAG", "检测版本更新：" + s);
                try {
                    JSONObject jObj = new JSONObject(s);
                    if (jObj.getString("code").equals("0")) {
                        JSONArray jsonArray = jObj.getJSONArray("jsonArray");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj=jsonArray.getJSONObject(i);
                            vcode=jobj.getInt("vcode");
                            vname=jobj.getString("vname");
                            vdesc=jobj.getString("vdesc");
                            if (vcode>0) {
                                if (vcode> MrBuyApplication.getAppContext().getCode()) {
                                    // 返回版本大于当前版本
                                    // 需要升级
                                    showUpdataDialog();
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
            }
            public void onFailure(Throwable arg0) { // 失败，调用
                Log.e("hck", " onFailure" + arg0.toString());
                close();
            };
            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            };
        });
    }
    protected void showUpdataDialog() {
        dialog1 = new AlertDialog.Builder(this).create();
        dialog1.show();
        dialog1.getWindow().setContentView(R.layout.dialog_sure);
        dialog1.getWindow().findViewById(R.id.dialog_out_diss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadApk();
                dialog1.dismiss();
            }
        });
        dialog1.getWindow().findViewById(R.id.dialog_out_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }
    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(HomeActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setCancelable(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = Update.getFileFromServer("http://oiuh123.oss-cn-shanghai.aliyuncs.com/"+vname+".apk", pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
                    // Message msg = new Message();
                    // msg.what = DOWN_ERROR;
                    // handler.sendMessage(msg);
                    // e.printStackTrace();
                }
            }
        }.start();
    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        HomeActivity.this.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    /* *//**
     * 加号选项菜单
     *//*
    private PopupWindow popupWindow;
    private void showPopuWindow() {
        ac_home_add.setText(R.string.icons_add2);
        if (popupWindow == null){
            *//*TextView tab_poi_text = (TextView) view.findViewById(R.id.tab_poi_text);
            tab_poi_text.setTypeface(iconfont);
            TextView tab_qr_text = (TextView) view.findViewById(R.id.tab_qr_text);
            tab_qr_text.setTypeface(iconfont);
            TextView tab_car_text = (TextView) view.findViewById(R.id.tab_car_text);
            tab_car_text.setTypeface(iconfont);
            TextView tab_order_text = (TextView) view.findViewById(R.id.tab_order_text);
            tab_order_text.setTypeface(iconfont);*//*
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, 800,200);
            // 设置焦点在弹窗上
            popupWindow.setFocusable(true);
            //设置动画
            popupWindow.setAnimationStyle(R.style.popwin_anim_style);
            // 设置允许在外点击消失
            popupWindow.setOutsideTouchable(true);
            // 设置弹窗消失事件监听
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    ac_home_add.setText(R.string.icons_add);
                }
            });
        }
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    ac_home_add.setText(R.string.icons_add);
                    return true;
                }
                return false;
            }
        });
        if(!popupWindow.isShowing()) {
            //popupWindow.showAsDropDown(ac_home_add, Gravity.CENTER,-150);
            int[] location = new int[2];
            ac_home_add.getLocationOnScreen(location);
            //设置显示在控件左边
            popupWindow.showAtLocation(ac_home_add, Gravity.NO_GRAVITY, location[0]-popupWindow.getWidth(), location[1]);
        }
    }

    private void setChioceItem(int index) {
        FragmentTransaction transaction = fManager.beginTransaction();
        clearChoice();
        hideFragments(transaction);
        switch (index){
            case 0:
                ac_home_course_image.setTextColor(gray);
                ac_home_course_text.setTextColor(gray);
                if (fragment_home == null){
                    fragment_home = new HomeFragment();
                    transaction.add(R.id.ac_home_content, fragment_home);
                }else {
                    transaction.show(fragment_home);
                }
                break;
            case 1:
                ac_home_found_image.setTextColor(gray);
                ac_home_found_text.setTextColor(gray);
                if (fragment_found==null){
                    fragment_found = new FoundFragment();
                    transaction.add(R.id.ac_home_content, fragment_found);
                }else {
                    transaction.show(fragment_found);
                }
                break;
            case 2:
                ac_home_car_image.setTextColor(gray);
                ac_home_car_text.setTextColor(gray);
                if (fragment_car==null){
                    fragment_car = new CarFragment();
                    transaction.add(R.id.ac_home_content, fragment_car);
                }else {
                    transaction.show(fragment_car);
                }
                break;
            case 3:
                ac_home_personal_image.setTextColor(gray);
                ac_home_personal_text.setTextColor(gray);
                if (fragment_personal==null){
                    fragment_personal = new PersonalFragment();
                    transaction.add(R.id.ac_home_content, fragment_personal);
                }else {
                    transaction.show(fragment_personal);
                }
                break;
        }
        transaction.commit();
    }

*/

   /* *//**
     * 回调接口
     * @author gaoyanjun
     **//*

    public interface MyTouchListener
    {
        void onTouchEvent(MotionEvent event);
    }


    *//** 保存MyTouchListener接口的列表*//*

    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<>();

   *//* *
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *//*
    public void registerMyTouchListener(MyTouchListener listener)
    {
        myTouchListeners.add( listener );
    }

   *//* *
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *//*
    public void unRegisterMyTouchListener(MyTouchListener listener)
    {
        myTouchListeners.remove(listener);
    }

   *//* *
     * 分发触摸事件给所有注册了MyTouchListener的接口
     *//*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }
}