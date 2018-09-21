package com.wqp.stadiumapp.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.wqp.stadiumapp.R;
import com.wqp.stadiumapp.UserMessageProcess;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.webservice.WebGet5secondU;
import com.wqp.webservice.entity.Get5SecondUBean;

/** 用户监听场馆回复*/
public class UserService extends Service {
	public static boolean FLAG=true;
	public int ID=0;
	
	private NotificationManager nm;//通知管理器
	private Notification mNotification;
	private PendingIntent pi;
	
	@Override
	public IBinder onBind(Intent intent) { 
		return null;
	}
	
	@Override
	public void onCreate() { 
		super.onCreate();
		Log.i("UserService","普通用户监听服务开启了");
		nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Runnable r=new Runnable(){  
			@Override
			public void run() {
				while(FLAG){
					try {
						//暂停20秒
						Thread.sleep(20000);
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}
					
					try {
						//获取服务端的回复用户的操作信息
						JSONObject object=new JSONObject();
						object.put("UserID", UserGlobal.UserID);
						String str=object.toString();
						Log.i("UserService","参数是:"+str);
						List<Get5SecondUBean> result=WebGet5secondU.getGet5secondU(str);
						if(result!=null && result.size()>0){ 
							//查询到有新消息需要处理了,就使用通知栏进行提醒操作
							Intent intent=new Intent(UserService.this,UserMessageProcess.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable("Get5SecondUBean", result.get(0)); 
							intent.putExtras(bundle); 
							pi=PendingIntent.getActivity(UserService.this, 0, intent, 0);
							
							mNotification=new Notification.Builder(UserService.this)
							.setAutoCancel(true)
							.setTicker("场馆消息")
							.setContentTitle("您有一条新消息")
							.setContentText("点击后查看地详情")
							.setSmallIcon(R.drawable.user_test)
							.setWhen(System.currentTimeMillis())
							.setContentIntent(pi).build();	 
							
							//发送消息
							nm.notify(ID++, mNotification);
						}else{
							Log.i("Result","普通用户每间隔5秒获取到数据为空,暂无新消息!");
						}
					} catch (JSONException e) { 
						e.printStackTrace(); 
					}  
				} 
			} 
		};
		
		//开启线程处理
		new Thread(r).start();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}

}
