package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.fragment.GoodsCollectionFragment;
import com.qingyuan.tianya.mrbuy.fragment.MerchantCollectFragment;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;


/**
 * 收藏
 */
public class CollectActivity extends BaseActivity{

    private TextView ac_collect_merchant_text;
    private TextView ac_collect_good_text;
    private Fragment fragment_shop;
    private Fragment fragment_goods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"iconfont/iconfont.ttf");
        ac_collect_merchant_text = ((TextView) findViewById(R.id.ac_collect_merchant_text));
        ac_collect_good_text = ((TextView) findViewById(R.id.ac_collect_good_text));
        HeaderView headView = ((HeaderView) findViewById(R.id.ac_collect_head));
        headView.getRightText().setTypeface(iconfont);
        headView.getRightText().setTextSize(25);
        headView.getHeadBackGround().setBackgroundColor(Color.rgb(254, 96, 0));
    }

    @Override
    public void initData() {

        if (fragment_shop == null) {
            fragment_shop = new MerchantCollectFragment();
        }
        switchContent(fragment_shop);
    }

    @Override
    public void initClick() {
        ac_collect_merchant_text.setOnClickListener(this);
        ac_collect_good_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ac_collect_merchant_text:
                getMerchant();
                break;
            case R.id.ac_collect_good_text:
                getGood();
                break;
        }
    }

    /**
     * 商品列表
     */
    private void getGood() {
        ac_collect_good_text.setBackgroundResource(R.drawable.shape_right_select);
        ac_collect_good_text.setTextColor(getResources().getColor(R.color.collectorange));

        ac_collect_merchant_text.setBackgroundResource(R.drawable.shape_left);
        ac_collect_merchant_text.setTextColor(getResources().getColor(R.color.white));
       if (fragment_goods == null) {
           fragment_goods = new GoodsCollectionFragment();
       }
        switchContent(fragment_goods);

    }

    /**
     * 商家列表
     */
    private void getMerchant() {
        ac_collect_merchant_text.setBackgroundResource(R.drawable.shape_left_select);
        ac_collect_merchant_text.setTextColor(getResources().getColor(R.color.collectorange));

        ac_collect_good_text.setBackgroundResource(R.drawable.shape_right);
        ac_collect_good_text.setTextColor(getResources().getColor(R.color.white));
        if (fragment_shop == null) {
            fragment_shop = new MerchantCollectFragment();
        }
        switchContent(fragment_shop);
    }

    private void switchContent(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        if (!fragment.isAdded()){
            transaction.add(R.id.ac_collect_fram,fragment);
        }
        transaction.show(fragment);
        transaction.commit();

    }
    private void hideFragments(FragmentTransaction transaction) {
        if (fragment_shop!=null){
            transaction.hide(fragment_shop);
        }
        if (fragment_goods!=null){
            transaction.hide(fragment_goods);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
