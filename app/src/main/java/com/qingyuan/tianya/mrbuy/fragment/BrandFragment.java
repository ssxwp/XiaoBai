package com.qingyuan.tianya.mrbuy.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.AbstractSpinerAdapter;
import com.qingyuan.tianya.mrbuy.view.popuwindow.SpinnerPopuWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 品牌服饰
 */
public class BrandFragment extends BaseFragment implements AbstractSpinerAdapter.IOnItemSelectListener {
    private View view;
    private List<String> nameList = new ArrayList<>();
    private List<String> nameLists = new ArrayList<>();
    private RelativeLayout rl1;
    private ImageView fr_brand_spinner_image;
    private ImageView fr_sort_spinner_image;
    private RelativeLayout rl2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_brand, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        Toast.makeText(getActivity(), "服饰正在开通，敬请期待...", Toast.LENGTH_SHORT).show();
        rl1 = ((RelativeLayout) view.findViewById(R.id.spinner_rl1));
        rl2 = ((RelativeLayout) view.findViewById(R.id.spinner_rl2));
        fr_brand_spinner_image = ((ImageView) view.findViewById(R.id.fr_brand_spinner_image));
        fr_sort_spinner_image = ((ImageView) view.findViewById(R.id.fr_sort_spinner_image));
    }

    @Override
    public void initonClick() {
        fr_brand_spinner_image.setOnClickListener(this);
        fr_sort_spinner_image.setOnClickListener(this);
    }

    @Override
    public void initData() {
        /**
         * spinner数据
         */
        String[] names = getResources().getStringArray(R.array.spinnername);
        Collections.addAll(nameList, names);
        mSpinnerPopuWindow = new SpinnerPopuWindow(getActivity());
        mSpinnerPopuWindow.refreshData(nameList,0);
        mSpinnerPopuWindow.setItemListener(this);

        String[] name = getResources().getStringArray(R.array.sort_name);
        Collections.addAll(nameLists, name);
        mSpinnerPopuWindows = new SpinnerPopuWindow(getActivity());
        mSpinnerPopuWindows.refreshData(nameLists,0);
        mSpinnerPopuWindows.setItemListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fr_brand_spinner_image:
                showPopuWindow(rl1);
                break;
            case R.id.fr_sort_spinner_image:
                showPopuWindows(rl2);
                break;
        }
    }

    private SpinnerPopuWindow mSpinnerPopuWindow;
    private void showPopuWindow(View view) {
        mSpinnerPopuWindow.setWidth(view.getWidth());
        mSpinnerPopuWindow.showAsDropDown(view);
    }
    private SpinnerPopuWindow mSpinnerPopuWindows;
    private void showPopuWindows(View view) {
        mSpinnerPopuWindows.setWidth(view.getWidth());
        mSpinnerPopuWindows.showAsDropDown(view);
    }

    @Override
    public void onItemClick(int pos) {

    }
}
