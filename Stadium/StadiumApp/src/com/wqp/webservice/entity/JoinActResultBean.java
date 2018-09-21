package com.wqp.webservice.entity;

import java.io.Serializable;

import android.view.View;

public class JoinActResultBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private boolean UserExist;//加入用户是否存在
	private String NoName;//用户名是否存在
	private String OnName;//是否已经加入活动了 
	private View mView;//用户成功添加活动之后需要更新图标
	private GetActInfoBean mGetActInfoBean;//保存用户成功加入应战之后的条目数据
	private int mPosition;//数据的编号 
	 
	
	public int getmPosition() {
		return mPosition;
	}
	public void setmPosition(int mPosition) {
		this.mPosition = mPosition;
	}
	public boolean isUserExist() {
		return UserExist;
	}
	public void setUserExist(boolean userExist) {
		UserExist = userExist;
	}
	public GetActInfoBean getmGetActInfoBean() {
		return mGetActInfoBean;
	}
	public void setmGetActInfoBean(GetActInfoBean mGetActInfoBean) {
		this.mGetActInfoBean = mGetActInfoBean;
	}
	public View getmView() {
		return mView;
	}
	public void setmView(View mView) {
		this.mView = mView;
	}
	 
	public String getNoName() {
		return NoName;
	}
	public void setNoName(String noName) {
		NoName = noName;
	}
	public String getOnName() {
		return OnName;
	}
	public void setOnName(String onName) {
		OnName = onName;
	}
	
	
	
	
}
