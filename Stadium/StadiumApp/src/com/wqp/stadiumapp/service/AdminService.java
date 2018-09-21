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
	
	private NotificationManager nm;//֪ͨ������
	private Notification mNotification;
	private PendingIntent pi;
	
	@Override
	public IBinder onBind(Intent intent) { 
		return null;
	}
	
	@Override
	public void onCreate() { 
		super.onCreate();
		Log.i("AdminService","����Ա�û����������Ѿ�������");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Runnable r=new Runnable(){ 
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				while(FLAG){
					try {
						//��ͣ20��
						Thread.sleep(20000);
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}
					
					try {
						//��ȡ����˵Ļظ��û��Ĳ�����Ϣ
						JSONObject object=new JSONObject();
						object.put("UserID", UserGlobal.UserID);
						String str=object.toString();
						Log.i("UserService","������:"+str);
						String result=WebGet5secondV.getGet5secondV(str);
						if(!TextUtils.isEmpty(result)){
							if(result.equalsIgnoreCase("����Ϣ")){
								//����Ϣ�����κβ���
								Log.i("UserService","���յ����������Ϊ��"+result+";������֪ͨ");
							}else{
								Log.i("UserService","���յ����������Ϊ��"+result);
								//��ѯ��������Ϣ��Ҫ������,��ʹ��֪ͨ���������Ѳ���
								mNotification=new Notification();	
								mNotification.icon=R.drawable.user_test;
								mNotification.tickerText="���û��ύ������Ϣ����";
								mNotification.when=System.currentTimeMillis();
								//�����mNotification֮�󣬸�mNotification�Զ���ʧ
								mNotification.flags=Notification.FLAG_AUTO_CANCEL;
								Intent intent=new Intent(AdminService.this,AdminMessageProcess.class);
								intent.putExtra("message", result);
								
								pi=PendingIntent.getActivity(AdminService.this, 0, intent, 0);
								mNotification.setLatestEventInfo(AdminService.this, "�û���Ϣ", result, pi);
								nm.notify(ID++, mNotification); 
							}
						} 
					} catch (JSONException e) { 
						e.printStackTrace();
					} 
					
				} 
			} 
		};
		
		//�����̴߳���
		new Thread(r).start();
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() { 
		super.onDestroy();
		//���ٷ���
	}

}
