package com.wqp.stadiumapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.wqp.stadiumapp.entity.TPOSLoginBean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ToolsNavigate { 	
	public static TPOSLoginBean TPOS_LOG=null;//用户使用TPOS登录的标识
	
	
	private static final String TAG="ToolsNavigate";
	public static final int TPOS_LOGIN_RESULT=0x01;//TPOS登录返回的结果标识
	public static final int TPOS_LOGIN_BITMAP=0x02;//TPOS登录返回的结果解析的头像标识
	
	
	
	
	/**QQ登录使用,从指定网络图片的Uri路径加载图片资源以Bitmap格式返回*/
	public static Bitmap getBitmap(String imageUri) {
		Log.v(TAG, "getBitmap:" + imageUri);
		//显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Log.v(TAG, "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			Log.v(TAG, "TEST >>> getbitmap bmp fail---");
			return null;
		}
		return bitmap;
	}
	
	
	/**
     * 判断当前网络是否可用
     * @return 当前网络的状态
     */
    public static boolean isNetworkAvailable(Context context){
    	//获取手机所有连接管理对象(包括wi-fi,net等连接管理)
    	ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if(connectivityManager==null){
    		return false;
    	}else{
    		// 获取NetworkInfo对象
    		NetworkInfo[] networkInfo=connectivityManager.getAllNetworkInfo();
    		if(networkInfo!=null && networkInfo.length > 0){
    			for(int i=0;i<networkInfo.length;i++){
    				// 判断当前网络状态是否为连接状态
    				if(networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }
	
}