package com.qingyuan.tianya.mrbuy.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 今日特卖
 */
public class SpecialFragment extends BaseFragment{

    private View view;
    private SimpleDraweeView head_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_special, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        head_image = ((SimpleDraweeView) view.findViewById(R.id.fr_special_head_image));
    }

    @Override
    public void initonClick() {

    }

    @Override
    public void initData() {
        querImage();
    }

    @Override
    public void onClick(View v) {

    }
    private void querImage() {
        String urlString = ApiConstant.ADVER;
        RequestParams params = new RequestParams();
        params.put("a_id", 36 + "");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject cus = jObj.getJSONObject("responseList");
                        String img = cus.getString("m_pic");
                        if (StringUtil.isNotEmpty(img)) {
                            Uri uri = Uri.parse(img);
                            head_image.setImageURI(uri);
                        } else {
                            head_image.setImageResource(R.mipmap.buffer_pic);
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
}
