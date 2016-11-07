package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.bean.LoginBean;
import com.qingyuan.tianya.mrbuy.db.SharedPreferenceHelper;
import com.qingyuan.tianya.mrbuy.push.ExampleUtil;
import com.qingyuan.tianya.mrbuy.utils.KeepLogMsg;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    private EditText et_reg_phone,et_reg_password;
    private SharedPreferenceHelper userMsg;
    private LoginBean loginBean;
    private String from;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        HeaderView header = (HeaderView) findViewById(R.id.login_head);
        header.getHeadBackGround().setBackgroundColor(Color.rgb(38, 42, 59));
        et_reg_phone = ((EditText) findViewById(R.id.et_reg_phone));
        et_reg_password = ((EditText) findViewById(R.id.et_reg_password));
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        from = bundle.getString("from");
        userMsg = new SharedPreferenceHelper(LoginActivity.this, "user_custId");

    }

    @Override
    public void initClick() {
        findViewById(R.id.login_log).setOnClickListener(this);
        findViewById(R.id.login_regist).setOnClickListener(this);
        findViewById(R.id.login_up).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_log:
                String et_phone = et_reg_phone.getText().toString().trim();
                String et_password = et_reg_password.getText().toString().trim();
                getLoginMessage(et_phone, et_password);
                break;
            case R.id.login_regist:
                Bundle bundle = new Bundle();
                bundle.putString("from",from);
                skipActivity(LoginActivity.this, RegistActivity.class, bundle);
                break;
            case R.id.login_up:
                skipActivity(this,PassWordActivity.class,null);
                break;
        }
    }

    private void getLoginMessage(final String phone, String password) {
        if (phone == null || phone.trim().length() == 0) {
            toast("请输入您的手机号！");
            return;
        }
        if (password == null || password.trim().length() == 0) {
            toast("请输入您的密码！");
            return;
        }
        if (phone.length() != 11) {
            toast("手机号码格式不对！");
            return;
        }
        initProgressDialog();
        String urlString = ApiConstant.LOGIN;
        RequestParams params = new RequestParams();
        params.put("account", phone);
        params.put("password", password);
        HttpUtil.get(urlString, params, new
                AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        // 获取数据成功会调用这里
                        try {
                            JSONObject jObj = new JSONObject(response.trim());
                            if (jObj.getString("flag").equals("success")) {
                                JSONObject cus = jObj.getJSONObject("responseList");
                                //JSONArray customer=jObj.getJSONArray("responseList");
                                //for (int i=0;i<customer.length();i++){
                                    //JSONObject cus=customer.getJSONObject(i);
                                    String custId =cus.getString("m_id");
                                    userMsg.putValue("custId",custId);
                                    loginBean = new LoginBean();
                                    loginBean.setM_id(custId);
                                //}
                                KeepLogMsg.getKeepLogMsg(loginBean, userMsg);
                                setAlias(loginBean.getM_id());
                                toast(jObj.getString("message"));
                                switch (from) {
                                    case "personal":
                                        skipActivityForClose(LoginActivity.this, PersonalInfoActivity.class, null);
                                        break;
                                    case "collect":
                                        skipActivityForClose(LoginActivity.this, CollectActivity.class, null);
                                        break;
                                    case "car":
                                        skipActivityForClose(LoginActivity.this, CarActivity.class, null);
                                        break;
                                    case "vip":
                                        skipActivityForClose(LoginActivity.this, VipActivity.class, null);
                                        break;
                                    case "mer":
                                        finish();
                                        break;
                                }
                            } else {
                                toast(jObj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        close();
                    }

                    public void
                    onFailure(Throwable arg0) { // 失败，调用
                        close();
                    }

                    public void onFinish() { // 完成后调用，失败，成功，都要掉 close(); }; });
                    }
                });
    }

    private void setAlias(String phone) {
        //校验输入的手机号是否合理
        if (!ExampleUtil.isValidTagAndAlias(phone)) {
            Toast.makeText(LoginActivity.this, "格式不对",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
