package com.qingyuan.tianya.mrbuy.fragment;


import android.annotation.TargetApi;
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
 * 化妆品
 */
public class CosmeticsFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private View view;
    private LinearLayout ll;
    private SimpleDraweeView cosmetics_head;
    private TextView face,hair,body,mouth,women,perfume;
    private ArrayList<GoodsBean> mList;
    private ListView mReListView;
    private String m_id;
    private Drawable drawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cosmetics, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        ll = (LinearLayout)view.findViewById(R.id.cos_ll);
        cosmetics_head = ((SimpleDraweeView) view.findViewById(R.id.cosmetics_head));
        face = ((TextView) view.findViewById(R.id.face_text));
        hair = ((TextView) view.findViewById(R.id.hair_text));
        body = ((TextView) view.findViewById(R.id.body_text));
        mouth = ((TextView) view.findViewById(R.id.mouth_text));
        women = ((TextView) view.findViewById(R.id.women_text));
        perfume = ((TextView) view.findViewById(R.id.perfume_text));
        PullToRefreshListView mListView = (PullToRefreshListView) view.findViewById(R.id.cos_list);
        mListView.setPullToRefreshEnabled(true);
        mReListView = mListView.getRefreshableView();
        mReListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mReListView.setOnItemClickListener(this);
    }

    @Override
    public void initonClick() {
        face.setOnClickListener(this);
        hair.setOnClickListener(this);
        body.setOnClickListener(this);
        mouth.setOnClickListener(this);
        women.setOnClickListener(this);
        perfume.setOnClickListener(this);
    }

    @Override
    public void initData() {
        m_id = createSharedPreference(getActivity(), "user_custId").getValue("custId");
        Resources resources=getResources();
        drawable=resources.getDrawable(R.drawable.searchview);
        queryImage();
        queryGoods(38);
    }

    private void queryGoods(int shop_id) {
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
                        GoodsAdapters adapter = new GoodsAdapters(MrBuyApplication.getAppContext(), mList, m_id);
                        mReListView.setAdapter(adapter);
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

    private void queryImage() {
        String urlString = ApiConstant.ADVER;
        RequestParams params = new RequestParams();
        params.put("a_id", 37 + "");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject cus = jObj.getJSONObject("responseList");
                        String img = cus.getString("m_pic");
                        if (StringUtil.isNotEmpty(img)) {
                            Uri uri = Uri.parse(img);
                            cosmetics_head.setImageURI(uri);
                        } else {
                            cosmetics_head.setImageResource(R.mipmap.buffer_pic);
                        }
                    } /*else {
                        Toast.makeText(FoodMerchantActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }*/
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.face_text:
                face.setBackgroundColor(Color.RED);
                    hair.setBackground(drawable);
                    body.setBackground(drawable);
                    mouth.setBackground(drawable);
                    women.setBackground(drawable);
                    perfume.setBackground(drawable);
                queryGoods(38);
                break;
            case R.id.hair_text:

                hair.setBackgroundColor(Color.RED);
                    face.setBackground(drawable);
                    body.setBackground(drawable);
                    mouth.setBackground(drawable);
                    women.setBackground(drawable);
                    perfume.setBackground(drawable);
                queryGoods(38);
                break;
            case R.id.body_text:
                    face.setBackground(drawable);
                    hair.setBackground(drawable);
                    mouth.setBackground(drawable);
                    women.setBackground(drawable);
                    perfume.setBackground(drawable);
                body.setBackgroundColor(Color.RED);
                queryGoods(38);
                break;
            case R.id.mouth_text:
                face.setBackground(drawable);
                hair.setBackground(drawable);
                body.setBackground(drawable);
                mouth.setBackgroundColor(Color.RED);
                women.setBackground(drawable);
                perfume.setBackground(drawable);
                queryGoods(38);
                break;
            case R.id.women_text:
                face.setBackground(drawable);
                hair.setBackground(drawable);
                body.setBackground(drawable);
                mouth.setBackground(drawable);
                women.setBackgroundColor(Color.RED);
                perfume.setBackground(drawable);
                queryGoods(38);
                break;
            case R.id.perfume_text:
                face.setBackground(drawable);
                hair.setBackground(drawable);
                body.setBackground(drawable);
                mouth.setBackground(drawable);
                women.setBackground(drawable);
                perfume.setBackgroundColor(Color.RED);
                queryGoods(38);
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
