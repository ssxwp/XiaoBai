package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qingyuan.tianya.mrbuy.R;



public class  PersonActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"iconfont/iconfont.ttf");
        ((TextView) findViewById(R.id.ac_person_mycall_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_person_vip_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_person_mysell_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_person_mybuy_img)).setTypeface(iconfont);
        ((TextView) findViewById(R.id.ac_person_mylove_img)).setTypeface(iconfont);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initClick() {
        findViewById(R.id.ac_person_mycall_rel).setOnClickListener(this);
        findViewById(R.id.ac_person_mysell_rel).setOnClickListener(this);
        findViewById(R.id.ac_person_mybuy_rel).setOnClickListener(this);
        findViewById(R.id.ac_person_mylove_rel).setOnClickListener(this);
        findViewById(R.id.ac_person_back).setOnClickListener(this);
        findViewById(R.id.ac_person_edit).setOnClickListener(this);
        findViewById(R.id.ac_person_vip_img).setOnClickListener(this);
        findViewById(R.id.ac_person_vip_text).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ac_person_mycall_rel:

                break;
            case R.id.ac_person_mysell_rel:

                break;
            case R.id.ac_person_mybuy_rel:

                break;
            case R.id.ac_person_mylove_rel:

                break;
            case R.id.ac_person_back:
                finish();
                break;
            case R.id.ac_person_edit:
                skipActivity(this, PersonalInfoActivity.class, null);
                break;
            case R.id.ac_person_vip_img:
                /*Intent intent = new Intent(this,HomeActivity.class);
                Bundle bundle = new Bundle();
                startActivity(intent);*/
                skipActivity(this, VipActivity.class, null);
                break;
            case R.id.ac_person_vip_text:
                skipActivity(this,VipActivity.class,null);
                break;
        }
    }
}
