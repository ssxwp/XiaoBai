package com.qingyuan.tianya.mrbuy.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.bean.DishMessageBean;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gaoyanjun on 2016/6/23.
 */
public class DishMerchantAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DishMessageBean> mList;

    public DishMerchantAdapter(Context context, ArrayList<DishMessageBean> mList) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dishmerchant, null);
            holder = new ViewHolder();
            holder.img = ((SimpleDraweeView) convertView.findViewById(R.id.dishmerchant_image));
            holder.tv_name = ((TextView) convertView.findViewById(R.id.dishmerchant_name));
            holder.tv_address = ((TextView) convertView.findViewById(R.id.dishmerchant_address));
            holder.tv_start = ((TextView) convertView.findViewById(R.id.start_price_text));
            holder.tv_send = ((TextView) convertView.findViewById(R.id.send_price_text));
            holder.img_status = ((ImageView) convertView.findViewById(R.id.status_img));
            holder.tv_juli = (TextView) convertView.findViewById(R.id.tv_juli);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (StringUtil.isNotEmpty(mList.get(position).getHead_pic())) {
            Uri uri = Uri.parse(mList.get(position).getHead_pic());
            int width = 60, height = 60;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setImageRequest(request)
                    .setTapToRetryEnabled(true)
                    .build();
            holder.img.setController(controller);
            holder.img.setImageURI(uri);
        } else {
            holder.img.setImageResource(R.mipmap.buffer_pic);
        }

        if (mList.get(position).getStatus().equals("0")) {
            holder.img_status.setImageResource(R.mipmap.open);
        } else if (mList.get(position).getStatus().equals("1")) {
            holder.img_status.setImageResource(R.mipmap.close);
        }
        if (Double.parseDouble(mList.get(position).getJuli()) < 1000) {
            holder.tv_juli.setText(mList.get(position).getJuli() + "m");
        } else {
            holder.tv_juli.setText(Double.parseDouble(mList.get(position).getJuli()) / 1000 + "km");
        }
        holder.tv_start.setText("起送价：￥" + mList.get(position).getQ_price());
        holder.tv_send.setText("配送费：￥" + mList.get(position).getP_price());
        holder.tv_name.setText(mList.get(position).getShop_name());
        holder.tv_address.setText("地址：" + mList.get(position).getProvince() + "" + mList.get(position).getCity() + "" + mList.get(position).getDistrict());
        return convertView;
    }

    static class ViewHolder {
        public SimpleDraweeView img;
        public TextView tv_name;
        public TextView tv_address, tv_start, tv_send, tv_juli;
        public ImageView img_status;

    }
}
