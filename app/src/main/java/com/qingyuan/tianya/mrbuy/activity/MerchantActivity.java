package com.qingyuan.tianya.mrbuy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.fragment.FoodFragment;
import com.qingyuan.tianya.mrbuy.fragment.FunFragment;

import java.util.Objects;

public class MerchantActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        FragmentManager fManager =getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (Objects.equals(bundle.getString("merchant"), "food")){
            FoodFragment foodFragment = new FoodFragment();
            transaction.replace(R.id.ac_merchant_frag,foodFragment);
        }else if (Objects.equals(bundle.getString("merchant"), "fun")){
            FunFragment funFragment = new FunFragment();
            transaction.replace(R.id.ac_merchant_frag,funFragment);
        }
        transaction.commit();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initClick() {

    }
}
