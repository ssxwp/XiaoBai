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
import android.widget.CheckBox;
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
import com.qingyuan.tianya.mrbuy.bean.CarBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by gaoyanjun on 2016/8/4.
 */
public class CarAdapter extends BaseAdapter {
    private Context context;
    private List<CarBean> mList;
    private CheckBox checkAll;
    private String m_id;
    private Handler mHandler;

    public CarAdapter(Context context, List<CarBean> mList, CheckBox checkAll,String m_id,Handler mHandler) {
        this.context = context;
        this.mList = mList;
        this.checkAll = checkAll;
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
        final ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.carlist,parent,false);
            holder = new ViewHolder();
            holder.img = ((SimpleDraweeView) convertView.findViewById(R.id.car_head_image));
            holder.tv_name = ((TextView) convertView.findViewById(R.id.car_name_text));
            //holder.score = ((TextView) convertView.findViewById(R.id.good_score_text));
            holder.price = ((TextView) convertView.findViewById(R.id.car_price_text));
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.car_check);
            holder.sub_img = (ImageView)convertView.findViewById(R.id.subbt);
            holder.add_img = (ImageView)convertView.findViewById(R.id.addbt);
            holder.del_img = (ImageView)convertView.findViewById(R.id.car_del_img);
            holder.ttt = (TextView)convertView.findViewById(R.id.ttt);
            holder.tv_all = (TextView)convertView.findViewById(R.id.car_allprice_text);
            //holder.s_price = ((TextView) convertView.findViewById(R.id.good_price_text2));
            //holder.ytime = ((TextView) convertView.findViewById(R.id.good_time_text));
            convertView.setTag(holder);
        }else{
            holder = ((ViewHolder) convertView.getTag());
        }
        if (StringUtil.isNotEmpty(mList.get(position).getPic())){
            Uri uri = Uri.parse(mList.get(position).getPic());
            holder.img.setImageURI(uri);
        }else{
            holder.img.setImageResource(R.mipmap.buffer_pic);
        }
        holder.ttt.setText(String.valueOf(mList.get(position).getNum()));
        holder.tv_name.setText(mList.get(position).getName());
        //holder.score.setText(mList.get(position).getP_score()+"分");
        holder.price.setText("单价￥"+Double.valueOf(mList.get(position).getPrice()));
        //holder.s_price.setText("会员价格："+mList.get(position).getS_price()+"元");
        //holder.ytime.setText(mList.get(position).getYtime());
        holder.tv_all.setText("￥"+String.valueOf(Double.valueOf(mList.get(position).getPrice()) * mList.get(position).getNum()));
        //选中未选择状态
        if (mList.get(position).ischecked()){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(position).ischecked()){
                    mList.get(position).setIschecked(false);
                    checkAll.setChecked(false);
                }else {
                    mList.get(position).setIschecked(true);
                }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 100;
                            mHandler.sendMessage(msg);
                        }
                    });
                    thread.start();
            }
        });
        holder.sub_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = mList.get(position).getNum();
                num++;
                mList.get(position).setNum(num);
                String money = String.valueOf(mList.get(position).getNum() * (Double.valueOf(mList.get(position).getPrice())));
                holder.tv_all.setText("￥："+money);
                holder.ttt.setText(String.valueOf(mList.get(position).getNum()));
                if (holder.checkBox.isChecked()){
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 100;
                            mHandler.sendMessage(msg);
                        }
                    });
                    thread.start();
                }
            }
        });
        holder.add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = mList.get(position).getNum();
                if (num>1) {
                    num--;
                    mList.get(position).setNum(num);
                    String money = String.valueOf(mList.get(position).getNum() * (Double.valueOf(mList.get(position).getPrice())));
                    holder.tv_all.setText("￥："+money);
                    holder.ttt.setText(String.valueOf(mList.get(position).getNum()));

                }
                if (holder.checkBox.isChecked()){
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 100;
                            mHandler.sendMessage(msg);
                        }
                    });
                    thread.start();
                }
            }
        });
        holder.del_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delCar(String.valueOf(mList.get(position).getGoods_id()));
            }
        });
        return convertView;
    }

    private void delCar(String goods_id) {
        final Dialog loadingDialog = createLoadingDialog(context, "正在删除...");
        loadingDialog.show();
        final String urlString = ApiConstant.CARDEL;
        final RequestParams params = new RequestParams();
        params.put("goods_id",goods_id);
        params.put("mem_id",m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.toString().trim());
                        if (jObj.getString("flag").equals("success")) {
                            Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    Message msg = new Message();
                                    msg.what = 20;
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
    private class ViewHolder {
        public SimpleDraweeView img;
        public TextView tv_name,tv_all;
        public TextView price;
        public CheckBox checkBox;
        public ImageView sub_img;
        public ImageView add_img,del_img;
        public TextView ttt;
    }
}
