package com.qingyuan.tianya.mrbuy.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingyuan.tianya.mrbuy.R;

/**
 * 会员卡
 */
public class CarFragment extends BaseFragment {


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_car, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        //((HeaderView) view.findViewById(R.id.fr_car_head)).getLeftImageView().setVisibility(View.GONE);
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
}
