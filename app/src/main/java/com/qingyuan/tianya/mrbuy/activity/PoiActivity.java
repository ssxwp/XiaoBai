package com.qingyuan.tianya.mrbuy.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.db.SharedPreferenceHelper;
import com.qingyuan.tianya.mrbuy.utils.AMapUtil;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;


/**
 * 选择城市
 */

public class PoiActivity extends BaseActivity {

    private TextView poi_text1,poi_text2;
    private SharedPreferenceHelper sp;
    private HeaderView header;
    private AMapUtil aMap = null;
    private String locationCity;
    private LinearLayout ll_left_layout;
    private ImageView leftImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        addActivity(this);
        initView();
        initData();
        initClick();


    }

    @Override
    public void initView() {
        sp = new SharedPreferenceHelper(this,"position");
        poi_text1 = (TextView) findViewById(R.id.ac_poi_text1);
        poi_text2 = (TextView) findViewById(R.id.ac_poi_text2);
        header = ((HeaderView) findViewById(R.id.ac_poi_head));
        ll_left_layout=(LinearLayout) header.findViewById(R.id.ll_left_layout);
        aMap = new AMapUtil(this);
        leftImage=(ImageView) header.findViewById(R.id.header_left_arrow);

    }

    @Override
    public void initData() {
        aMap.setOption();
        //aMap.requestLocation();
        aMap.getData(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation!=null){
                    if (aMapLocation.getErrorCode() == 0) {
                        double latitude = aMapLocation.getLatitude();//获取纬度
                        double longitude = aMapLocation.getLongitude();//获取经度
                        toast("纬度" + latitude + "经度"+longitude);
                        aMapLocation.getAccuracy();//获取精度信息
                        locationCity = aMapLocation.getCity();
                        if (StringUtil.isNotEmpty(locationCity)) {
                            poi_text1.setText(locationCity);
                            sp.putValue("position", locationCity);
                            aMap.stop();
                        }
                    }else{
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }

        });
        aMap.start();

        Bundle bundle=getIntent().getExtras();
        if (null != bundle) {
            String home = bundle.getString("home");
            if ("home".equals(home)) {
                ll_left_layout.setVisibility(View.VISIBLE);
            }else{
                ll_left_layout.setVisibility(View.INVISIBLE);
            }
        }else{
            leftImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void initClick() {
        poi_text1.setOnClickListener(this);
        poi_text2.setOnClickListener(this);
        ll_left_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoiActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ac_poi_text1:
                sp.putValue("poi", poi_text1.getText().toString().trim());
                skipActivityForClose(this,HomeActivity.class,null);
                break;
            case R.id.ac_poi_text2:
                sp.putValue("poi", poi_text2.getText().toString().trim());
                skipActivityForClose(this, HomeActivity.class, null);
                break;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        aMap.stop();
        if (aMap.mLocationClient == null) return ;
        if (aMap.mLocationClient.isStarted()){
            aMap.mLocationClient.stopLocation();
        }
    }

    @Override
    public void onDestroy() {
        if (aMap.mLocationClient != null && aMap.mLocationClient.isStarted()) {
            aMap.stop();
            aMap.mLocationClient = null;
        }
        super.onDestroy();
    }

}
