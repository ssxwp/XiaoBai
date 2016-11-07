package com.qingyuan.tianya.mrbuy.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.bean.DishMessageBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by gaoyanjun on 2016/8/13.
 */
public class WomenMerchantAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<DishMessageBean> mList;

    public WomenMerchantAdapter(Context context, ArrayList<DishMessageBean> mList) {
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

   /* public void addItem(String item) {
        mList.add(item);
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_women_merchant,null);
            holder = new ViewHolder();
            holder.img = ((SimpleDraweeView) convertView.findViewById(R.id.dishmerchant_image));
            holder.tv_name = ((TextView) convertView.findViewById(R.id.dishmerchant_name));
            holder.tv_address = ((TextView) convertView.findViewById(R.id.dishmerchant_address));
            holder.tv_start = ((TextView) convertView.findViewById(R.id.start_price_text));
            holder.tv_send = ((TextView) convertView.findViewById(R.id.send_price_text));
            holder.tv_juli = ((TextView) convertView.findViewById(R.id.women_juli));
            holder.img_status = ((ImageView) convertView.findViewById(R.id.status_img));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (StringUtil.isNotEmpty(mList.get(position).getHead_pic())){
            Uri uri = Uri.parse(mList.get(position).getHead_pic());
            holder.img.setImageURI(uri);
        }
        if (mList.get(position).getStatus().equals("0")){
            holder.img_status.setImageResource(R.mipmap.open);
        }else if (mList.get(position).getStatus().equals("1")){
            holder.img_status.setImageResource(R.mipmap.open);
        }
        if (Double.parseDouble(mList.get(position).getJuli())<1000) {
            holder.tv_juli.setText(mList.get(position).getJuli()+"m");
        }else {
            holder.tv_juli.setText(Double.parseDouble(mList.get(position).getJuli())/1000+"km");
        }
        holder.tv_start.setText("人均消费：￥"+mList.get(position).getPrice());
        holder.tv_name.setText(mList.get(position).getShop_name());
        holder.tv_address.setText("地址："+mList.get(position).getProvince()+""+mList.get(position).getCity()+""+mList.get(position).getDistrict());
        return convertView;
    }

    private class ViewHolder {
        public SimpleDraweeView img;
        public TextView tv_name,tv_juli;
        public TextView tv_address,tv_start,tv_send;
        public ImageView img_status;

    }
}
