package com.qingyuan.tianya.mrbuy.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingyuan.tianya.mrbuy.R;

/**
 * 发现
 */
public class FoundFragment extends BaseFragment {


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_found, container, false);
        initView();
        return view;
    }

    @Override
    public void initView() {
        //((HomeActivity)this.getActivity()).registerMyTouchListener(myTouchListener);
        view.findViewById(R.id.fr_found_head).findViewById(R.id.ll_left_layout).setVisibility(View.GONE);
    }

    @Override
    public void initonClick() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }

  /*  *//**
     * 在activity中注册了手势监听，对这里有影响，不知道怎么解决？
     *//*
    private HomeActivity.MyTouchListener myTouchListener = new HomeActivity.MyTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            getActivity().findViewById(R.id.ac_home_line).setVisibility(View.VISIBLE);
            *//*LayoutInflater inflater = LayoutInflater.from(getActivity());
            inflater.inflate(R.layout.fragment_home, null).findViewById(R.id.drawerlayout).setVisibility(View.GONE);*//*
        }
    };*/
}
