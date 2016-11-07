package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qingyuan.tianya.mrbuy.R;

/**
 * 钱包
 */
public class MoneyActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        //图标
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"iconfont/iconfont.ttf");
        ((TextView) findViewById(R.id.ac_money_lost_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_back_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_save_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_scored_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_money_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_discount_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_lottery_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_charge_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_vip_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_recharge_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_money_backac_img)).setTypeface(iconfont);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initClick() {
        findViewById(R.id.ac_money_backac_rel).setOnClickListener(this);
        findViewById(R.id.ac_money_back_rel).setOnClickListener(this);
        findViewById(R.id.ac_money_save_rel).setOnClickListener(this);
        findViewById(R.id.ac_money_score_rel).setOnClickListener(this);
        findViewById(R.id.ac_money_money_rel).setOnClickListener(this);
        findViewById(R.id.ac_money_discount_rel).setOnClickListener(this);
        findViewById(R.id.ac_money_lottery_rel).setOnClickListener(this);
        findViewById(R.id.ac_money_charge_rel).setOnClickListener(this);
        findViewById(R.id.ac_money_vipcharge_rel).setOnClickListener(this);
        findViewById(R.id.ac_money_recharge_rel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ac_money_backac_rel:

                break;
            case R.id.ac_money_back_rel:

                break;
            case R.id.ac_money_save_rel:

                break;
            case R.id.ac_money_score_rel:

                break;
            case R.id.ac_money_money_rel:

                break;
            case R.id.ac_money_discount_rel:

                break;
            case R.id.ac_money_lottery_rel:

                break;
            case R.id.ac_money_charge_rel:

                break;
            case R.id.ac_money_vipcharge_rel:

                break;
            case R.id.ac_money_recharge_rel:

                break;
        }
    }
}
