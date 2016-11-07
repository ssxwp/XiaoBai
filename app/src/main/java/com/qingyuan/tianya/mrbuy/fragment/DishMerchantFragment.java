package com.qingyuan.tianya.mrbuy.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.qingyuan.tianya.mrbuy.activity.AddressActivity;
import com.qingyuan.tianya.mrbuy.activity.LoginActivity;
import com.qingyuan.tianya.mrbuy.activity.OrderActivity;
import com.qingyuan.tianya.mrbuy.adapter.CateAdapter;
import com.qingyuan.tianya.mrbuy.adapter.GoodsAdapter;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.application.MrBuyApplication;
import com.qingyuan.tianya.mrbuy.bean.CateBean;
import com.qingyuan.tianya.mrbuy.bean.GoodsBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.pulltorefresh.PullToRefreshListView;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 商家详细信息
 */
@SuppressLint("ValidFragment")
public class DishMerchantFragment extends BaseFragment {


    private View view;
    private int shop_id;
    private PullToRefreshListView mListView;
    private ListView mReListView;
    private ListView cateListView;
    private ArrayList<GoodsBean> mList;
    private ArrayList<CateBean> cateList = new ArrayList<>();
    private SimpleDraweeView mImage;
    private TextView mTv_name,mTv_address,mTv_number,mTv_money;
    private HeaderView headerView;
    //private ImageView mImg_collect;
    private String m_id;
    private boolean isCollect;
    private ImageView rightPic;
    private ImageView mImg_poi;
    private StringBuilder good_id = new StringBuilder();
    private StringBuilder good_num = new StringBuilder();
    private RelativeLayout fr_mer_rel;
    private int number=0;
    private Double money = 0.0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 30:
                    getTotalPrice();
                    break;
            }
        }
    };
    private TextView mTv_p_money;
    private TextView mTv_q_money;
    private RelativeLayout rel;
    private ImageView right;
    private String phone;
    private GoodsAdapter adapter;


    public DishMerchantFragment(int shop_id) {
        this.shop_id = shop_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dish_merchant, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initonClick();
    }

    @Override
    public void initView() {
        cateListView = (ListView)view.findViewById(R.id.sort_listview);
        mListView = ((PullToRefreshListView) view.findViewById(R.id.fr_merchant_list));
        mListView.setPullToRefreshEnabled(true);
        mReListView = mListView.getRefreshableView();
//        //
//        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
//        params.topMargin=20;
//        mReListView.setLayoutParams(params);

        mReListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mImage = ((SimpleDraweeView) view.findViewById(R.id.fr_dishmerchant_image));
        fr_mer_rel = (RelativeLayout)view.findViewById(R.id.fr_mer_rel);
        rel = (RelativeLayout)view.findViewById(R.id.rel);
        //mTv_name = ((TextView) view.findViewById(R.id.fr_dishmerchant_name));
        mTv_address= ((TextView) view.findViewById(R.id.fr_dishmerchant_address));
        mTv_number= ((TextView) view.findViewById(R.id.goods_number_text));
        mTv_money= ((TextView) view.findViewById(R.id.goods_money_text));
        mTv_p_money= ((TextView) view.findViewById(R.id.fr_merchant_price_text1));
        mTv_q_money= ((TextView) view.findViewById(R.id.fr_merchant_price_text2));
        //mImg_poi= ((ImageView) view.findViewById(R.id.fr_dish_poi_imag));
        headerView = (HeaderView) view.findViewById(R.id.header_view);
        headerView.getHeadBackGround().setBackgroundColor(Color.rgb(254,97,0));
        right = headerView.getRightPic();
        rightPic = (ImageView)view.findViewById(R.id.right_pic);
        right.setImageResource(R.mipmap.phone);
        //rightPic.setImageResource(R.mipmap.collect_1);
        cateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getGoodsMessage(cateList.get(position).getCate_id());
                cateListView.setSelected(true);
            }
        });
    }

    @Override
    public void initonClick() {
        view.findViewById(R.id.goods_pay_button).setOnClickListener(this);
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
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+phone));
                //方法的内部会自动为intent对象设置类别:android.intent.category.DEFAULT
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        m_id = createSharedPreference(getActivity(), "user_custId").getValue("custId");
        queryIsCollect();
        getMerchantMessage();
        getSort();
    }

    private void getSort() {
        String urlString = ApiConstant.CATES;
        RequestParams params = new RequestParams();
        params.put("cate_id", 35 + "");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        JSONArray customer = jObj.getJSONArray("responseList");
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject cus = customer.getJSONObject(i);
                            int cate_id = cus.getInt("cate_id");
                            String cate_name = cus.getString("cate_name");
                            CateBean goodsBean = new CateBean(cate_id, cate_name);
                            cateList.add(goodsBean);
                        }
                        CateAdapter adapter = null;
                        if (adapter == null) {
                            adapter = new CateAdapter(getActivity(), cateList);
                            cateListView.setAdapter(adapter);
                            //setListViewHeightBasedOnChildren(mReListView);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
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

    private void queryIsCollect() {
        String urlString = ApiConstant.ISCOLLECTION;
        RequestParams params = new RequestParams();
        params.put("shop_id",shop_id+"");
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        isCollect = true;
                        rightPic.setImageResource(R.mipmap.collect_1);
                        //Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        isCollect = false;
                        rightPic.setImageResource(R.mipmap.collect_3);
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
    private void getGoodsMessage(int cate_id) {
        //initProgressDialog();
        mList = new ArrayList<>();
        String urlString = ApiConstant.GOODSMESSAGE;
        RequestParams params = new RequestParams();
        params.put("shop_id", shop_id + "");
        if (cate_id!=0){
            params.put("cate_id",cate_id+"");
        }
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
                        if (adapter == null) {
                            adapter = new GoodsAdapter(MrBuyApplication.getAppContext(), mList,m_id,mHandler);
                            mReListView.setAdapter(adapter);
                            //setListViewHeightBasedOnChildren(mReListView);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        cateListView.setVisibility(View.VISIBLE);
                        fr_mer_rel.setVisibility(View.VISIBLE);
                        mListView.onRefreshComplete();
                        mListView.scrollTo(0, 50);
                    } else {
                        Toast.makeText(MrBuyApplication.getAppContext(), "商家还没添加商品", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
                close();
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
                        phone = customer.getString("phone");
                        if (StringUtil.isNotEmpty(img)) {
                            Uri uri = Uri.parse(img);
                            mImage.setImageURI(uri);
                        }else{
                            mImage.setImageResource(R.mipmap.buffer_pic);
                        }
                        mTv_p_money.setText("起送价：￥"+q_price);
                        mTv_q_money.setText("配送费：￥"+p_price);
                        //mTv_name.setText(name);
                        //mImg_poi.setImageResource(R.mipmap.address_03);
                        mTv_address.setText(province + "" + city + "" + district);
                        headerView.getMidTextView().setText(name);
                        rel.setVisibility(View.VISIBLE);
                        getGoodsMessage(0);
                        //mImg_collect.setImageResource(R.mipmap.collection_red);
                    } else {
                        Toast.makeText(MrBuyApplication.getAppContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
                close();
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.i("erro", arg0.toString());
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        m_id =createSharedPreference(getActivity(), "user_custId").getValue("custId");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goods_pay_button:
                if (StringUtil.isNotEmpty(m_id)) {
                    if (StringUtil.isNotEmpty(getNum())) {
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", getGoods_id());
                        bundle.putString("goods_num", getNum());
                        skipActivityforClass(getActivity(), AddressActivity.class, bundle);
                    } else {
                        Toast.makeText(getActivity(), "请选择商品", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("from","mer");
                    skipActivityforClass(getActivity(), LoginActivity.class, bundle);
                }
                //setOrder();
                break;
        }
    }

    /**
     * 添加收藏
     */
    private void collectShop(final String urlString) {
        Log.e("Main","-------------------------------------点击收藏，上传收藏/取消收藏");
        RequestParams params = new RequestParams();
        params.put("shop_id",shop_id+"");
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    Log.e("Main","-------------------------------------上传收藏成功");
                    JSONObject jObj = new JSONObject(response.trim());
                    if (urlString.equals(ApiConstant.ADDCOLLECTION)) {
                        Log.e("Main","-------------------------------------添加收藏1:"+jObj.getString("flag"));
                        if (jObj.getString("flag").equals("success")) {
                            Log.e("Main","-------------------------------------添加收藏2");
                            isCollect = true;
                            rightPic.setImageResource(R.mipmap.collect_1);
                            Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } else if (urlString.equals(ApiConstant.DELCOLLECTION)) {
                        Log.e("Main","-------------------------------------取消收藏1"+jObj.getString("flag"));
                        if (jObj.getString("flag").equals("success")) {
                            Log.e("Main","-------------------------------------取消收藏2");
                            isCollect = false;
                            rightPic.setImageResource(R.mipmap.collect_3);
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
                Log.e("Main","-------------------------------------上传收藏失败");
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }
    /**
     * 创建订单
     */
    private void setOrder() {
        initProgressDialog();
        String urlString = ApiConstant.ADDORDER;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id);
        params.put("goods_id", getGoods_id());
        params.put("goods_nub", getNum());
        HttpUtil.post(urlString, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("o_id", jObj.getString("message"));
                        skipActivityforClass(getActivity(), OrderActivity.class, bundle);
                        close();
                    } else {
                        Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        close();
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

    /**
     * 拼接商品id
     * @return
     */
    public String getGoods_id(){
        good_id.replace(0, good_id.length(), "");
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getNum()>0){
                //money+=(mList.get(i).getNum()*Integer.valueOf(mList.get(i).getPrice()));
                if (StringUtil.isNotEmpty(good_id.toString().trim())) {
                    good_id.append("/").append(mList.get(i).getGoods_id());
                }else {
                    good_id.append(mList.get(i).getGoods_id());
                }
            }
        }
        return good_id.toString();
    }
    /**
     * 拼接商品数量
     * @return
     */
    public String getNum(){
        good_num.replace(0, good_num.length(), "");
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getNum()>0){
                /*number++;
                mTv_number.setText(String.valueOf(number));*/
                if (StringUtil.isNotEmpty(good_num.toString().trim())) {
                    good_num.append("/").append(mList.get(i).getNum());
                }else {
                    good_num.append(mList.get(i).getNum());
                }
            }
        }
        return good_num.toString();
    }
    /**
     * 计算总价和数量
     */
    public  void getTotalPrice(){
        money = 0.0;
        number = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getNum()>0) {
                money += (mList.get(i).getNum() * Double.valueOf(mList.get(i).getPrice()));
                number+=mList.get(i).getNum();
            }
        }
        mTv_money.setText("合计：￥" + money);
        mTv_number.setText("共"+String.valueOf(number)+"件商品");
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
