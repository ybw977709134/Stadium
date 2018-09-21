package com.wqp.stadiumapp.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.wqp.stadiumapp.AdminMessageProcess;
import com.wqp.stadiumapp.R; 
import com.wqp.stadiumapp.utils.UserGlobal; 
import com.wqp.webservice.WebGet5secondV;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class AdminService extends Service {
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
		Log.i("AdminService","管理员用户监听服务已经开启了");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Runnable r=new Runnable(){ 
			@SuppressWarnings("deprecation")
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
						String result=WebGet5secondV.getGet5secondV(str);
						if(!TextUtils.isEmpty(result)){
							if(result.equalsIgnoreCase("无消息")){
								//无消息不做任何操作
								Log.i("UserService","接收到服务端数据为："+result+";不发送通知");
							}else{
								Log.i("UserService","接收到服务端数据为："+result);
								//查询到有新消息需要处理了,就使用通知栏进行提醒操作
								mNotification=new Notification();	
								mNotification.icon=R.drawable.user_test;
								mNotification.tickerText="有用户提交了新消息……";
								mNotification.when=System.currentTimeMillis();
								//点击了mNotification之后，该mNotification自动消失
								mNotification.flags=Notification.FLAG_AUTO_CANCEL;
								Intent intent=new Intent(AdminService.this,AdminMessageProcess.class);
								intent.putExtra("message", result);
								
								pi=PendingIntent.getActivity(AdminService.this, 0, intent, 0);
								mNotification.setLatestEventInfo(AdminService.this, "用户消息", result, pi);
								nm.notify(ID++, mNotification); 
							}
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
		//销毁服务
	}

}
