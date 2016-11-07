package com.qingyuan.tianya.mrbuy.application;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.facebook.drawee.backends.pipeline.Fresco;


/**
 * Created by gaoyanjun on 2016/4/19.
 */
public class MrBuyApplication extends Application {
    private static MrBuyApplication context;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        context = this;
    }
    public static MrBuyApplication getAppContext() {
        return context;
    }
    public  String getVersion() {
        String version = "0.0.0";
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
            //version=packageInfo.v
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
    public int getCode(){
        int code =0;
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            code = packageInfo.versionCode;
            //version=packageInfo.v
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }
}
