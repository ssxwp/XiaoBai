package com.qingyuan.tianya.mrbuy.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.view.slidingmenu.SlidingMenu;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.view.view.CategoryTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物
 */
@SuppressLint("ValidFragment")
public class ShoppingFragment extends BaseFragment{
   private List<Fragment> mFragmentList = new ArrayList<>();

    private CategoryTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private View view;
    private SlidingMenu menu;
    private ViewPagerChangeListener mViewPagerChangeListener;
    private SpecialFragment mSpecialFg;
   // private BrandFragment mBrandFg;
    private CosmeticsFragment mCosmeticsFg;
    private DigitalFragment mDigital;
    private LivingHomeFragment mLivingHomeFg;


    public ShoppingFragment(SlidingMenu menu) {
        this.menu=menu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shopping, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
       /* HeaderView headerView = (HeaderView) getActivity().findViewById(R.id.header_view);
        headerView.getMidTextView().setVisibility(View.VISIBLE);
        headerView.getMidTextView().setText("购物");
        headerView.getHeadBackGround().setBackgroundColor(getActivity().getResources().getColor(R.color.red));
        getActivity().findViewById(R.id.home_head_search).setVisibility(View.GONE);*/
        tabs = (CategoryTabStrip) view.findViewById(R.id.category_strip);
        pager = (ViewPager) view.findViewById(R.id.view_pager);
        adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        mViewPagerChangeListener = new ViewPagerChangeListener();
        pager.addOnPageChangeListener(mViewPagerChangeListener);

    }

    @Override
    public void initonClick() {

    }

    @Override
    public void initData() {
        if (mSpecialFg == null) {
            mSpecialFg = new SpecialFragment();
        }
//        if (mBrandFg == null) {
//            mBrandFg = new BrandFragment();
//        }
        if (mCosmeticsFg==null) {
            mCosmeticsFg = new CosmeticsFragment();
        }
        if (mDigital==null) {
            mDigital = new DigitalFragment();
        }
        if (mLivingHomeFg==null) {
            mLivingHomeFg = new LivingHomeFragment();
        }
        mFragmentList.add(mSpecialFg);
        //mFragmentList.add(mBrandFg);
        mFragmentList.add(mCosmeticsFg);
        mFragmentList.add(mDigital);
        mFragmentList.add(mLivingHomeFg);
    }
    @Override
    public void onClick(View v) {

    }
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final List<String> catalogs = new ArrayList<>();
        private List<Fragment> fragmentlist = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            catalogs.add(getString(R.string.category_hot));
          //  catalogs.add(getString(R.string.category_clothe));
            catalogs.add(getString(R.string.category_women));
            catalogs.add(getString(R.string.category_digital));
            catalogs.add(getString(R.string.category_mmboy));
            this.fragmentlist=mFragmentList;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catalogs.get(position);
        }

        @Override
        public int getCount() {
            return catalogs.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentlist.get(position);
        }

    }
    class ViewPagerChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    menu.setMode(menu.LEFT);
                    menu.setTouchModeAbove(menu.TOUCHMODE_FULLSCREEN);
                    break;
                default:
                    menu.setTouchModeAbove(menu.TOUCHMODE_NONE);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
