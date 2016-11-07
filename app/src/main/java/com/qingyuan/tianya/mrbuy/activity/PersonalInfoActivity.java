package com.qingyuan.tianya.mrbuy.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qingyuan.tianya.mrbuy.R;
import com.qingyuan.tianya.mrbuy.api.ApiConstant;
import com.qingyuan.tianya.mrbuy.api.HttpUtil;
import com.qingyuan.tianya.mrbuy.utils.StringUtil;
import com.qingyuan.tianya.mrbuy.utils.UploadUtil;
import com.qingyuan.tianya.mrbuy.utils.timedialogd.NumericWheelAdapter;
import com.qingyuan.tianya.mrbuy.utils.timedialogd.OnWheelScrollListener;
import com.qingyuan.tianya.mrbuy.utils.timedialogd.WheelView;
import com.qingyuan.tianya.mrbuy.view.popuwindow.TakePhotoPopuWindow;
import com.qingyuan.tianya.mrbuy.view.view.ActionSheet;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心
 */
public class PersonalInfoActivity extends BaseActivity implements ActionSheet.MenuItemClickListener {

    private SimpleDraweeView ac_info_head;
    private TextView ac_info_name_edi;
    private TextView ac_info_sex_text,ac_info_birthday_text;
    private TakePhotoPopuWindow menuWindow;
    /* 用来标识请求照相功能的activity */
    private static final int CAMERA_WITH_DATA = 3023;
    /* 用来标识请求gallery的activity */
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    /* 用来标识请求裁剪图片后的activity */
    private static final int CAMERA_CROP_DATA = 3022;
    // 照相机拍照得到的图片
    private String mFileName = System.currentTimeMillis() + ".jpg";
    private File mCurrentPhotoFile;
    private LinearLayout ll;
    private List<String> info = new ArrayList<>();//设置性别的集合
    private View view;
    private TextView bt_ok;
    private TextView bt_canle;
    private boolean flag =true;
    private AlertDialog dialog;
    private String m_id;
    private ImageView personal_name_img;
    private TextView ac_info_phone_text;
    private RelativeLayout ac_info_name_rel;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        addActivity(this);
        initView();
        initData();
        initClick();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.timedialog, null);
        bt_ok = (TextView) view.findViewById(R.id.bt_ok);
        bt_canle = (TextView) view.findViewById(R.id.bt_cancel);
        ac_info_head = ((SimpleDraweeView) findViewById(R.id.ac_info_head));
        ac_info_name_edi = ((TextView) findViewById(R.id.ac_info_name_edi));
        ac_info_name_rel = (RelativeLayout)findViewById(R.id.ac_info_name_rel);
        ac_info_sex_text = ((TextView) findViewById(R.id.ac_info_sex_text));
        ac_info_birthday_text = ((TextView) findViewById(R.id.ac_info_birthday_text));
        ac_info_phone_text = ((TextView) findViewById(R.id.ac_info_phone_text));
        personal_name_img = ((ImageView) findViewById(R.id.personal_name_img));
        ll = ((LinearLayout) findViewById(R.id.ll));
        mCurrentPhotoFile = new File(Environment.getExternalStorageDirectory(),
                mFileName);
    }

    @Override
    public void initData() {
        m_id = createSharedPreference(PersonalInfoActivity.this, "user_custId").getValue("custId");
        queryMessage();
        info.add("男");
        info.add("女");
    }

    private void queryMessage() {
        String urlString = ApiConstant.QUERYMESSAGE;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id);
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.trim());
                    Log.i("TAG", "昵称信息：" + jObj.toString());
                    if (jObj.getString("flag").equals("success")) {
                        //toast(jObj.getString("message"));
                        JSONObject object = jObj.getJSONObject("responseList");
                        username = object.getString("username");
                        String sex = object.getString("sex");
                        String stime = object.getString("stime");
                        //String province = object.getString("province");
                        //String city = object.getString("city");
                        String head_pic = object.getString("head_pic");
                        String phone = object.getString("account");
                        //String district = object.getString("district");
                        if (/*username != null && username!=""*/StringUtil.isNotEmpty(username)) {
                            ac_info_name_edi.setText(username);
                        }
                        if (!sex.equals("0")) {
                            if (Integer.valueOf(sex) == 1) {
                                ac_info_sex_text.setText("男");
                            } else if (Integer.valueOf(sex) == 2) {
                                ac_info_sex_text.setText("女");
                            }
                        }
                        if (!stime.equals("0")) {
                            String strTime = getStrTime(stime);
                            String birth = strTime.substring(0, 10);
                            ac_info_birthday_text.setText(birth);
                        }
                        if (head_pic != null) {
                            Uri uri = Uri.parse(head_pic);
                            ac_info_head.setImageURI(uri);
                        }
                        ac_info_phone_text.setText(phone);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.e("hck", " onFailure" + arg0.toString());
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }

        });
    }

    @Override
    public void initClick() {
        ac_info_head.setOnClickListener(this);
        ac_info_name_rel.setOnClickListener(this);
        findViewById(R.id.ac_info_name_rel).setOnClickListener(this);
        findViewById(R.id.ac_info_sex_rel).setOnClickListener(this);
        findViewById(R.id.ac_info_birthday_rel).setOnClickListener(this);
        findViewById(R.id.ac_info_out_rel).setOnClickListener(this);
        bt_canle.setOnClickListener(this);
        bt_ok.setOnClickListener(this);

    }

    /**
     *
     * 存储名字
     */
    private void getNicknameMessage(String nickname) {
        initProgressDialog();
        String urlString = ApiConstant.UPDATENICKNAME;
        RequestParams params = new RequestParams();
        params.put("username", nickname);
        params.put("m_id", m_id);
        HttpUtil.post(urlString, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject jObj = new JSONObject(jsonString);
                    if (jObj.getString("flag").equals("success")) {
                        toast(jObj.getString("message"));
                    } else {
                        toast(jObj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.e("hck", " onFailure" + arg0.toString());
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ac_info_name_rel:
               Bundle bundle = new Bundle();
                bundle.putString("name",username);
                skipActivity(this,UpdateNameActivity.class,bundle);
                break;
            case R.id.ac_info_head:
                menuWindow = new TakePhotoPopuWindow(this, this);
                // 显示窗口
                menuWindow.showAtLocation(this.findViewById(R.id.re_info),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ac_info_sex_rel:
                ll.removeView(view);
                flag=true;
                showActionSheet();
                break;
            case R.id.ac_info_birthday_rel:
                if (flag) {
                    ll.addView(getDataPick());
                    flag=false;
                }else{
                    ll.removeView(view);
                    flag=true;
                }
                break;
            case R.id.ac_info_out_rel:
                dialog = new AlertDialog.Builder(this).create();
                dialog.show();
                dialog.getWindow().setContentView(R.layout.dialog_out);
                dialog.getWindow().findViewById(R.id.dialog_out_diss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.getWindow().findViewById(R.id.dialog_out_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        loginout();
                    }
                });

                break;
            case R.id.iv_take_pic:
                doPickPhotoAction();
                menuWindow.dismiss();
                break;
            case R.id.iv_from_phone:
                pickPhoto();
                menuWindow.dismiss();
                break;
            case R.id.bt_ok:
                getUpdatebirthMessage(birthday);
                ll.removeView(view);
                break;
            case R.id.bt_cancel:
                flag=true;
                ll.removeView(view);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryMessage();
    }

    /**
     * 退出登录
     */
    private void loginout() {
        createSharedPreference(PersonalInfoActivity.this, "user_custId")
                .clearShared();
        skipActivityForClose(PersonalInfoActivity.this,
                HomeActivity.class, null);
    }

    /**
     * 存储生日
     */
    private void getUpdatebirthMessage(String birthday) {
        initProgressDialog();
        String stime = getTime(birthday);
        String urlString = ApiConstant.UPDATENICKNAME;
        RequestParams params = new RequestParams();
        params.put("stime", stime);
        params.put("m_id", m_id);
        HttpUtil.post(urlString, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String jObj) {

                Log.i("TAG", "生日信息：" + jObj);
                try {
                    JSONObject json = new JSONObject(jObj);
                    if (json.getString("flag").equals("success")) {
                        toast(json.getString("message"));

                    }else{
                        toast(json.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.e("hck", " onFailure" + arg0.toString());
                close();
            }


            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }

        });
    }

    private WheelView year;
    private WheelView month;
    private WheelView day;
    private String birthday;
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem() + 1950;// 年
            int n_month = month.getCurrentItem() + 1;// 月

            initDay(n_year, n_month);

            birthday = String.valueOf((year.getCurrentItem() + 1950)) + "年" + ((month.getCurrentItem() + 1) < 10 ? "0"
                    + (month.getCurrentItem() + 1) : (month
                    .getCurrentItem() + 1)) + "月" + (((day.getCurrentItem() + 1) < 10) ? "0"
                    + (day.getCurrentItem() + 1) : (day
                    .getCurrentItem() + 1))+"日"+"00时00分00秒";
                String birth = birthday.substring(0,10);
                ac_info_birthday_text.setText(birth);

        }
    };
    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);
        // int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        // int curDate = c.get(Calendar.DATE);

        int curYear = 1996;
        int mMonth = 0;
        int curMonth = mMonth + 1;
        int curDate = 1;
        year = (WheelView) view.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
                this, 1950, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(false);// 是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
                this, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(false);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(curYear, curMonth);
        day.setCyclic(false);
        day.addScrollingListener(scrollListener);

        year.setVisibleItems(7);// 设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        return view;
    }

    private void initDay(int curYear, int curMonth) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,
                1, getDay(curYear, curMonth), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }

    private int getDay(int year, int month) {
        int day;
        boolean flag;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    //设置性别
    public void showActionSheet() {
        ActionSheet menuView = new ActionSheet(this);
        menuView.setCancelButtonTitle("取消");// before add items
        menuView.addItems(info);
        menuView.setItemClickListener(this);
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
    }

    @Override
    public void onItemClick(int itemPosition) {
        initProgressDialog();
        ac_info_sex_text.setText(info.get(itemPosition));
        String urlString = ApiConstant.UPDATENICKNAME;
        RequestParams params = new RequestParams();
        params.put("m_id", m_id);
        if (info.get(itemPosition).equals("男")){
            params.put("sex", 1+"");
        }else if (info.get(itemPosition).equals("女")){
            params.put("sex", 2+"");
        }

        HttpUtil.post(urlString, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject jObj = new JSONObject(jsonString);
                    if (jObj.getString("flag").equals("success")) {
                        toast(jObj.getString("message"));
                    } else {
                        toast(jObj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    close();
                }
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                Log.e("hck", " onFailure" + arg0.toString());
                close();
            }

            public void onFinish() { // 完成后调用，失败，成功，都要掉
                close();
            }
        });
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        // 从相册中去获取
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            toast("没有找到照片");
        }
    }
    /**
     * 描述：从照相机获取
     */
    private void doPickPhotoAction() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            doTakePhoto();
        } else {
            toast("没有可用的存储卡");
        }
    }

    private void doTakePhoto() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(mCurrentPhotoFile));
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (Exception e) {
            toast("未找到系统相机程序");
        }
    }

    /**
     * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
     */
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent mIntent) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA:
                Uri uri = mIntent.getData();
                String currentFilePath = getPath(uri);
                if (StringUtil.isNotEmpty(currentFilePath)) {
                    Intent intent1 = new Intent(this, CropImageActivity.class);
                    intent1.putExtra("PATH", currentFilePath);
                    startActivityForResult(intent1, CAMERA_CROP_DATA);
                } else {
                    toast("未在存储卡中找到这个文件");
                }
                break;
            case CAMERA_WITH_DATA:
                String currentFilePath2 = mCurrentPhotoFile.getPath();
                if (StringUtil.isNotEmpty(currentFilePath2)) {
                    Intent intent2 = new Intent(this, CropImageActivity.class);
                    intent2.putExtra("PATH", currentFilePath2);
                    startActivityForResult(intent2, CAMERA_CROP_DATA);
                }

                break;
            case CAMERA_CROP_DATA:
                String path = mIntent.getStringExtra("PATH");
                toUpdateImage(path);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                ac_info_head.setImageBitmap(bitmap);
                break;
        }
    }

    /**
     * 从相册得到的url转换为SD卡中图片路径
     */
    public String getPath(Uri uri) {
        if (StringUtil.isEmpty(uri.getAuthority())) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     *
     * 存储头像
     */
    private void toUpdateImage(final String srcPath) {
        String fileKey = "head_pic";
        UploadUtil uploadUtil = new UploadUtil(this);
        Map<String, String> params = new HashMap<>();
        params.put("m_id", m_id);
        try {
            uploadUtil.uploadFile(new File(srcPath),fileKey,"http://114.215.78.102/index.php/Api/member/editMember",params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*String urlString = ApiConstant.UPDATENICKNAME;
        RequestParams params = new RequestParams();
        try {
            params.put("head_pic", srcPath);
            Log.i("ssssss", Base64.encodeFromFile(srcPath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        params.put("m_id", m_id);
        HttpUtil.post(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) { // 获取数据成功会调用这里
                Log.i("TAG", "修改头像成功！" + response);
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
            }

            public void onFailure(Throwable arg0) { // 失败，调用
                close();
                Log.e("hck", " onFailure" + arg0.toString());
            }

            public void onFinish() { // 完成后调用，失败，成功，都要调
                close();
            }


        });*/
    }
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Date d;
        try {

            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }
    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }
}
