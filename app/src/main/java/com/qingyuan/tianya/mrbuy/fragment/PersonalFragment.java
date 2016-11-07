package com.qingyuan.tianya.mrbuy.fragment;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.activity.CollectActivity;
import com.qingyuan.tianya.mrbuy.activity.HomeActivity;
import com.qingyuan.tianya.mrbuy.activity.MessageActivity;
import com.qingyuan.tianya.mrbuy.activity.MoneyActivity;
import com.qingyuan.tianya.mrbuy.activity.OrderActivity;
import com.qingyuan.tianya.mrbuy.activity.PersonalInfoActivity;

/**
 * 个人中心
 */
public class PersonalFragment extends BaseFragment {


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {

        Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(), "iconfont/iconfont.ttf");
        ((TextView) view.findViewById(R.id.fr_personal_money_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_personal_order_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_personal_message_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_personal_collect_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_personal_merchant_img)).setTypeface(iconfont);
        ((TextView) view.findViewById(R.id.fr_personal_task_img)).setTypeface(iconfont);
    }

    @Override
    public void initonClick() {
        view.findViewById(R.id.fr_personal_money_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_personal_order_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_personal_collect_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_personal_message_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_personal_task_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_personal_merchant_rel).setOnClickListener(this);
        view.findViewById(R.id.fr_personal_head).setOnClickListener(this);
        view.findViewById(R.id.fr_personal_vip_text).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fr_personal_money_rel:
                skipActivityforClass(getActivity(), MoneyActivity.class,null);
                break;
            case R.id.fr_personal_order_rel:
                skipActivityforClass(getActivity(), OrderActivity.class,null);
                break;
            case R.id.fr_personal_collect_rel:
                skipActivityforClass(getActivity(), CollectActivity.class,null);
                break;
            case R.id.fr_personal_message_rel:
                skipActivityforClass(getActivity(), MessageActivity.class,null);
                break;
            case R.id.fr_personal_task_rel:
                skipActivityforClass(getActivity(), MoneyActivity.class,null);
                break;
            case R.id.fr_personal_merchant_rel:
                break;
            case R.id.fr_personal_head:
                skipActivityforClass(getActivity(), PersonalInfoActivity.class,null);
                break;
            case R.id.fr_personal_vip_text:
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("vip","vip");
                startActivity(intent);
                //skipActivityforClass(getActivity(), VipActivity.class,null);
                break;
        }
    }
}
