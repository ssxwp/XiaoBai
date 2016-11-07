package com.qingyuan.tianya.mrbuy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.qingyuan.tianya.mrbuy.R;

public class WelcomeActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        initClick();
        initData();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent=new Intent(WelcomeActivity.this,HomeActivity.class);
                    WelcomeActivity.this.startActivity(intent);
                    WelcomeActivity.this.finish();

            }

        }, 2000);
    }
   /* public void getTextUpdate() {
//		final int versionCode = getVersionVode(context, "com.djf.moodinn");// 当前版本号
        String urlString = ApiConstant.QUERYVERSIONS;
        RequestParams params=new RequestParams();
        HttpUtil.post(urlString, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jObj) {
                Log.i("TAG", "检测版本更新：" + jObj.toString());
                try {
                    if (jObj.getString("code").equals("0")) {
                        JSONArray jsonArray = jObj.getJSONArray("jsonArray");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
//							code=jobj.getString("code");
//							name=jobj.getString("name");
                            vcode = jobj.getInt("vcode");
                            vname = jobj.getString("vname");
                            vdesc = jobj.getString("vdesc");
                            scode.putValue("vcode", String.valueOf(vcode));
                            scode.putValue("vname", vname);
                            scode.putValue("vdesc", vdesc);
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    close();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.e("hck", " onFailure" + arg0.toString());
                close();
            }

            ;

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }

            ;
        });
    }*/
}
