package com.qingyuan.tianya.mrbuy.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.activity.CollectActivity;
import com.qingyuan.tianya.mrbuy.activity.MessageActivity;
import com.qingyuan.tianya.mrbuy.activity.MoneyActivity;
import com.qingyuan.tianya.mrbuy.activity.OrderActivity;
import com.qingyuan.tianya.mrbuy.activity.PersonActivity;
import com.qingyuan.tianya.mrbuy.activity.SettingActivity;
import com.qingyuan.tianya.mrbuy.activity.VipActivity;

/**
 * 主页左侧fragment
 */
public class HomeLeftFragment extends BaseFragment {


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_left_layout,null);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(),"iconfont/iconfont.ttf");
        ((TextView) view.findViewById(R.id.fr_left_money_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_left_message_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_left_feedback_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_left_collect_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_left_setting_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_left_vip_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_left_merchant_img)).setTypeface(iconfont);
    }

    @Override
    public void initonClick() {
        view.findViewById(R.id.fr_left_my_rel1).setOnClickListener(this);
        view.findViewById(R.id.fr_left_money_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_left_message_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_left_feedback_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_left_collect_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_left_setting_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_left_vip_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_left_merchant_rel).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fr_left_my_rel1:
                skipActivityforClass(getActivity(), PersonActivity.class,null);
                break;
            case R.id.fr_left_money_rel:
                skipActivityforClass(getActivity(), MoneyActivity.class,null);
                break;
            case R.id.fr_left_message_rel:
                skipActivityforClass(getActivity(), MessageActivity.class,null);
                break;
            case R.id.fr_left_feedback_rel:
                skipActivityforClass(getActivity(), OrderActivity.class,null);
                break;
            case R.id.fr_left_collect_rel:
                skipActivityforClass(getActivity(), CollectActivity.class,null);
                break;
            case R.id.fr_left_setting_rel:
                skipActivityforClass(getActivity(), SettingActivity.class,null);
                break;
            case R.id.fr_left_vip_rel:
                skipActivityforClass(getActivity(), VipActivity.class,null);
                break;
            case R.id.fr_left_merchant_rel:
                skipActivityforClass(getActivity(), SettingActivity.class,null);
                break;
        }
    }
}
