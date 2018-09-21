package com.wqp.stadiumapp.entity;

import java.io.Serializable;

import android.graphics.Bitmap;

/** QQ��΢���ʻ���¼ʱʹ�õ�JavaBean*/
public class TPOSLoginBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Bitmap bitmap;//��¼��ͷ��
	private String nickname;//�ǳ�
	private int account;//�ʺ�
	 
	
	public TPOSLoginBean() {
		super();
	}
	public TPOSLoginBean(Bitmap bitmap, String nickname, int account) {
		super();
		this.bitmap = bitmap;
		this.nickname = nickname;
		this.account = account;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getAccount() {
		return account;
	}
	public void setAccount(int account) {
		this.account = account;
	}
 
}
