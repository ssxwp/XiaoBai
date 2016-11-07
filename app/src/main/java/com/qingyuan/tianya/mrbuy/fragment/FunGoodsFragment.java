package com.qingyuan.tianya.mrbuy.fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.adapter.FunGoodsAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.GoodsBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.pulltorefresh.PullToRefreshListView;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 娱乐项目
 */
@SuppressLint("ValidFragment")
public class FunGoodsFragment extends BaseFragment {
    private int shop_id;
    private PullToRefreshListView mListView;
    private ListView mReListView;
    private ArrayList<GoodsBean> mList = new ArrayList<>();
    private SimpleDraweeView mImage;
    private TextView mTv_name,mTv_address,mTv_number,mTv_money;
    private HeaderView headerView;
    //private ImageView mImg_collect;
    private String m_id;
    private boolean isCollect;
    private ImageView rightPic;
    private ImageView mImg_poi;
    private RelativeLayout fr_mer_rel;
    private TextView mTv_p_money;
    private TextView mTv_q_money;

    private View view;
    private RelativeLayout rel;

    public FunGoodsFragment(int shop_id) {
        this.shop_id = shop_id;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fun_goods, container, false);
        initView();
        initData();
        initonClick();
        return view;
    }

    @Override
    public void initView() {
        mListView = ((PullToRefreshListView) view.findViewById(R.id.fr_fun_list));
        mListView.setPullToRefreshEnabled(true);
        mReListView = mListView.getRefreshableView();
        mReListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mImage = ((SimpleDraweeView) view.findViewById(R.id.fr_dishmerchant_image));
        //mTv_name = ((TextView) view.findViewById(R.id.fr_dishmerchant_name));
        mTv_address= ((TextView) view.findViewById(R.id.fr_dishmerchant_address));
        mTv_money= ((TextView) view.findViewById(R.id.goods_money_text));
        mTv_p_money= ((TextView) view.findViewById(R.id.fr_merchant_price_text1));
        mTv_q_money= ((TextView) view.findViewById(R.id.fr_merchant_price_text2));
        rel = (RelativeLayout)view.findViewById(R.id.rel);
        // mImg_poi= ((ImageView) view.findViewById(R.id.fr_dish_poi_imag));
        headerView = (HeaderView) view.findViewById(R.id.header_view);
        headerView.getHeadBackGround().setBackgroundColor(Color.rgb(254,97,0));
        rightPic = headerView.getRightPic();
        rightPic.setImageResource(R.mipmap.collection);
    }

    @Override
    public void initonClick() {
        rightPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollect) {
                    collectShop(ApiConstant.DELCOLLECTION);
                } else {
                    collectShop(ApiConstant.ADDCOLLECTION);
                }
            }
        });
    }

    @Override
    public void initData() {
        m_id = createSharedPreference(getActivity(), "user_custId").getValue("custId");
        //queryIsCollect();
        getMerchantMessage();
    }

    private void queryIsCollect() {
        String urlString = ApiConstant.ADDCAR;
        RequestParams params = new RequestParams();
        //params.put("goods_id",good_id+"");
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        isCollect = true;
                        rightPic.setImageResource(R.mipmap.collection);
                        //Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        isCollect = false;
                        rightPic.setImageResource(R.mipmap.collection_2);
                        //Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    //close();
                } catch (JSONException e) {
                    e.printStackTrace();
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

    /**
     * 商家商品信息
     */
    private void getGoodsMessage() {
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
                            GoodsBean goodsBean = new GoodsBean(goods_id, goods_name, price, s_price, p_score, score, ytime, remark, pic_1,num);
                            mList.add(goodsBean);
                        }
                        FunGoodsAdapter adapter = null;
                        if (adapter == null) {
                            adapter = new FunGoodsAdapter(getActivity().getApplicationContext(), mList,m_id);
                            mReListView.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(mReListView);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        mListView.onRefreshComplete();
                        mListView.scrollTo(0, 50);
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

    /**
     * 商家信息
     */
    private void getMerchantMessage() {
        initProgressDialog();
        String urlString = ApiConstant.SHOPMESSAGE;
        RequestParams params = new RequestParams();
        params.put("shop_id", shop_id+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONObject customer = jObj.getJSONObject("responseList");
                        int shop_id = customer.getInt("shop_id");
                        String img = customer.getString("head_pic");
                        String name = customer.getString("shop_name");
                        String province = customer.getString("province");
                        String city = customer.getString("city");
                        String district = customer.getString("district");
                        String p_price = customer.getString("p_price");
                        String q_price = customer.getString("q_price");
                        String status = customer.getString("status");
                        if (StringUtil.isNotEmpty(img)) {
                            Uri uri = Uri.parse(img);
                            mImage.setImageURI(uri);
                        }else{
                            mImage.setImageResource(R.mipmap.buffer_pic);
                        }
                        mTv_p_money.setText("均价：￥"+q_price);
                        mTv_q_money.setText("销量：￥"+p_price);
                        //mTv_name.setText(name);
                        //mImg_poi.setImageResource(R.mipmap.address_03);
                        mTv_address.setText(province + "" + city + "" + district);
                        headerView.getMidTextView().setText(name);
                        rel.setVisibility(View.VISIBLE);
                        getGoodsMessage();
                        //mImg_collect.setImageResource(R.mipmap.collection_red);
                    } else {
                        Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.i("erro", arg0.toString());
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                //close();
            }
        });
    }



    @Override
    public void onClick(View v) {

    }

    /**
     * 添加收藏
     */
    private void collectShop(final String urlString) {
        RequestParams params = new RequestParams();
        params.put("shop_id",shop_id+"");
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.toString().trim());
                    if (urlString.equals(ApiConstant.ADDCOLLECTION)) {
                        if (jObj.getString("flag").equals("success")) {
                            isCollect = true;
                            rightPic.setImageResource(R.mipmap.collection);
                            Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } else if (urlString.equals(ApiConstant.DELCOLLECTION)) {
                        if (jObj.getString("flag").equals("success")) {
                            isCollect = false;
                            rightPic.setImageResource(R.mipmap.collection_2);
                            Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    //close();
                } catch (JSONException e) {
                    e.printStackTrace();
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
    /**
     * 解决scrollview嵌套listview问题
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mList!=null){
            mList.clear();
        }
    }
}
