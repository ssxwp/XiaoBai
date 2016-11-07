package com.qingyuan.tianya.mrbuy.utils;


import com.qingyuan.tianya.mrbuy.bean.LoginBean;
import com.qingyuan.tianya.mrbuy.db.SharedPreferenceHelper;

public class KeepLogMsg {

	public KeepLogMsg() {
		
	}
	
	public static void getKeepLogMsg(LoginBean loginBean,SharedPreferenceHelper userMsg) {
		userMsg.putValue("m_id", loginBean.getM_id());
		userMsg.putValue("username", loginBean.getUsername());
		userMsg.putValue("sex", loginBean.getSex());
		userMsg.putValue("head_pic", loginBean.getHead_pic());
		userMsg.putValue("stime", loginBean.getStime());
		userMsg.putValue("province", loginBean.getProvince());
		userMsg.putValue("city", loginBean.getCity());
		userMsg.putValue("district", loginBean.getDistrict());
	}

}
