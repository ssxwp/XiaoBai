package com.qingyuan.tianya.mrbuy.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.AbstractSpinerAdapter;
import com.qingyuan.tianya.mrbuy.utils.Lunar;
import com.qingyuan.tianya.mrbuy.view.popuwindow.SpinnerPopuWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


/**
    首页fragment
 */
public class HomeFragment1 extends BaseFragment implements AbstractSpinerAdapter.IOnItemSelectListener {

    private View view;
    private TextView fr_home_left_text;
    private TextView fr_home_right_text;
    private TextView fr_home_data_text;
    private TextView fr_home_week_text;
    private TextView fr_home_spinner_text1;
    private TextView fr_home_spinner_text2;
    private TextView fr_home_spinner_text3;
    private TextView fr_home_spinner_text4;
    private RelativeLayout rl1,rl2,rl3,rl4;
    private List<String> nameList = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private LinearLayout ac_home_line;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        //((HomeActivity)this.getActivity()).registerMyTouchListener(mTouchListener);
        initView();
        initData();
        initEvents();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
       // ac_home_line = ((LinearLayout) getActivity().findViewById(R.id.ac_home_line));
        Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(),"iconfont/iconfont.ttf");
        fr_home_left_text = ((TextView) view.findViewById(R.id.fr_home_left_text));
        fr_home_left_text.setTypeface(iconfont);
        fr_home_right_text = ((TextView) view.findViewById(R.id.fr_home_right_text));
        fr_home_right_text.setTypeface(iconfont);
        fr_home_spinner_text1 = ((TextView) view.findViewById(R.id.fr_home_spinner_text1));
        fr_home_spinner_text1.setTypeface(iconfont);
        fr_home_spinner_text2 = ((TextView) view.findViewById(R.id.fr_home_spinner_text2));
        fr_home_spinner_text2.setTypeface(iconfont);
        fr_home_spinner_text3 = ((TextView) view.findViewById(R.id.fr_home_spinner_text3));
        fr_home_spinner_text3.setTypeface(iconfont);
        fr_home_spinner_text4 = ((TextView) view.findViewById(R.id.fr_home_spinner_text4));
        fr_home_spinner_text4.setTypeface(iconfont);
        fr_home_data_text = ((TextView) view.findViewById(R.id.fr_home_data_text));
        fr_home_week_text = ((TextView) view.findViewById(R.id.fr_home_week_text));
        rl1 = ((RelativeLayout) view.findViewById(R.id.spinner_rl1));
        rl2 = ((RelativeLayout) view.findViewById(R.id.spinner_rl2));
        rl3 = ((RelativeLayout) view.findViewById(R.id.spinner_rl3));
        rl4 = ((RelativeLayout) view.findViewById(R.id.spinner_rl4));

        mDrawerLayout = ((DrawerLayout) view.findViewById(R.id.drawerlayout));
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);


    }

    @Override
    public void initonClick() {
        fr_home_left_text.setOnClickListener(this);
        fr_home_right_text.setOnClickListener(this);
        fr_home_spinner_text1.setOnClickListener(this);
        fr_home_spinner_text2.setOnClickListener(this);
        fr_home_spinner_text3.setOnClickListener(this);
        fr_home_spinner_text4.setOnClickListener(this);
    }

    @Override
    public void initData() {
        /**
         * 获取日期
         */
        Time time = new Time("UTC+8");
        time.setToNow();
        int month = time.month+1;
        int day = time.monthDay;
        int weekDay = time.weekDay;
        String week;
        if ("1".equals(weekDay+"")){
            week = "一";
        }else if ("2".equals(weekDay+"")){
            week = "二";
        }else if ("3".equals(weekDay+"")){
            week = "三";
        }else if ("4".equals(weekDay+"")){
            week = "四";
        }else if ("5".equals(weekDay+"")){
          week = "五";
        }else if ("6".equals(weekDay+"")){
            week = "六";
        }else /*if ("7".equals(weekDay+""))*/{
            week = "日";
        }
        fr_home_data_text.setText(month + "月" + day + "日");
        Calendar calendar = Calendar.getInstance();
        fr_home_week_text.setText("周" + week + "\t\t\t" + new Lunar(calendar).toString());

        /**
         * spinner数据
         */
        String[] names = getResources().getStringArray(R.array.sort_name);
        Collections.addAll(nameList, names);
        mSpinnerPopuWindow = new SpinnerPopuWindow(getActivity());
        mSpinnerPopuWindow.refreshData(nameList,0);
        mSpinnerPopuWindow.setItemListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fr_home_spinner_text1:
                showPopuWindow(rl1);
                break;
            case R.id.fr_home_spinner_text2:
                showPopuWindow(rl2);
                break;
            case R.id.fr_home_spinner_text3:
                showPopuWindow(rl3);
                break;
            case R.id.fr_home_spinner_text4:
                showPopuWindow(rl4);
                break;
            case R.id.fr_home_right_text:
                //isOpen = true;
                ac_home_line.setVisibility(View.GONE);
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                        Gravity.RIGHT);
                break;
            case R.id.fr_home_left_text:
                //isOpen = true;
                ac_home_line.setVisibility(View.GONE);
                mDrawerLayout.openDrawer(Gravity.LEFT);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                        Gravity.LEFT);
                break;
        }
    }
    private SpinnerPopuWindow mSpinnerPopuWindow;
    private void showPopuWindow(View view) {
        mSpinnerPopuWindow.setWidth(view.getWidth());
        mSpinnerPopuWindow.showAsDropDown(view);
    }

    @Override
    public void onItemClick(int pos) {

    }
    /**
     * 左右侧滑
     */
    private void initEvents() {
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerStateChanged(int newState)
            {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                View mContent = mDrawerLayout.getChildAt(0);
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                if (drawerView.getTag().equals("LEFT"))
                {
                    float leftScale = 1 - 0.3f * scale;

                    ViewHelper.setScaleX(drawerView, leftScale);
                    ViewHelper.setScaleY(drawerView, leftScale);
                    ViewHelper.setAlpha(drawerView, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent,
                            drawerView.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                } else {
                    ViewHelper.setTranslationX(mContent,
                            -drawerView.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                //isOpen = false;
                ac_home_line.setVisibility(View.VISIBLE);
                mDrawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    /*private float x1 = 0;
    private boolean isOpen;*/
    /**
     * Fragment中，注册
     * 接收HomeActivity的Touch回调的对象
     * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
     */
    /*private HomeActivity.MyTouchListener mTouchListener = new HomeActivity.MyTouchListener() {
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
               if(x1 - x2 > 30&&!isOpen) {
                   isOpen = true;
                   ac_home_line.setVisibility(View.GONE);
                   mDrawerLayout.openDrawer(Gravity.RIGHT);
                   mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                           Gravity.RIGHT);
                } else if(x2 - x1 > 30&& !isOpen) {
                   isOpen =true;
                   ac_home_line.setVisibility(View.GONE);
                   mDrawerLayout.openDrawer(Gravity.LEFT);
                   mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                           Gravity.LEFT);

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
