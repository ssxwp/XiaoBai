package com.qingyuan.tianya.mrbuy.adapter;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.AddressBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gaoyanjun on 2016/8/13.
 */
public class AddressAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AddressBean> mList;
    private Handler mHandler;
    HashMap<String,Boolean> states=new HashMap<>();//用于记录每个RadioButton的状态，并保证只可选一个
    public AddressAdapter(Context context, ArrayList<AddressBean> mList,Handler mHandler) {
        this.context = context;
        this.mList = mList;
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
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_address,null);
            holder = new ViewHolder();
            holder.address = ((TextView) convertView.findViewById(R.id.address_road));
            holder.name = ((TextView) convertView.findViewById(R.id.address_name));
            holder.phone = ((TextView) convertView.findViewById(R.id.address_phone));
            holder.del = (ImageView)convertView.findViewById(R.id.address_del);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final RadioButton radio = (RadioButton) convertView.findViewById(R.id.address_check);
        holder.checkBox = radio;
        holder.address.setText(mList.get(position).getRoad());
        holder.name.setText(mList.get(position).getName());
        holder.phone.setText(mList.get(position).getPhone());
        holder.checkBox.setChecked(mList.get(position).isChecked());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重置，确保最多只有一项被选中
                for(String key:states.keySet()){
                    states.put(key, false);

                }
                states.put(String.valueOf(position), radio.isChecked());
                AddressAdapter.this.notifyDataSetChanged();
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setIsChecked(false);
                }
                mList.get(position).setIsChecked(true);

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
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delAdd(mList.get(position).getId());
            }
        });
        return convertView;
    }

    private void delAdd(int id) {
        final Dialog loadingDialog = createLoadingDialog(context, "正在删除...");
        loadingDialog.show();
        final String urlString = ApiConstant.DELADDRESS;
        final RequestParams params = new RequestParams();
        params.put("d_id",id+"");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 120;
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
    class ViewHolder{
        public TextView address,name,phone;
        public ImageView del;
        public RadioButton checkBox;
    }
}
