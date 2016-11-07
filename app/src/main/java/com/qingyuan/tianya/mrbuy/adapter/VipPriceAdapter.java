package com.qingyuan.tianya.mrbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.bean.VipPriceBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gaoyanjun on 2016/8/28.
 */
public class VipPriceAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<VipPriceBean> mList;
    HashMap<String,Boolean> states=new HashMap<>();//用于记录每个RadioButton的状态，并保证只可选一个

    public VipPriceAdapter(Context context, ArrayList<VipPriceBean> mList) {
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
        ViewHolder holder = null;
        if (convertView ==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.vippricelist,null);
            holder = new ViewHolder();
            holder.tv_month = (TextView)convertView.findViewById(R.id.month);
            holder.tv_money= (TextView)convertView.findViewById(R.id.money);
            convertView.setTag(holder);
        }else {
            holder = ((ViewHolder) convertView.getTag());
        }
        final CheckBox box = (CheckBox) convertView.findViewById(R.id.check);
        holder.checkBox= box;
        holder.tv_month.setText(mList.get(position).getMonth()+"月");
        holder.tv_money.setText("￥"+mList.get(position).getMoney());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重置，确保最多只有一项被选中
                for(String key:states.keySet()){
                    states.put(key, false);

                }
                states.put(String.valueOf(position), box.isChecked());
                VipPriceAdapter.this.notifyDataSetChanged();
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setChecked(false);
                }
                mList.get(position).setChecked(true);

            }
        });
        boolean res=false;
        if(states.get(String.valueOf(position)) == null || states.get(String.valueOf(position))== false){
            res=false;
            states.put(String.valueOf(position), false);
        }
        else
            res = true;

        holder.checkBox.setChecked(res);
        return convertView;
    }
    class ViewHolder {
        public TextView tv_money,tv_month;
        public CheckBox checkBox;
    }
}
