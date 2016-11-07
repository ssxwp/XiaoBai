package com.qingyuan.tianya.mrbuy.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingyuan.tianya.mrbuy.R;

import java.util.ArrayList;

public class GoodsImageActivity extends BaseActivity{
    private static final String TAG = "MutilTouchActivity";
    private Context context;
    private ViewPager mViewPager;
    private ArrayList<String> list_urls;
    private ImageView[] mImageViews;
    /*private int url_position;
    private int current_item;*/
    private TextView indexTextView, sumTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_image);
        Log.e("Main","--------------------------------------------GoodsImageActivity");
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        //url_position = bundle.getInt("mImageUrl");

        list_urls = bundle.getStringArrayList("mImageUrl");
        mImageViews = new ImageView[list_urls.size()];
        context = GoodsImageActivity.this;
        //current_item = url_position + 1;
        indexTextView = (TextView) findViewById(R.id.tv_mia_index);
        sumTextView = (TextView) findViewById(R.id.tv_mia_sum);
        sumTextView.setText("" + list_urls.size());

        indexTextView.setText("" + 1);
        mViewPager = (ViewPager) findViewById(R.id.vp_mia_viewpager);
        //EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getItemPosition(Object object) {
                // TODO Auto-generated method stub
                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                SimpleDraweeView imageView = new SimpleDraweeView(context);
                Uri uri = Uri.parse(list_urls.get(position));
                imageView.setImageURI(uri);


                container.addView(imageView);
                mImageViews[position] = imageView;
                mImageViews[position].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoodsImageActivity.this.finish();
                    }
                });
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // TODO Auto-generated method stub
                container.removeView(mImageViews[position]);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return list_urls.size();
            }
        });

        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                int index = position + 1;
                indexTextView.setText("" + index);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });

    }

    @Override
    public void initClick() {
    }

}
