package com.wqp.sina;

import com.sina.weibo.sdk.auth.Oauth2AccessToken; 

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor; 

public class SinaAccessTokenKeeper {
	private static final String PREFERENCES_NAME = "sina_weibo_token";//数据存在共享者中的名称
	private static final String UID = "uid";
	private static final String ACCESS_TOKEN  = "access_token";
	private static final String EXPIRES_IN = "expires_in";

	/** 保存 access_token 对象到 SharedPreferences*/
	public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (null == context || null == token) { return;  } 
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(UID, token.getUid());
        editor.putString(ACCESS_TOKEN, token.getToken());
        editor.putLong(EXPIRES_IN, token.getExpiresTime());
        editor.commit();
    }
	
	/**  从 SharedPreferences 读取 access_token 信息*/
	public static Oauth2AccessToken readAccessToken(Context context) {
	    if (null == context) {return null;}
	    Oauth2AccessToken token = new Oauth2AccessToken();
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	    token.setUid(pref.getString(UID, ""));
	    token.setToken(pref.getString(ACCESS_TOKEN, ""));
	    token.setExpiresTime(pref.getLong(EXPIRES_IN, 0));
	    return token;
	}

	/** 清空 SharedPreferences 中 access_token信息*/
	public static void clear(Context context) {
        if (null == context) { return; } 
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
 
	
	
}
