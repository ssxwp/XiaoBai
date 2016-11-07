package com.qingyuan.tianya.mrbuy.adapter;


import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;



import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.bean.OrderInfoBean;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gaoyanjun on 2016/8/17.
 */
public class OrderInfoAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<OrderInfoBean> mList;

    public OrderInfoAdapter(Context context, ArrayList<OrderInfoBean> mList) {
        this.context = context;
        this.mList = mList;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.order_list,null);
            holder = new ViewHolder();
            holder.tv_type = (TextView)convertView.findViewById(R.id.order_type);
            holder.tv_sn = (TextView)convertView.findViewById(R.id.order_sn);
            holder.tv_time = (TextView)convertView.findViewById(R.id.order_time);
            holder.tv_status = (TextView)convertView.findViewById(R.id.order_status);
            holder.tv_oldprice = (TextView)convertView.findViewById(R.id.order_oldprice);
            holder.tv_price = (TextView)convertView.findViewById(R.id.order_price);
            holder.tv_info = (TextView)convertView.findViewById(R.id.order_info_text);
            holder.img = (ImageView)convertView.findViewById(R.id.order_head_img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String strTime = getStrTime(mList.get(position).getCtime());
        holder.tv_sn.setText("订单号："+mList.get(position).getOrder_sn());
        holder.tv_time.setText("生成时间："+strTime);
        holder.tv_oldprice.setText("原价￥"+mList.get(position).getOldprice());
        holder.tv_price.setText("现价￥"+mList.get(position).getPrice());
        String status = mList.get(position).getStatus();
        String type = mList.get(position).getType();
        switch (status){
            case "3":
                holder.tv_status.setText("已取消");
                holder.tv_status.setBackgroundColor(Color.GRAY);
                break;
            case "2":
            case "4":
                holder.tv_status.setText("已完成");
                holder.tv_status.setBackgroundColor(Color.BLUE);
                break;
            case "0":
                holder.tv_status.setText("待付款");
                holder.tv_status.setBackgroundColor(Color.RED);
                break;
            case "1":
                holder.tv_status.setText("待收货");
                holder.tv_status.setBackgroundColor(Color.RED);
                break;
            case "5":
                holder.tv_status.setText("待消费");
                holder.tv_status.setBackgroundColor(Color.RED);
                break;
            case "8":
                holder.tv_status.setText("已接单");
                holder.tv_status.setBackgroundColor(Color.GREEN);
                break;
        }
        switch (type) {
            case "1":
                holder.tv_type.setText("普通订单");
                holder.img.setImageResource(R.mipmap.buffer_pic);
                break;
            case "2":
                holder.tv_type.setText("会员充值订单");
                holder.img.setImageResource(R.mipmap.vip);
                break;
            case "3":
                holder.tv_type.setText("余额充值订单");
                holder.img.setImageResource(R.mipmap.logo2);
                break;
            default:
                holder.tv_type.setText("外卖");
                holder.img.setImageResource(R.mipmap.food);
                break;
        }
        return convertView;
    }

    public class ViewHolder{
        private ImageView img;
        private TextView tv_type,tv_sn,tv_time,tv_status,tv_oldprice,tv_price,tv_info;
    }
    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }
}
