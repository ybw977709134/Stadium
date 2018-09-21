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
	private LocationClient mLocationClient;//�ٶ�SDK��λ
	private LocationMode mLocationMode=LocationMode.Hight_Accuracy;//��λ��ģʽ,Ĭ�����ã����Խ����޸�
	private String mCoordinate="bd09ll";//����ϵ,Ĭ������
	
	private RadioGroup baidu_location_mode,baidu_coordinate_mode;//��λģʽ������ϵģʽ
	private EditText baidu_location_frequence;//���ʱ��,Ƶ��
	private CheckBox baidu_geographic_code_mode;//���������
	private Button baidu_location_toggle;//�������رն�λ��ť
	private TextView baidu_location_information;//��ʾ��λ��Ľ�� 
	private StringBuilder locationSet=new StringBuilder();//��Ŷ�λ֮�󷵻صĽ���� 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidu_sdk_location);
		
		mLocationClient=new LocationClient(getApplicationContext());//��ȡ�ٶȶ�λʵ��
		mLocationClient.registerLocationListener(new BaiduBDLocationListener());//����λע�����
		
		initView();//��ȡ��������ʾ����� 
		
	}
	
	/** ��ȡ�������ϵ��������Ϊ��ť�󶨵���¼�*/
	private void initView(){
		baidu_location_mode=(RadioGroup) findViewById(R.id.baidu_location_mode);
		baidu_coordinate_mode=(RadioGroup) findViewById(R.id.baidu_coordinate_mode);
		baidu_location_frequence=(EditText) findViewById(R.id.baidu_location_frequence);
		baidu_geographic_code_mode=(CheckBox) findViewById(R.id.baidu_geographic_code_mode);
		baidu_location_toggle=(Button) findViewById(R.id.baidu_location_toggle);
		baidu_location_information=(TextView) findViewById(R.id.baidu_location_information);
		baidu_location_toggle.setOnClickListener(this);//Ϊ��ť���¼�
		
		baidu_location_mode.setOnCheckedChangeListener(this);//Ϊ��λģʽ��RadioGroup���ü���
		baidu_coordinate_mode.setOnCheckedChangeListener(this);//Ϊ����ϵ��RadioGroup���ü���
	}
	
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.baidu_location_toggle://�������رն�λ���ܰ�ť
			if(baidu_location_toggle.getText().toString().equals(getString(R.string.startlocation))){//������ذ�ť������ʾ���ǿ�����λ�������ͽ����붨λ������	
				initBaiduLocation();
				mLocationClient.start();
				baidu_location_toggle.setText(R.string.stoplocation);//�ѿ��ذ�ť�����ó� �رն�λ������
			}else{//����͹رն�λ����
				mLocationClient.stop();
				baidu_location_toggle.setText(R.string.startlocation);//�ѿ��ذ�ť�����ó� ������λ������
			} 	
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(group.getId()){
		case R.id.baidu_location_mode://��λģʽ��RadioGroup
			switch(checkedId){
			case R.id.baidu_location_mode_hight://�߾���
				mLocationMode=LocationMode.Hight_Accuracy;
				break;
			case R.id.baidu_location_mode_low://�͹���
				mLocationMode=LocationMode.Battery_Saving;
				break;
			case R.id.baidu_location_mode_device://���豸
				mLocationMode=LocationMode.Device_Sensors;
				break;
			}
			break;
		case R.id.baidu_coordinate_mode://����ϵ��RadioGroup
			switch(checkedId){
			case R.id.baidu_coordinate_mode_gcj02://�����
				mCoordinate="gcj02";
				break;
			case R.id.baidu_coordinate_mode_bd09ll://�ٶȼ��ܾ�γ��
				mCoordinate="bd09ll";
			case R.id.baidu_coordinate_mode_bd09://�ٶȼ���ī����
				mCoordinate="bd09";
			}
			break;
		} 
	}
	
	/** �԰ٶȶ�λ���г�ʼ��*/
	private void initBaiduLocation(){
		LocationClientOption options=new LocationClientOption();
		options.setLocationMode(mLocationMode);//���ö�λ��ģʽ
		options.setCoorType(mCoordinate);//���÷��ض�λ�������ϵ�����ͣ�Ĭ���� �ٶȼ��ܾ�γ��
		options.setAddrType("all");//ֵΪ allʱ����ʾ���ص�ַ��Ϣ������ֵ����ʾ�����ص�ַ��Ϣ�� 
		options.setOpenGps(true);//�Ƿ��GPS
		options.setIsNeedAddress(baidu_geographic_code_mode.isChecked());//�Ƿ񷴵�������(���ǰ�����ֵת����ʵ����ϸ��ַ)
		int span=1000;
		try{
			//���û�����ļ��ʱ���л�ȡ������
			span=Integer.valueOf(baidu_location_frequence.getText().toString().trim()).intValue();
		}catch(Exception e){
			e.printStackTrace();
		}
		options.setScanSpan(span);//���÷���λ����ļ��ʱ��Ϊ1000ms
		mLocationClient.setLocOption(options); 
	}
	
	/** ��������Ǹ���λ����ע��ʹ�õģ�ʱ��Ļص��������и���options�������ú󷵻صĽ��*/
	private class BaiduBDLocationListener implements com.baidu.location.BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation locationResult) { 
			if(locationResult==null) return;
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
				baidu_location_information.setText(locationSet.toString());
				Log.i("BaiduLocation",locationSet.toString());
			} 

			locationSet.delete(0, locationSet.length());//����ַ���
		} 
		 
	}
	
	
	@Override
	protected void onStop() { 
		super.onStop();
		mLocationClient.stop();//ֹͣ��λ
		baidu_location_toggle.setText(R.string.startlocation);//�ѿ��ذ�ť�����ó� ������λ������
	}
	 
}
