package com.wqp.webservice.entity;

import java.io.Serializable;

import android.view.View;

public class JoinActResultBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private boolean UserExist;//�����û��Ƿ����
	private String NoName;//�û����Ƿ����
	private String OnName;//�Ƿ��Ѿ������� 
	private View mView;//�û��ɹ���ӻ֮����Ҫ����ͼ��
	private GetActInfoBean mGetActInfoBean;//�����û��ɹ�����Ӧս֮�����Ŀ����
	private int mPosition;//���ݵı�� 
	 
	
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
