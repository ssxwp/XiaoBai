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
 * Created by gaoyanjun on 2016/8/16.
 */
public class BookAdapter  extends BaseAdapter{
    private Context context;
    private ArrayList<DishMessageBean> mList;

    public BookAdapter(Context context, ArrayList<DishMessageBean> mList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_book,null);
            holder = new ViewHolder();
            holder.img = ((SimpleDraweeView) convertView.findViewById(R.id.book_image));
            holder.tv_name = ((TextView) convertView.findViewById(R.id.book_name));
            holder.tv_price = ((TextView) convertView.findViewById(R.id.price_text));
            holder.tv_num = ((TextView) convertView.findViewById(R.id.book_num));
            holder.joinCar = (ImageView)convertView.findViewById(R.id.book_car);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (StringUtil.isNotEmpty(mList.get(position).getHead_pic())){
            Uri uri = Uri.parse(mList.get(position).getHead_pic());
            holder.img.setImageURI(uri);
        }
        holder.tv_name.setText(mList.get(position).getShop_name());
        holder.tv_price.setText("售价：￥"+mList.get(position).getPrice());
        holder.tv_num.setText("销量："+mList.get(position).getVolume());
        return convertView;
    }

    private class ViewHolder {
        public SimpleDraweeView img;
        public TextView tv_name;
        public TextView tv_price,tv_num;
        public ImageView joinCar;

    }
}
