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

/** �û��������ݻظ�*/
public class UserService extends Service {
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
		Log.i("UserService","��ͨ�û�������������");
		nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Runnable r=new Runnable(){  
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
						List<Get5SecondUBean> result=WebGet5secondU.getGet5secondU(str);
						if(result!=null && result.size()>0){ 
							//��ѯ��������Ϣ��Ҫ������,��ʹ��֪ͨ���������Ѳ���
							Intent intent=new Intent(UserService.this,UserMessageProcess.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable("Get5SecondUBean", result.get(0)); 
							intent.putExtras(bundle); 
							pi=PendingIntent.getActivity(UserService.this, 0, intent, 0);
							
							mNotification=new Notification.Builder(UserService.this)
							.setAutoCancel(true)
							.setTicker("������Ϣ")
							.setContentTitle("����һ������Ϣ")
							.setContentText("�����鿴������")
							.setSmallIcon(R.drawable.user_test)
							.setWhen(System.currentTimeMillis())
							.setContentIntent(pi).build();	 
							
							//������Ϣ
							nm.notify(ID++, mNotification);
						}else{
							Log.i("Result","��ͨ�û�ÿ���5���ȡ������Ϊ��,��������Ϣ!");
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
	}

}
