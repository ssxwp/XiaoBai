package com.qingyuan.tianya.mrbuy.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.push.ExampleUtil;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class AddAddressActivity extends BaseActivity {

    private EditText address,name,phone;
    private String m_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        m_id = createSharedPreference(this, "user_custId").getValue("custId");
        address = ((EditText) findViewById(R.id.addaddress_address));
        name = ((EditText) findViewById(R.id.addaddress_name));
        phone = ((EditText) findViewById(R.id.addaddress_phone));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initClick() {
        findViewById(R.id.addaddress_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addaddress_add:
                if (StringUtil.isNotEmpty(address.getText().toString())){
                    if (StringUtil.isNotEmpty(name.getText().toString())){
                        if (StringUtil.isEmpty(phone.getText().toString())){
                            toast("手机号不能为空");
                        }else if (phone.getText().toString().trim().length()!=11){
                            toast("手机号格式不对");
                        }else {
                            saveAddress();
                        }
                    }else {
                        toast("姓名不能为空");
                    }
                }else {
                    toast("地址不能为空");
                }
                break;
        }
    }

    private void saveAddress() {
        initProgressDialog();
        String urlString = ApiConstant.ADDADDRESS;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id);
        params.put("road",address.getText().toString());
        params.put("name",name.getText().toString());
        params.put("phone",phone.getText().toString());
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    if (jObj.getString("flag").equals("success")) {
                        setAlias(phone.getText().toString().trim());
                        Toast.makeText(AddAddressActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(AddAddressActivity.this,AddressActivity.class);
                        setResult(1);
                        AddAddressActivity.this.finish();
                    } else {
                        Toast.makeText(AddAddressActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                close();
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }
    //极光推送
    private void setAlias(String phone) {
        //校验输入的手机号是否合理
        if (!ExampleUtil.isValidTagAndAlias(phone)) {
            Toast.makeText(AddAddressActivity.this, "格式不对",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        /*// 调用JPush API设置Alias  调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, phone));*/
    }
}
