package com.wqp.stadiumapp.utils; 
import android.app.Activity; 
import android.content.Context; 
import android.util.DisplayMetrics; 


public class UtilScreen{
	
	/**
	 * 返回屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getWindowWidth(Context context){
		DisplayMetrics dm = new android.util.DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
	}
	/**
	 * 返回屏幕高度
	 * @param context
	 * @return
	 */
	public static int getWindowHeight(Context context){
		DisplayMetrics dm = new android.util.DisplayMetrics();   
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
	}
	
}