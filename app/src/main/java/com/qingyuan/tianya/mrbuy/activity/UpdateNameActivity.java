package com.qingyuan.tianya.mrbuy.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateNameActivity extends BaseActivity{
    private String name;
    private HeaderView head;
    private EditText ed;
    private String m_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        head = (HeaderView) findViewById(R.id.merchant_head);
        ed = ((EditText) findViewById(R.id.merchant_ed));
    }

    @Override
    public void initData() {
        m_id = createSharedPreference(this, "user_custId").getValue("custId");
        ed.setText(name);

    }

    @Override
    public void initClick() {
        head.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUpdateName();
            }
        });
    }
    /**
     *
     * 存储名字
     */
    private void toUpdateName() {
        String urlString = ApiConstant.UPDATENICKNAME;
        RequestParams params = new RequestParams();
        if (StringUtil.isNotEmpty(ed.getText().toString().trim())){
            params.put("username",ed.getText().toString().trim());
        }else {
            toast("店铺名字不能为空");
            return;
        }
        params.put("m_id", m_id);
        initProgressDialog();
        HttpUtil.post(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                Log.i("TAG", "修改成功！" + response);
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        toast(jObj.getString("message"));
                    } else {
                        toast(jObj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
                close();
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
                Log.e("hck", " onFailure" + arg0.toString());
            }

            public void onFinish() { // 完成后调用，失败，成功，都要调
                close();
            }


        });
    }
}
