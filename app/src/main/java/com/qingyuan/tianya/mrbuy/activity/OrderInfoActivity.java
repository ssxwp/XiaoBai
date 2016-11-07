package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.fragment.CancelOrderFragment;
import com.qingyuan.tianya.mrbuy.fragment.FinishOrderFragment;
import com.qingyuan.tianya.mrbuy.fragment.UnfinishOrderFragment;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

/**
 * 订单
 */
public class OrderInfoActivity extends BaseActivity {

    private TextView unfinish;
    private TextView finish;
    private TextView cancel;
    private Fragment fragment_unfinish;
    private Fragment fragment_finish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        HeaderView head = ((HeaderView) findViewById(R.id.ac_order_head));
        head.getHeadBackGround().setBackgroundColor(Color.rgb(38,43,58));
        unfinish = ((TextView) findViewById(R.id.order_unfinish_text));
        finish = ((TextView) findViewById(R.id.order_finish_text));
        //cancel = ((TextView) findViewById(R.id.order_cancel_text));
      // lv_order=(ListView)findViewById(R.id.order_list);
    }

    @Override
    public void initData() {

        if (fragment_unfinish == null) {
            fragment_unfinish = new UnfinishOrderFragment();
        }
        switchContent(fragment_unfinish);
    }

    @Override
    public void initClick() {
        unfinish.setOnClickListener(this);
        finish.setOnClickListener(this);
       // cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_unfinish_text:
                getUnfinishOrder();
                break;
            case R.id.order_finish_text:
                getFinishOrder();
                break;
//            case R.id.order_cancel_text:
//                getCancelOrder();
//                break;
        }
    }

    private void getCancelOrder() {
        cancel.setBackgroundResource(R.drawable.shape_right_select);
        cancel.setTextColor(getResources().getColor(R.color.lightblued));

        unfinish.setBackgroundResource(R.drawable.shape_left);
        unfinish.setTextColor(getResources().getColor(R.color.white));
        finish.setBackgroundResource(R.drawable.shape_middle);
        finish.setTextColor(getResources().getColor(R.color.white));

//        if (fragment_cancel==null) {
//            fragment_cancel = new CancelOrderFragment();
//        }
//        switchContent(fragment_cancel);
    }

    private void getFinishOrder() {
        finish.setBackgroundResource(R.drawable.shape_middle_select);
        finish.setTextColor(getResources().getColor(R.color.lightblued));

        unfinish.setBackgroundResource(R.drawable.shape_left);
        unfinish.setTextColor(getResources().getColor(R.color.white));
//        cancel.setBackgroundResource(R.drawable.shape_right);
//        cancel.setTextColor(getResources().getColor(R.color.white));
        if (fragment_finish==null) {
            fragment_finish = new FinishOrderFragment();
        }
        switchContent(fragment_finish);
    }

    private void getUnfinishOrder() {
        unfinish.setBackgroundResource(R.drawable.shape_left_select);
        unfinish.setTextColor(getResources().getColor(R.color.lightblued));

        finish.setBackgroundResource(R.drawable.shape_middle);
        finish.setTextColor(getResources().getColor(R.color.white));
//        cancel.setBackgroundResource(R.drawable.shape_right);
//        cancel.setTextColor(getResources().getColor(R.color.white));
        if (fragment_unfinish==null) {
            fragment_unfinish = new UnfinishOrderFragment();
        }
        switchContent(fragment_unfinish);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (fragment_unfinish!=null){
            transaction.hide(fragment_unfinish);
        }
        if (fragment_finish!=null){
            transaction.hide(fragment_finish);
        }
//        if (fragment_cancel!=null){
//            transaction.hide(fragment_cancel);
//        }

    }
    public void switchContent( Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragments(ft);
        if (!fragment.isAdded()) {
            ft.add(R.id.ac_order_fram, fragment);
        }
        ft.show(fragment);
        ft.commit();
    }
}
