package com.qingyuan.tianya.mrbuy.adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.GoodsBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gaoyanjun on 2016/8/17.
 */
public class GoodsAdapters extends BaseAdapter{
    private Context context;
    private ArrayList<GoodsBean> mList;
    private String m_id;
    private boolean isCollect;
    //private Dialog loadingDialog;

    public GoodsAdapters(Context context, ArrayList<GoodsBean> mList,String m_id) {
        this.context = context;
        this.mList = mList;
        this.m_id = m_id;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_goods,null);
            holder = new ViewHolder();
            holder.img = ((SimpleDraweeView) convertView.findViewById(R.id.good_head_img));
            holder.tv_name = ((TextView) convertView.findViewById(R.id.good_name_text));
            holder.score = ((TextView) convertView.findViewById(R.id.good_score_text));
            holder.price = ((TextView) convertView.findViewById(R.id.good_price_text1));
            holder.s_price = ((TextView) convertView.findViewById(R.id.good_price_text2));
            holder.good_join_img = (ImageView)convertView.findViewById(R.id.good_join_img);
            holder.good_collect_img = (ImageView)convertView.findViewById(R.id.good_collect_img);
            convertView.setTag(holder);
        }else{
            holder = ((ViewHolder) convertView.getTag());
        }
        if (StringUtil.isNotEmpty(mList.get(position).getPic_1())){
            Uri uri = Uri.parse(mList.get(position).getPic_1());
            holder.img.setImageURI(uri);
        }else{
            holder.img.setImageResource(R.mipmap.buffer_pic);
        }
       // holder.num_text.setText(String.valueOf(mList.get(position).getNum()));
        holder.tv_name.setText(mList.get(position).getGoods_name());
        holder.score.setText(mList.get(position).getScore()+"分");
        holder.price.setText("市场价：￥" + mList.get(position).getS_price());
        holder.s_price.setText("会员价：￥" + mList.get(position).getPrice() );
        isCollect(holder.good_collect_img, mList.get(position).getGoods_id());
        //点击加入购物车
        holder.good_join_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCar(mList.get(position).getGoods_id());
            }
        });
        //点击收藏
        holder.good_collect_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollect) {
                    isCollect = false;
                    collectShop(ApiConstant.DELCOLLECT,holder.good_collect_img, mList.get(position).getGoods_id());
                }else {
                    isCollect =true;
                    collectShop(ApiConstant.ADDCOLLECT,holder.good_collect_img,mList.get(position).getGoods_id());
                }
            }
        });

        return convertView;
    }

    private void isCollect(final ImageView imageView,int goods_id) {
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
                        imageView.setImageResource(R.mipmap.collect_1);
                        //Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        isCollect = false;
                        imageView.setImageResource(R.mipmap.collect_3);
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

    private class ViewHolder {
        public SimpleDraweeView img;
        public TextView tv_name,num_text;
        public TextView price;
        public TextView s_price;
        public ImageView good_join_img,good_collect_img;
        public TextView score;

    }

    private void addCar(int good_id) {
//        loadingDialog = createLoadingDialog(context, "正在加入购物车...");
//        loadingDialog.show();
        String urlString = ApiConstant.ADDCAR;
        RequestParams params = new RequestParams();
        params.put("goods_id",good_id+"");
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        Toast.makeText(context, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //loadingDialog.dismiss();
            }

            public void onFailure(Throwable arg0) { // 失败，调用
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉

                //loadingDialog.dismiss();
            }
        });
    }
    /**
     * 添加收藏
     */
    private void collectShop(final String urlString,final ImageView imageView,int goods_id) {
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
                            imageView.setImageResource(R.mipmap.collect_1);
                            Toast.makeText(context, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }else if (urlString.equals(ApiConstant.DELCOLLECT)){
                        if (jObj.getString("flag").equals("success")) {
                            imageView.setImageResource(R.mipmap.collect_3);
                            Toast.makeText(context, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jObj.getString("message"), Toast.LENGTH_SHORT).show();
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

    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.dialog_loading);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.load_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;

    }
}
