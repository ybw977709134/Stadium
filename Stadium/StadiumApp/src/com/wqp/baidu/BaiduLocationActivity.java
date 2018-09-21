package com.wqp.baidu;

import com.baidu.location.BDLocation; 
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.wqp.stadiumapp.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class BaiduLocationActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	private LocationClient mLocationClient;//百度SDK定位
	private LocationMode mLocationMode=LocationMode.Hight_Accuracy;//定位的模式,默认设置，可以进行修改
	private String mCoordinate="bd09ll";//坐标系,默认设置
	
	private RadioGroup baidu_location_mode,baidu_coordinate_mode;//定位模式，坐标系模式
	private EditText baidu_location_frequence;//间隔时间,频率
	private CheckBox baidu_geographic_code_mode;//反地理编码
	private Button baidu_location_toggle;//开启、关闭定位按钮
	private TextView baidu_location_information;//显示定位后的结果 
	private StringBuilder locationSet=new StringBuilder();//存放定位之后返回的结果集 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidu_sdk_location);
		
		mLocationClient=new LocationClient(getApplicationContext());//获取百度定位实例
		mLocationClient.registerLocationListener(new BaiduBDLocationListener());//给定位注册监听
		
		initView();//获取到界面显示的组件 
		
	}
	
	/** 获取到界面上的组件，并为按钮绑定点击事件*/
	private void initView(){
		baidu_location_mode=(RadioGroup) findViewById(R.id.baidu_location_mode);
		baidu_coordinate_mode=(RadioGroup) findViewById(R.id.baidu_coordinate_mode);
		baidu_location_frequence=(EditText) findViewById(R.id.baidu_location_frequence);
		baidu_geographic_code_mode=(CheckBox) findViewById(R.id.baidu_geographic_code_mode);
		baidu_location_toggle=(Button) findViewById(R.id.baidu_location_toggle);
		baidu_location_information=(TextView) findViewById(R.id.baidu_location_information);
		baidu_location_toggle.setOnClickListener(this);//为按钮绑定事件
		
		baidu_location_mode.setOnCheckedChangeListener(this);//为定位模式的RadioGroup设置监听
		baidu_coordinate_mode.setOnCheckedChangeListener(this);//为坐标系的RadioGroup设置监听
	}
	
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.baidu_location_toggle://开启、关闭定位功能按钮
			if(baidu_location_toggle.getText().toString().equals(getString(R.string.startlocation))){//如果开关按钮上面显示的是开启定位，点击后就进行入定位开启了	
				initBaiduLocation();
				mLocationClient.start();
				baidu_location_toggle.setText(R.string.stoplocation);//把开关按钮上设置成 关闭定位的字样
			}else{//否则就关闭定位功能
				mLocationClient.stop();
				baidu_location_toggle.setText(R.string.startlocation);//把开关按钮上设置成 开启定位的字样
			} 	
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(group.getId()){
		case R.id.baidu_location_mode://定位模式的RadioGroup
			switch(checkedId){
			case R.id.baidu_location_mode_hight://高精度
				mLocationMode=LocationMode.Hight_Accuracy;
				break;
			case R.id.baidu_location_mode_low://低功耗
				mLocationMode=LocationMode.Battery_Saving;
				break;
			case R.id.baidu_location_mode_device://仅设备
				mLocationMode=LocationMode.Device_Sensors;
				break;
			}
			break;
		case R.id.baidu_coordinate_mode://坐标系的RadioGroup
			switch(checkedId){
			case R.id.baidu_coordinate_mode_gcj02://国测局
				mCoordinate="gcj02";
				break;
			case R.id.baidu_coordinate_mode_bd09ll://百度加密经纬度
				mCoordinate="bd09ll";
			case R.id.baidu_coordinate_mode_bd09://百度加密墨卡托
				mCoordinate="bd09";
			}
			break;
		} 
	}
	
	/** 对百度定位进行初始化*/
	private void initBaiduLocation(){
		LocationClientOption options=new LocationClientOption();
		options.setLocationMode(mLocationMode);//设置定位的模式
		options.setCoorType(mCoordinate);//设置返回定位结果坐标系的类型，默认是 百度加密经纬度
		options.setAddrType("all");//值为 all时，表示返回地址信息，其他值都表示不返回地址信息。 
		options.setOpenGps(true);//是否打开GPS
		options.setIsNeedAddress(baidu_geographic_code_mode.isChecked());//是否反地量编码(就是把坐标值转换成实际详细地址)
		int span=1000;
		try{
			//从用户输入的间隔时间中获取到参数
			span=Integer.valueOf(baidu_location_frequence.getText().toString().trim()).intValue();
		}catch(Exception e){
			e.printStackTrace();
		}
		options.setScanSpan(span);//设置发起定位请求的间隔时间为1000ms
		mLocationClient.setLocOption(options); 
	}
	
	/** 这个方法是给定位进行注册使用的，时面的回调方法带有根据options参数配置后返回的结果*/
	private class BaiduBDLocationListener implements com.baidu.location.BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation locationResult) { 
			if(locationResult==null) return;
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
				baidu_location_information.setText(locationSet.toString());
				Log.i("BaiduLocation",locationSet.toString());
			} 

			locationSet.delete(0, locationSet.length());//清空字符串
		} 
		 
	}
	
	
	@Override
	protected void onStop() { 
		super.onStop();
		mLocationClient.stop();//停止定位
		baidu_location_toggle.setText(R.string.startlocation);//把开关按钮上设置成 开启定位的字样
	}
	 
}
