package com.qingyuan.tianya.mrbuy.fragment;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.activity.GoodsInfoActivity;
import com.qingyuan.tianya.mrbuy.adapter.GoodsAdapters;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.application.MrBuyApplication;
import com.qingyuan.tianya.mrbuy.bean.GoodsBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 数码产品
 */
public class DigitalFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private View view;
    private TextView editon;
    private TextView child,letter,society,buss,succe,science,live;
    private ArrayList<GoodsBean> mList;
    private PullToRefreshListView mListView;
    private ListView mReListView;
    private SimpleDraweeView book_head;
    private LinearLayout ll;
    private String m_id;
    private Drawable drawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_digital, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        ll = (LinearLayout)view.findViewById(R.id.ll);
        book_head = ((SimpleDraweeView) view.findViewById(R.id.book_head));
        editon = ((TextView) view.findViewById(R.id.editon_text));
        child = ((TextView) view.findViewById(R.id.child_text));
        letter = ((TextView) view.findViewById(R.id.letter_text));
        society = ((TextView) view.findViewById(R.id.society_text));
        buss = ((TextView) view.findViewById(R.id.buss_text));
        succe = ((TextView) view.findViewById(R.id.succe_text));
        science = ((TextView) view.findViewById(R.id.science_text));
        live = ((TextView) view.findViewById(R.id.live_text));
        mListView = (PullToRefreshListView)view.findViewById(R.id.book_list);
        mListView.setPullToRefreshEnabled(true);
        mReListView = mListView.getRefreshableView();
        mReListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mReListView.setOnItemClickListener(this);
    }

    @Override
    public void initonClick() {
        editon.setOnClickListener(this);
        child.setOnClickListener(this);
        letter.setOnClickListener(this);
        society.setOnClickListener(this);
        buss.setOnClickListener(this);
        succe.setOnClickListener(this);
        science.setOnClickListener(this);
        live.setOnClickListener(this);
    }

    @Override
    public void initData() {
        m_id = createSharedPreference(getActivity(), "user_custId").getValue("custId");
        Resources resources=getResources();
        drawable=resources.getDrawable(R.drawable.searchview);
        querImage();
        querBook(54);
    }

    private void querImage() {
        String urlString = ApiConstant.ADVER;
        RequestParams params = new RequestParams();
        params.put("a_id", 38 + "");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject cus = jObj.getJSONObject("responseList");
                        String img = cus.getString("m_pic");
                        if (StringUtil.isNotEmpty(img)) {
                            Uri uri = Uri.parse(img);
                            book_head.setImageURI(uri);
                        } else {
                            book_head.setImageResource(R.mipmap.buffer_pic);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }

    private void querBook(int shop_id) {
        initProgressDialog();
        mList = new ArrayList<>();
        String urlString = ApiConstant.GOODSMESSAGE;
        RequestParams params = new RequestParams();
        params.put("shop_id", shop_id + "");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray customer = jObj.getJSONArray("responseList");
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject cus = customer.getJSONObject(i);
                            int goods_id = cus.getInt("goods_id");
                            String goods_name = cus.getString("goods_name");
                            String price = cus.getString("price");
                            String p_score = cus.getString("p_score");
                            String s_price = cus.getString("s_price");
                            String score = cus.getString("score");
                            String ytime = cus.getString("ytime");
                            String remark = cus.getString("remark");
                            String pic_1 = cus.getString("pic_1");
                            int num = 0;
                            GoodsBean goodsBean = new GoodsBean(goods_id, goods_name, price, s_price, p_score, score, ytime, remark, pic_1, num);
                            mList.add(goodsBean);
                        }
                        GoodsAdapters adapter = null;
                        if (adapter == null) {
                            adapter = new GoodsAdapters(MrBuyApplication.getAppContext(), mList, m_id);
                            mReListView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        ll.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    close();
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editon_text:
                editon.setBackgroundColor(Color.RED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    child.setBackground(drawable);
                    letter.setBackground(drawable);
                    society.setBackground(drawable);
                    buss.setBackground(drawable);
                    succe.setBackground(drawable);
                    science.setBackground(drawable);
                    live.setBackground(drawable);
                }
                querBook(54);
                break;
            case R.id.child_text:
                child.setBackgroundColor(Color.RED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editon.setBackground(drawable);
                    letter.setBackground(drawable);
                    society.setBackground(drawable);
                    buss.setBackground(drawable);
                    succe.setBackground(drawable);
                    science.setBackground(drawable);
                    live.setBackground(drawable);
                }
                querBook(54);
                break;
            case R.id.letter_text:
                letter.setBackgroundColor(Color.RED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editon.setBackground(drawable);
                    child.setBackground(drawable);
                    society.setBackground(drawable);
                    buss.setBackground(drawable);
                    succe.setBackground(drawable);
                    science.setBackground(drawable);
                    live.setBackground(drawable);
                }
                querBook(54);
                break;
            case R.id.society_text:
                society.setBackgroundColor(Color.RED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editon.setBackground(drawable);
                    letter.setBackground(drawable);
                    child.setBackground(drawable);
                    buss.setBackground(drawable);
                    succe.setBackground(drawable);
                    science.setBackground(drawable);
                    live.setBackground(drawable);
                }
                querBook(54);
                break;
            case R.id.buss_text:
                buss.setBackgroundColor(Color.RED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editon.setBackground(drawable);
                    letter.setBackground(drawable);
                    society.setBackground(drawable);
                    child.setBackground(drawable);
                    succe.setBackground(drawable);
                    science.setBackground(drawable);
                    live.setBackground(drawable);
                }
                querBook(54);
                break;
            case R.id.succe_text:
                succe.setBackgroundColor(Color.RED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editon.setBackground(drawable);
                    letter.setBackground(drawable);
                    society.setBackground(drawable);
                    buss.setBackground(drawable);
                    child.setBackground(drawable);
                    science.setBackground(drawable);
                    live.setBackground(drawable);
                }
                querBook(54);
                break;
            case R.id.science_text:
                science.setBackgroundColor(Color.RED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editon.setBackground(drawable);
                    letter.setBackground(drawable);
                    society.setBackground(drawable);
                    buss.setBackground(drawable);
                    succe.setBackground(drawable);
                    child.setBackground(drawable);
                    live.setBackground(drawable);
                }
                querBook(54);
                break;
            case R.id.live_text:
                live.setBackgroundColor(Color.RED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editon.setBackground(drawable);
                    letter.setBackground(drawable);
                    society.setBackground(drawable);
                    buss.setBackground(drawable);
                    succe.setBackground(drawable);
                    science.setBackground(drawable);
                    child.setBackground(drawable);
                }
                querBook(54);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putInt("info",mList.get(position).getGoods_id());
        skipActivityforClass(getActivity(), GoodsInfoActivity.class,bundle);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
