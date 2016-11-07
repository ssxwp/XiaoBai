package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

public class RegardtyyouActivity extends BaseActivity {
    private ScrollView llview;
    private ImageView iv_tuyou;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regardtuyou);
        addActivity(this);
        initView();
        initData();
        initClick();


    }

    @Override
    public void initView() {
        HeaderView head = ((HeaderView) findViewById(R.id.regaid_tuy));
        head.getHeadBackGround().setBackgroundColor(Color.rgb(233,1,61));
        llview=(ScrollView)findViewById(R.id.ll_tuyou);
        iv_tuyou=(ImageView)findViewById(R.id.iv_tuyou);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initClick() {

    }



}
