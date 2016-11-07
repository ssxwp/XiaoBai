package com.qingyuan.tianya.mrbuy.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.view.view.HeaderView;

import org.json.JSONException;
import org.json.JSONObject;


public class RegistActivity extends BaseActivity{

    private EditText et_phone,et_code,et_password;
    private TextView tv_getsecurity,tv_login;
    private SharedPreferenceHelper sp,userMsg;
    private String logEmail;
    private LoginBean loginBean;
    private String from;
    private EditText et_password1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
        initData();
        initClick();

    }

    @Override
    public void initView() {
        HeaderView header = (HeaderView) findViewById(R.id.login_head);
        header.getHeadBackGround().setBackgroundColor(Color.rgb(38,42,59));
        et_phone = (EditText) findViewById(R.id.et_reg_phone);
        et_code = (EditText) findViewById(R.id.et_reg_security);
        et_password = (EditText) findViewById(R.id.et_reg_password);
        et_password1 = (EditText) findViewById(R.id.et_reg_password1);
        tv_getsecurity = (TextView) findViewById(R.id.tv_reg_getsecurity);
        tv_login = (TextView) findViewById(R.id.tv_login);

        sp = new SharedPreferenceHelper(RegistActivity.this, "loginMsg");
        userMsg = new SharedPreferenceHelper(RegistActivity.this, "user_custId");

        //String phone = sp.getValue("phone");
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        from = bundle.getString("fro");
    }

    @Override
    public void initClick() {
        tv_login.setOnClickListener(this);
        tv_getsecurity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_login:
                logEmail = et_phone.getText().toString().trim();
                String logCode1 = et_code.getText().toString().trim();
                String logPassWord = et_password.getText().toString().trim();
                String pass = et_password1.getText().toString().trim();
                getLoginMessage(logEmail, logCode1, logPassWord, pass);
                break;

            case R.id.tv_reg_getsecurity:
                String phone2 = et_phone.getText().toString().trim();
                if (StringUtil.isNotEmpty(phone2)) {
                    getSecurity();
                    telSecurityCode(phone2);
                } else {
                    toast("请输入您的手机号码！");
                }
                break;
        }
    }

    /**
     * 验证码倒计时
     */
    private void getSecurity() {
        CountDownTimer mCountDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_getsecurity.setClickable(false);
                tv_getsecurity.setBackgroundColor(RegistActivity.this
                        .getResources().getColor(R.color.light_gray));
                tv_getsecurity.setText(millisUntilFinished / 1000 + "秒后重新获取");
            }

            public void onFinish() {
                tv_getsecurity.setClickable(true);
                tv_getsecurity.setBackgroundColor(RegistActivity.this
                        .getResources().getColor(R.color.lightblue));
                tv_getsecurity.setText("获取验证码");
            }
        };
        mCountDownTimer.start();
    }
    /**
     * 获取验证码
     */
    private void telSecurityCode(final String phone) {
        if (StringUtil.isEmpty(phone)) {
            toast("请输入手机号！");
            return;
        }
        if (phone.length() != 11) {
            toast("手机号码格式不对！");
            return;
        }
        String urlString = ApiConstant.SENDMESSAGE;
        //HashMap<String, String> map = new HashMap<String, String>();
        RequestParams params = new RequestParams();
        params.put("number", phone);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jObj = new JSONObject(s.trim());
                    if (jObj.getString("flag").equals("success")) {
                        toast("验证码已发送");
                    } else if (jObj.getString("flag").equals("repeat")) {
                        toast(jObj.getString("message"));
                    } else {
                        toast(jObj.getString("message"));
                    }
                   /* try {
                        toast(jObj.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        close();
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要调
                close();
            }
        });

    }

    /**
     * 注册
     */
    public void getLoginMessage(final String logPhone, final String logCode,final String logPassWord,final String pass) {
        if (logPhone == null || logPhone.trim().length() == 0) {
            toast("请输入您的手机号！");
            return;
        }
        if (logPassWord == null || logPassWord.trim().length() == 0) {
            toast("请输入您的密码！");
            return;
        }
        if (!pass.equals(logPassWord)){
            toast("两次密码输入不同");
            return;
        }
        if (logPhone.length() != 11) {
            toast("手机号码格式不对！");
            return;
        }
        if (logCode == null || logCode.trim().length() == 0) {
            toast("请输入验证码");
            return;
        }
        initProgressDialog();
        String urlString = ApiConstant.REGIST;
        RequestParams params = new RequestParams();
        params.put("way", logPhone);
        params.put("vc", logCode);
        params.put("password", logPassWord);
        HttpUtil.get(urlString, params, new
                AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        // 获取数据成功会调用这里
                        try {
                            JSONObject jObj = new JSONObject(response.trim());
                            if (jObj.getString("flag").equals("success")) {
                                sp.putValue("phone", logEmail);
                                //注册成功自动登录并跳转
                                login(logPhone,logPassWord);
                                toast(jObj.getString("message"));
                                switch (from) {
                                    case "personal":
                                        skipActivityForClose(RegistActivity.this, PersonalInfoActivity.class, null);
                                        break;
                                    case "collect":
                                        skipActivityForClose(RegistActivity.this, CollectActivity.class, null);
                                        break;
                                    case "car":
                                        skipActivityForClose(RegistActivity.this, CarActivity.class, null);
                                        break;
                                    case "vip":
                                        skipActivityForClose(RegistActivity.this, VipActivity.class, null);
                                        break;
                                    case "mer":
                                        finish();
                                        break;
                                }
                                //skipActivityForClose(RegistActivity.this, HomeActivity.class, null);
                            } else {
                                toast(jObj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            close();
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
    //登录
    private void login(String logPhone, String logPassWord) {
        initProgressDialog();
        String urlString = ApiConstant.LOGIN;
        RequestParams params = new RequestParams();
        params.put("account", logPhone);
        params.put("password", logPassWord);
        HttpUtil.get(urlString, params, new
                AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        // 获取数据成功会调用这里
                        try {
                            JSONObject jObj = new JSONObject(response.trim());
                            if (jObj.getString("flag").equals("success")) {
                                //JSONArray customer=jObj.getJSONArray("responseList");
                                //for (int i=0;i<customer.length();i++){
                                JSONObject cus = jObj.getJSONObject("responseList");
                                String custId = cus.getString("m_id");
                                userMsg.putValue("custId", custId);
                                loginBean = new LoginBean();
                                loginBean.setM_id(custId);
                                //}
                                KeepLogMsg.getKeepLogMsg(loginBean, userMsg);
                                setAlias(loginBean.getM_id());
                                toast(jObj.getString("message"));
                                //skipActivityForClose(RegistActivity.this, HomeActivity.class, null);
                            } else {
                                toast(jObj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            close();
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

    //极光推送
    private void setAlias(String phone) {
        //校验输入的手机号是否合理
        if (!ExampleUtil.isValidTagAndAlias(phone)) {
            Toast.makeText(RegistActivity.this, "格式不对",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        /*// 调用JPush API设置Alias  调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, phone));*/
    }
    /*private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(MSG_SET_ALIAS, alias),
                                1000 * 60);
                    } else {
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
            // ExampleUtil.showToast(logs, getApplicationContext());
        }

    };
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj, null, mAliasCallback);
                    break;
            }
        }
    };*/
}
