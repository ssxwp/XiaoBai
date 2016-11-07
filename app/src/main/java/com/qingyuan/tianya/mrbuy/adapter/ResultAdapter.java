package com.qingyuan.tianya.mrbuy.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.bean.GoodsBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;


import java.util.ArrayList;

/**
 * Created by gaoyanjun on 2016/8/21.
 */
public class ResultAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<GoodsBean> mList;

    public ResultAdapter(Context context, ArrayList<GoodsBean> mList ){
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
        final ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.result_list,null);
            holder = new ViewHolder();
            holder.img = ((SimpleDraweeView) convertView.findViewById(R.id.good_head_img));
            holder.tv_name = ((TextView) convertView.findViewById(R.id.good_name_text));
            holder.score = ((TextView) convertView.findViewById(R.id.good_score_text));
            holder.price = ((TextView) convertView.findViewById(R.id.good_price_text1));
            holder.s_price = ((TextView) convertView.findViewById(R.id.good_price_text2));
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
        holder.score.setText(mList.get(position).getScore() + "分");
        holder.price.setText("市场价：￥" + mList.get(position).getS_price());
        holder.s_price.setText("会员价：￥" + mList.get(position).getPrice());


        return convertView;
    }



    private class ViewHolder {
        public SimpleDraweeView img;
        public TextView tv_name;
        public TextView price;
        public TextView s_price;
        public TextView score;

    }

    }
