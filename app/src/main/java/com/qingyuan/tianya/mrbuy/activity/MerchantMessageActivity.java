package com.qingyuan.tianya.mrbuy.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.fragment.DishMerchantFragment;
import com.qingyuan.tianya.mrbuy.fragment.FunGoodsFragment;

/**
 * 商品列表
 */
public class MerchantMessageActivity extends BaseActivity{

    private String sort;
    private int shop_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_message);
        Log.e("Main","--------------------------------------------MerchantMessageActivity");
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        sort = bundle.getString("sort");
        shop_id = bundle.getInt("shop_id");
    }

    @Override
    public void initData() {
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        if (sort!=null&&sort.equals("dish")){

            DishMerchantFragment fragment_dish=new DishMerchantFragment(shop_id);
            transaction.replace(R.id.ac_merchant_content,fragment_dish);
        }else if (sort!=null&&sort.equals("fun")){
            FunGoodsFragment fragment_dish=new FunGoodsFragment(shop_id);
            transaction.replace(R.id.ac_merchant_content,fragment_dish);
        }

        transaction.commit();
    }

    @Override
    public void initClick() {

    }


}
