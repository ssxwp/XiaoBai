package com.qingyuan.tianya.mrbuy.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class PassWordActivity extends BaseActivity{

    //private EditText phone;
    private EditText phone;
    private EditText password;
    private EditText password1;
    private TextView sure;
    private EditText security;
    private TextView tv_reg_getsecurity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        //phone = (EditText)findViewById(R.id.et_reg_phone);
        phone = (EditText)findViewById(R.id.et_reg_oldpassword);
        security = (EditText)findViewById(R.id.et_reg_security);
        password = (EditText)findViewById(R.id.et_reg_password);
        password1 = (EditText)findViewById(R.id.et_reg_password1);
        sure = (TextView)findViewById(R.id.tv_login);
        tv_reg_getsecurity = (TextView)findViewById(R.id.tv_reg_getsecurity);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initClick() {
        sure.setOnClickListener(this);
        tv_reg_getsecurity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_login:
                //String account =phone.getText().toString().trim();
                String old = phone.getText().toString().trim();
                String newword = password.getText().toString().trim();
                String pass = password1.getText().toString().trim();
                String se = security.getText().toString().trim();
                changePassword(old,newword,pass,se);
                break;
            case R.id.tv_reg_getsecurity:
                String phone2 = phone.getText().toString().trim();
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
                tv_reg_getsecurity.setClickable(false);
                tv_reg_getsecurity.setBackgroundColor(PassWordActivity.this
                        .getResources().getColor(R.color.light_gray));
                tv_reg_getsecurity.setText(millisUntilFinished / 1000 + "秒后重新获取");
            }

            public void onFinish() {
                tv_reg_getsecurity.setClickable(true);
                tv_reg_getsecurity.setBackgroundColor(PassWordActivity.this
                        .getResources().getColor(R.color.lightblue));
                tv_reg_getsecurity.setText("获取验证码");
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
        HttpUtil.gets("http://114.215.78.102/index.php?s=/Api/Member/send_back", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jObj = new JSONObject(s);
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
    private void changePassword(String old, final String newword, String pass,String se) {
        if (old == null || old.trim().length() == 0) {
            toast("请输入您的手机号！");
            return;
        }
        if (newword == null || newword.trim().length() == 0) {
            toast("请输入您的新密码！");
            return;
        }
        if (pass == null || pass.trim().length() == 0) {
            toast("请确认您的新密码！");
            return;
        }
        if (!newword.equals(pass)){
            toast("两次输入密码不一致");
            return;
        }
        if (se == null || se.trim().length() == 0) {
            toast("请输入验证码");
            return;
        }
        initProgressDialog();
        String urlString = ApiConstant.PASSWORD;
        RequestParams params = new RequestParams();
        params.put("vc", se);
        params.put("way",old);
        HttpUtil.gets("http://114.215.78.102/index.php?s=/Api/Member/vc_back", params, new
                AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        // 获取数据成功会调用这里
                        try {
                            JSONObject jObj = new JSONObject(response.trim());
                            if (jObj.getString("flag").equals("success")) {
                                String mem_id = jObj.getString("message");
                               updatePassWord(mem_id,newword);
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

    private void updatePassWord(String m,String n) {
        RequestParams params = new RequestParams();
        params.put("mem_id", m);
        params.put("password",n);
        HttpUtil.gets("http://114.215.78.102/index.php?s=/Api/Member/edit_back", params, new
                AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        // 获取数据成功会调用这里
                        try {
                            JSONObject jObj = new JSONObject(response.trim());
                            if (jObj.getString("flag").equals("success")) {
                                toast(jObj.getString("message"));
                                PassWordActivity.this.finish();
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

}
