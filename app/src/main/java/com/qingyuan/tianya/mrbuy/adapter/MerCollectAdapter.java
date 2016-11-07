package com.qingyuan.tianya.mrbuy.adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
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
import com.qingyuan.tianya.mrbuy.bean.MerCollectBean;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gaoyanjun on 2016/8/6.
 */
public class MerCollectAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MerCollectBean> mList;
    private String m_id;
    private Handler mHandler;

    public MerCollectAdapter(Context context, ArrayList<MerCollectBean> mList,String m_id,Handler mHandler) {
        this.context = context;
        this.mList = mList;
        this.m_id = m_id;
        this.mHandler = mHandler;
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
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.mercollectlist,null);
            holder = new ViewHolder();
            holder.img = ((SimpleDraweeView) convertView.findViewById(R.id.mer_collect_img));
            holder.name = ((TextView) convertView.findViewById(R.id.mer_collect_name));
            holder.imageView = (ImageView)convertView.findViewById(R.id.mer_dele_img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList.get(position).getHead_pic()!=null) {
            Uri uri = Uri.parse(mList.get(position).getHead_pic());
            holder.img.setImageURI(uri);
        }else {
            holder.img.setImageResource(R.mipmap.buffer_pic);
        }
        holder.name.setText(mList.get(position).getShop_name());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delMer(String.valueOf(mList.get(position).getShop_id()));
                MerCollectAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public class ViewHolder{
        private SimpleDraweeView img;
        private TextView name;
        private ImageView imageView;
    }
    private void delMer(String goods_id) {
        final Dialog loadingDialog = createLoadingDialog(context, "正在删除...");
        loadingDialog.show();
        final String urlString = ApiConstant.DELCOLLECTION;
        final RequestParams params = new RequestParams();
        params.put("shop_id",goods_id);
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 101;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                        Toast.makeText(context, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用

            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                loadingDialog.dismiss();
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
