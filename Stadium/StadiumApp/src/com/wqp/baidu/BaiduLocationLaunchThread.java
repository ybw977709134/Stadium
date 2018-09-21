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
	private LocationClient mLocationClient;//百度SDK定位
	private LocationMode mLocationMode=LocationMode.Hight_Accuracy;//定位的模式,默认设置，可以进行修改
	private String mCoordinate="bd09ll";//坐标系,默认设置 
	private StringBuilder locationSet=new StringBuilder();//存放定位之后返回的结果集   
	
	public BaiduLocationLaunchThread(Context context,Handler handler,LocationClient mLocationClient){
		this.context=context;
		this.mHandler=handler;
		this.mLocationClient=mLocationClient;
	}
	
	
	@Override
	public void run() {  
		mLocationClient.registerLocationListener(new BaiduBDLocationListener());//给定位注册监听 
		initBaiduLocation();
		mLocationClient.requestLocation();
		mLocationClient.start(); 
	}
	
	/** 对百度定位进行初始化*/
	private void initBaiduLocation(){
		LocationClientOption options=new LocationClientOption();
		options.setLocationMode(mLocationMode);//设置定位的模式
		options.setCoorType(mCoordinate);//设置返回定位结果坐标系的类型，默认是 百度加密经纬度
		options.setAddrType("all");//值为 all时，表示返回地址信息，其他值都表示不返回地址信息。 
		options.setOpenGps(true);//是否打开GPS
		options.setIsNeedAddress(true);//是否反地量编码(就是把坐标值转换成实际详细地址) 
		options.setScanSpan(5000);//设置发起定位请求的间隔时间为1000ms
		mLocationClient.setLocOption(options); 
	}
	
	/** 这个方法是给定位进行注册使用的，时面的回调方法带有根据options参数配置后返回的结果*/
	private class BaiduBDLocationListener implements com.baidu.location.BDLocationListener{
 
		@Override
		public void onReceiveLocation(BDLocation locationResult) { 
			if(locationResult==null){ 
				Message msg=new Message();
				msg.obj="自动定位失败";
				msg.what=ToolsHome.AUTO_LOCATION_FAILURE;
				mHandler.sendMessage(msg); 
				return;
			}
			locationSet.append("Time:");
			locationSet.append(locationResult.getTime());//时期和时间
			locationSet.append("\nError Code:");
			locationSet.append(locationResult.getLocType());//返回的编响应号
			locationSet.append("\nLatitude:");
			locationSet.append(locationResult.getLatitude());//纬度
			locationSet.append("\nLongitude:");
			locationSet.append(locationResult.getLongitude());//经度
			locationSet.append("\nRadius:");
			locationSet.append(locationResult.getRadius());//半径
			locationSet.append("\nDirection:");
			locationSet.append(locationResult.getDirection());//获取手机方向信息
			if(locationResult.getLocType()==BDLocation.TypeGpsLocation){//如果是GPS进行的定位
				locationSet.append("\nSpeed:");
				locationSet.append(locationResult.getSpeed());//获取 
				locationSet.append("\nSatelliteNumber:");
				locationSet.append(locationResult.getSatelliteNumber()); //获取 
				
				locationSet.append("\nAddress:");
				locationSet.append(locationResult.getAddrStr());//获取到详细地址信息
				
				locationSet.append("\nProvince:");
				locationSet.append(locationResult.getProvince());//获取到省份信息 
				
				locationSet.append("\nCity:");
				locationSet.append(locationResult.getCity());//获取到城市信息
				locationSet.append("\nCityCode:");
				locationSet.append(locationResult.getCityCode());//获取到城市编码信息
				
				locationSet.append("\nDistrict:");
				locationSet.append(locationResult.getDistrict());//获取到区县信息  
				
				locationSet.append("\nOperationers : ");
				locationSet.append(locationResult.getOperators());//运营商信息
				
			}else if(locationResult.getLocType()==BDLocation.TypeNetWorkLocation){ //如果是网络定位
				locationSet.append("\nSpeed:");
				locationSet.append(locationResult.getSpeed());//获取 
				locationSet.append("\nSatelliteNumber:");
				locationSet.append(locationResult.getSatelliteNumber()); //获取 
				
				locationSet.append("\nAddress:");
				locationSet.append(locationResult.getAddrStr());//获取到详细地址信息
				
				locationSet.append("\nProvince:");
				locationSet.append(locationResult.getProvince());//获取到省份信息 
				
				locationSet.append("\nCity:");
				locationSet.append(locationResult.getCity());//获取到城市信息
				locationSet.append("\nCityCode:");
				locationSet.append(locationResult.getCityCode());//获取到城市编码信息
				
				locationSet.append("\nDistrict:");
				locationSet.append(locationResult.getDistrict());//获取到区县信息 
				
				locationSet.append("\nOperationers : ");
				locationSet.append(locationResult.getOperators());//运营商信息 
			}
			
			//显示返回的数据
			if(locationSet!=null){  
				Log.i("BaiduLocation",locationSet.toString()); 
			}  
			locationSet.delete(0, locationSet.length());//清空字符串
			
			//把定位到的地址显示到主界面的所在地位置 
	        ToolsHome.LOCATION_CITY=locationResult.getCity();//获取到定位成功城市名称 
	        ToolsHome.IS_LOCATION=true;//设置为已定位标识
	        
			Message msg=new Message();
			msg.obj=ToolsHome.LOCATION_CITY;
			msg.what=ToolsHome.AUTO_LOCATION_SUCCESS;
			mHandler.sendMessage(msg); 
			
			mLocationClient.stop();//停止定位  
		} 
		 
	} 
	 
}
