package com.qingyuan.tianya.mrbuy.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingyuan.tianya.mrbuy.R;

/**
 * 超市
 */
public class MarketFragment extends BaseFragment {


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_market, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
       /* HeaderView headerView = (HeaderView) getActivity().findViewById(R.id.header_view);
        headerView.getMidTextView().setVisibility(View.VISIBLE);
        headerView.getMidTextView().setText("超市");
        getActivity().findViewById(R.id.home_head_search).setVisibility(View.GONE);*/
    }

    @Override
    public void initonClick() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
