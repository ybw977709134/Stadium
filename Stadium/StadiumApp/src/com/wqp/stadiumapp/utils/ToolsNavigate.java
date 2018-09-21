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
	public static TPOSLoginBean TPOS_LOG=null;//�û�ʹ��TPOS��¼�ı�ʶ
	
	
	private static final String TAG="ToolsNavigate";
	public static final int TPOS_LOGIN_RESULT=0x01;//TPOS��¼���صĽ����ʶ
	public static final int TPOS_LOGIN_BITMAP=0x02;//TPOS��¼���صĽ��������ͷ���ʶ
	
	
	
	
	/**QQ��¼ʹ��,��ָ������ͼƬ��Uri·������ͼƬ��Դ��Bitmap��ʽ����*/
	public static Bitmap getBitmap(String imageUri) {
		Log.v(TAG, "getBitmap:" + imageUri);
		//��ʾ�����ϵ�ͼƬ
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
     * �жϵ�ǰ�����Ƿ����
     * @return ��ǰ�����״̬
     */
    public static boolean isNetworkAvailable(Context context){
    	//��ȡ�ֻ��������ӹ������(����wi-fi,net�����ӹ���)
    	ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if(connectivityManager==null){
    		return false;
    	}else{
    		// ��ȡNetworkInfo����
    		NetworkInfo[] networkInfo=connectivityManager.getAllNetworkInfo();
    		if(networkInfo!=null && networkInfo.length > 0){
    			for(int i=0;i<networkInfo.length;i++){
    				// �жϵ�ǰ����״̬�Ƿ�Ϊ����״̬
    				if(networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }
	
}