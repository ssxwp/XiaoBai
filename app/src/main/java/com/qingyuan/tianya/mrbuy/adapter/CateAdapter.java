package com.qingyuan.tianya.mrbuy.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.bean.CateBean;

import java.util.ArrayList;

/**
 * Created by gaoyanjun on 2016/8/18.
 */
public class CateAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CateBean> mList;

    public CateAdapter(Context context, ArrayList<CateBean> mList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.cate,null);
            holder = new ViewHolder();
            holder.tv_name = ((TextView) convertView.findViewById(R.id.tex));
            convertView.setTag(holder);
        }else {
            holder = ((ViewHolder) convertView.getTag());
        }
        Resources resources=context.getResources();
        Drawable drawable=resources.getDrawable(R.drawable.searchview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            convertView.setBackground(drawable);
        }
        holder.tv_name.setText(mList.get(position).getCate_name());
        return convertView;
    }
    class ViewHolder{
        public TextView tv_name;
    }
}
