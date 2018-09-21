package com.wqp.baidu;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log; 

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.wqp.stadiumapp.utils.ToolsHome;

public class BaiduLocationLaunchThread extends Thread {
	@SuppressWarnings("unused")
	private Context context;
	private Handler mHandler;
	private LocationClient mLocationClient;//�ٶ�SDK��λ
	private LocationMode mLocationMode=LocationMode.Hight_Accuracy;//��λ��ģʽ,Ĭ�����ã����Խ����޸�
	private String mCoordinate="bd09ll";//����ϵ,Ĭ������ 
	private StringBuilder locationSet=new StringBuilder();//��Ŷ�λ֮�󷵻صĽ����   
	
	public BaiduLocationLaunchThread(Context context,Handler handler,LocationClient mLocationClient){
		this.context=context;
		this.mHandler=handler;
		this.mLocationClient=mLocationClient;
	}
	
	
	@Override
	public void run() {  
		mLocationClient.registerLocationListener(new BaiduBDLocationListener());//����λע����� 
		initBaiduLocation();
		mLocationClient.requestLocation();
		mLocationClient.start(); 
	}
	
	/** �԰ٶȶ�λ���г�ʼ��*/
	private void initBaiduLocation(){
		LocationClientOption options=new LocationClientOption();
		options.setLocationMode(mLocationMode);//���ö�λ��ģʽ
		options.setCoorType(mCoordinate);//���÷��ض�λ�������ϵ�����ͣ�Ĭ���� �ٶȼ��ܾ�γ��
		options.setAddrType("all");//ֵΪ allʱ����ʾ���ص�ַ��Ϣ������ֵ����ʾ�����ص�ַ��Ϣ�� 
		options.setOpenGps(true);//�Ƿ��GPS
		options.setIsNeedAddress(true);//�Ƿ񷴵�������(���ǰ�����ֵת����ʵ����ϸ��ַ) 
		options.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ1000ms
		mLocationClient.setLocOption(options); 
	}
	
	/** ��������Ǹ���λ����ע��ʹ�õģ�ʱ��Ļص��������и���options�������ú󷵻صĽ��*/
	private class BaiduBDLocationListener implements com.baidu.location.BDLocationListener{
 
		@Override
		public void onReceiveLocation(BDLocation locationResult) { 
			if(locationResult==null){ 
				Message msg=new Message();
				msg.obj="�Զ���λʧ��";
				msg.what=ToolsHome.AUTO_LOCATION_FAILURE;
				mHandler.sendMessage(msg); 
				return;
			}
			locationSet.append("Time:");
			locationSet.append(locationResult.getTime());//ʱ�ں�ʱ��
			locationSet.append("\nError Code:");
			locationSet.append(locationResult.getLocType());//���صı���Ӧ��
			locationSet.append("\nLatitude:");
			locationSet.append(locationResult.getLatitude());//γ��
			locationSet.append("\nLongitude:");
			locationSet.append(locationResult.getLongitude());//����
			locationSet.append("\nRadius:");
			locationSet.append(locationResult.getRadius());//�뾶
			locationSet.append("\nDirection:");
			locationSet.append(locationResult.getDirection());//��ȡ�ֻ�������Ϣ
			if(locationResult.getLocType()==BDLocation.TypeGpsLocation){//�����GPS���еĶ�λ
				locationSet.append("\nSpeed:");
				locationSet.append(locationResult.getSpeed());//��ȡ 
				locationSet.append("\nSatelliteNumber:");
				locationSet.append(locationResult.getSatelliteNumber()); //��ȡ 
				
				locationSet.append("\nAddress:");
				locationSet.append(locationResult.getAddrStr());//��ȡ����ϸ��ַ��Ϣ
				
				locationSet.append("\nProvince:");
				locationSet.append(locationResult.getProvince());//��ȡ��ʡ����Ϣ 
				
				locationSet.append("\nCity:");
				locationSet.append(locationResult.getCity());//��ȡ��������Ϣ
				locationSet.append("\nCityCode:");
				locationSet.append(locationResult.getCityCode());//��ȡ�����б�����Ϣ
				
				locationSet.append("\nDistrict:");
				locationSet.append(locationResult.getDistrict());//��ȡ��������Ϣ  
				
				locationSet.append("\nOperationers : ");
				locationSet.append(locationResult.getOperators());//��Ӫ����Ϣ
				
			}else if(locationResult.getLocType()==BDLocation.TypeNetWorkLocation){ //��������綨λ
				locationSet.append("\nSpeed:");
				locationSet.append(locationResult.getSpeed());//��ȡ 
				locationSet.append("\nSatelliteNumber:");
				locationSet.append(locationResult.getSatelliteNumber()); //��ȡ 
				
				locationSet.append("\nAddress:");
				locationSet.append(locationResult.getAddrStr());//��ȡ����ϸ��ַ��Ϣ
				
				locationSet.append("\nProvince:");
				locationSet.append(locationResult.getProvince());//��ȡ��ʡ����Ϣ 
				
				locationSet.append("\nCity:");
				locationSet.append(locationResult.getCity());//��ȡ��������Ϣ
				locationSet.append("\nCityCode:");
				locationSet.append(locationResult.getCityCode());//��ȡ�����б�����Ϣ
				
				locationSet.append("\nDistrict:");
				locationSet.append(locationResult.getDistrict());//��ȡ��������Ϣ 
				
				locationSet.append("\nOperationers : ");
				locationSet.append(locationResult.getOperators());//��Ӫ����Ϣ 
			}
			
			//��ʾ���ص�����
			if(locationSet!=null){  
				Log.i("BaiduLocation",locationSet.toString()); 
			}  
			locationSet.delete(0, locationSet.length());//����ַ���
			
			//�Ѷ�λ���ĵ�ַ��ʾ������������ڵ�λ�� 
	        ToolsHome.LOCATION_CITY=locationResult.getCity();//��ȡ����λ�ɹ��������� 
	        ToolsHome.IS_LOCATION=true;//����Ϊ�Ѷ�λ��ʶ
	        
			Message msg=new Message();
			msg.obj=ToolsHome.LOCATION_CITY;
			msg.what=ToolsHome.AUTO_LOCATION_SUCCESS;
			mHandler.sendMessage(msg); 
			
			mLocationClient.stop();//ֹͣ��λ  
		} 
		 
	} 
	 
}
