package com.qingyuan.tianya.mrbuy.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GoodsInfoActivity extends BaseActivity {
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private String[] list_urls;

    private TextView priceTextView;
    private WebView contenTextView;
    private SimpleDraweeView mAdView;
    private int goods_id;
    private TextView produce_price;
    private int shop_id;
    private String m_id;
    private boolean isCollect;
    private ImageView imageView;
    private TextView sumTextView;
    private ImageView shop;
    private String cate_1;
    private ImageView addcart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        Log.e("Main","--------------------------------------------GoodsInfoActivity");
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        mAdView = (SimpleDraweeView) findViewById(R.id.imageCycleView_productDetail);
        priceTextView = (TextView) findViewById(R.id.tv_pda_productName);
        contenTextView = (WebView) findViewById(R.id.tv_pda_content);
        produce_price = (TextView) findViewById(R.id.produce_price);
        imageView = ((ImageView) findViewById(R.id.tv_pda_collect));
        sumTextView = (TextView) findViewById(R.id.tv_mia_sum);
        shop = (ImageView) findViewById(R.id.shop);
        addcart = ((ImageView) findViewById(R.id.addcart));

    }

    @Override
    public void initData() {
        m_id = createSharedPreference(this, "user_custId").getValue("custId");
        Bundle bundle = getIntent().getExtras();
        goods_id = bundle.getInt("info");
        isCollect();
        getGoods();
    }

    @Override
    public void initClick() {
        mAdView.setOnClickListener(this);
        addcart.setOnClickListener(this);
        imageView.setOnClickListener(this);
        shop.setOnClickListener(this);
    }

    private void getGoods() {
        initProgressDialog();
        String urlString = ApiConstant.GOODSINFO;
        RequestParams params = new RequestParams();
        params.put("goods_id", goods_id + "");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject cus = jObj.getJSONObject("responseList");
                        //int goods_id = cus.getInt("goods_id");
                        shop_id = cus.getInt("shop_id");
                        String goods_name = cus.getString("goods_name");
                        String price = cus.getString("price");
                        //String p_score = cus.getString("p_score");
                        //String s_price = cus.getString("s_price");
                        //String score = cus.getString("score");
                        //String ytime = cus.getString("ytime");
                        String remark = cus.getString("remark");
                        String pic_1 = cus.getString("pic_1");
                        String pic_2 = cus.getString("pic_2");
                        String pic_3 = cus.getString("pic_3");
                        String pic_4 = cus.getString("pic_4");
                        String pic_5 = cus.getString("pic_5");
                        String pic_6 = cus.getString("pic_6");
                        cate_1 = cus.getString("cate_1");
                        priceTextView.setText(goods_name);
                        if (StringUtil.isNotEmpty(remark)) {
                            contenTextView.setVisibility(View.VISIBLE);
                            contenTextView.loadDataWithBaseURL(null, remark, "text/html", "utf-8", null);
                        }
                        produce_price.setText("￥" + price);

                        if (StringUtil.isNotEmpty(pic_1)) {
                            Uri uri = Uri.parse(pic_1);
                            mAdView.setImageURI(uri);
                            mImageUrl.add(pic_1);
                        }
                        if (StringUtil.isNotEmpty(pic_2)) {
                            mImageUrl.add(pic_2);
                        }
                        if (StringUtil.isNotEmpty(pic_3)) {
                            mImageUrl.add(pic_3);
                        }
                        if (StringUtil.isNotEmpty(pic_4)) {
                            mImageUrl.add(pic_4);
                        }
                        if (StringUtil.isNotEmpty(pic_5)) {
                            mImageUrl.add(pic_5);
                        }
                        if (StringUtil.isNotEmpty(pic_6)) {
                            mImageUrl.add(pic_6);
                        }
                        sumTextView.setText(mImageUrl.size() + "");
                        if (cate_1.equals("1")||cate_1.equals("16")||cate_1.equals("27")) {
                            addcart.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(GoodsInfoActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageCycleView_productDetail:
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("mImageUrl",mImageUrl);
                skipActivity(GoodsInfoActivity.this,GoodsImageActivity.class,bundle);
                break;
            case R.id.addcart:
                addCar();
                break;
            case R.id.tv_pda_collect:
                if (isCollect) {
                    isCollect = false;
                    collectShop(ApiConstant.DELCOLLECT);
                }else {
                    isCollect =true;
                    collectShop(ApiConstant.ADDCOLLECT);
                }
                break;
            case R.id.shop:
                switch (cate_1) {
                    case "1":
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("sort", "dish");
                        bundle1.putInt("shop_id", shop_id);
                        skipActivity(GoodsInfoActivity.this, MerchantMessageActivity.class, bundle1);
                        break;
                    case "16":
                    case "27":
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("sort", "fun");
                        bundle2.putInt("shop_id", shop_id);
                        skipActivity(GoodsInfoActivity.this, MerchantMessageActivity.class, bundle2);
                        break;
                    case "28":
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("from", "info");
                        skipActivity(GoodsInfoActivity.this, HomeActivity.class, bundle3);
                        break;
                    default:
                        Bundle bundle4 = new Bundle();
                        bundle4.putString("from", "info1");
                        skipActivity(GoodsInfoActivity.this, HomeActivity.class, bundle4);
                        break;
                }
                break;
        }
    }


    private void addCar() {
        initProgressDialog();
        String urlString = ApiConstant.ADDCAR;
        RequestParams params = new RequestParams();
        params.put("goods_id",goods_id+"");
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        Toast.makeText(GoodsInfoActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GoodsInfoActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                close();
            }

            public void onFailure(Throwable arg0) { // 失败，调用
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }
    /**
     * 添加收藏
     */
    private void collectShop(final String urlString) {
        //loadingDialog = createLoadingDialog(context,"正在加载...");
        final RequestParams params = new RequestParams();
        params.put("goods_id",goods_id+"");
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (urlString.equals(ApiConstant.ADDCOLLECT)) {
                        if (jObj.getString("flag").equals("success")) {
                            imageView.setImageResource(R.mipmap.collection_red_2);
                            Toast.makeText(GoodsInfoActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GoodsInfoActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }else if (urlString.equals(ApiConstant.DELCOLLECT)){
                        if (jObj.getString("flag").equals("success")) {
                            imageView.setImageResource(R.mipmap.collection_red);
                            Toast.makeText(GoodsInfoActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GoodsInfoActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    // loadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用

            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                //loadingDialog.dismiss();
            }
        });
    }
    private void isCollect() {
        String urlString = ApiConstant.ISCOLLECT;
        RequestParams params = new RequestParams();
        params.put("goods_id",goods_id+"");
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        isCollect = true;
                        imageView.setImageResource(R.mipmap.collection_red_2);
                        //Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        isCollect = false;
                        imageView.setImageResource(R.mipmap.collection_red);
                        //Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    //close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
            }
        });
    }
}
