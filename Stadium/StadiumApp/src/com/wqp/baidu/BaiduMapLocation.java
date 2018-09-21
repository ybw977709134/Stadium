package com.wqp.baidu; 
  
import android.app.Activity;
import android.content.Context; 
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.wqp.stadiumapp.R;
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.stadiumapp.utils.UtilKey;
import com.wqp.view.XMapView;

public class BaiduMapLocation extends Activity{
    private  EditText txtAddr;
	// ��λ���
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public BMapManager mBMapManager=null; 
	private StringBuilder locationSet=new StringBuilder();//��Ŷ�λ֮�󷵻صĽ���� 
	
	//��λͼ��
	locationOverlay myLocationOverlay = null;
	//��������ͼ��
	private PopupOverlay   pop  = null;//��������ͼ�㣬����ڵ�ʱʹ��
	private TextView  popupText = null;//����view
	private View viewCache = null;
	
	//��ͼ��أ�ʹ�ü̳�MapView��MyLocationMapViewĿ������дtouch�¼�ʵ�����ݴ���
	//���������touch�¼���������̳У�ֱ��ʹ��MapView����
	public XMapView mMapView = null;	// ��ͼView
	private MapController mMapController = null;
	private MKSearch mMKSearch = null;//������Ϣ�������� 

	//UI���
	OnCheckedChangeListener radioButtonListener = null;
	TextView requestLocButton ,btSerach;
	boolean isRequest = false;//�Ƿ��ֶ���������λ
	boolean isFirstLoc = true;//�Ƿ��״ζ�λ
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * ʹ�õ�ͼsdkǰ���ȳ�ʼ��BMapManager.
         * BMapManager��ȫ�ֵģ���Ϊ���MapView���ã�����Ҫ��ͼģ�鴴��ǰ������
         * ���ڵ�ͼ��ͼģ�����ٺ����٣�ֻҪ���е�ͼģ����ʹ�ã�BMapManager�Ͳ�Ӧ������
         */ 
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(getApplicationContext());
            /** ���BMapManagerû�г�ʼ�����ʼ��BMapManager  */
            mBMapManager.init(UtilKey.BAIDU_KEY,new MyGeneralListener(getApplicationContext()));
        }
        setContentView(R.layout.baidu_map_location); 
       	txtAddr=(EditText)findViewById(R.id.txtAddr);//��ȡ���û���������
       	
      //�������������¼�
       	btSerach= (TextView)findViewById(R.id.btOk);
    	btSerach.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//��ȡ���û�����ĳ�����
				mMKSearch.poiSearchInCity("", txtAddr.getText().toString().trim());  					
			}
		});
        //��λ��ť
        requestLocButton = (TextView)findViewById(R.id.btget);
	    requestLocButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestLocClick();
			}
		});
	    
		//��ͼ��ʼ��
        mMapView = (XMapView)findViewById(R.id.bmapView);
        mMapView.setTraffic(true);//���õ�ͼģʽΪ��ͨ��ͼ(Ҳ��Ϊ������ͼ) 
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(15);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
      //��Ϣ������ʼ��
    	mMKSearch = new MKSearch(); 
		mMKSearch.init(mBMapManager, new MKSearchListener() {
			@Override
			public void onGetAddrResult(MKAddrInfo arg0, int arg1) { }

			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) { }

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) { }

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) { }

			@Override
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				if (error == MKEvent.ERROR_RESULT_NOT_FOUND) {
					Toast.makeText(BaiduMapLocation.this, "��Ǹ��δ�ҵ����",Toast.LENGTH_LONG).show();
					return;
				} else if (error != 0 || res == null) {
					Toast.makeText(BaiduMapLocation.this, "����������..",Toast.LENGTH_LONG).show();
					return;
				}else{
					Toast.makeText(BaiduMapLocation.this, "�����ɹ�",Toast.LENGTH_LONG).show();
				}
				PoiOverlay poiOverlay = new PoiOverlay(BaiduMapLocation.this, mMapView);
				poiOverlay.setData(res.getAllPoi());
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(poiOverlay);
				mMapView.refresh();
				for (MKPoiInfo info : res.getAllPoi()) {
					if (info.pt != null) {
						mMapView.getController().animateTo(info.pt);
						break;
					}
				}
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) { } 
			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) { } 
			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) { } 
			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) { }
		});
		createPaopao();
        //��λ��ʼ��
        mLocClient = new LocationClient(getApplicationContext());
        locData = new LocationData();
        mLocClient.registerLocationListener( myListener );
        LocationClientOption options=new LocationClientOption(); 
		options.setCoorType("bd09ll");//���÷��ض�λ�������ϵ�����ͣ�Ĭ���� �ٶȼ��ܾ�γ��
		options.setAddrType("all");//ֵΪ allʱ����ʾ���ص�ַ��Ϣ������ֵ����ʾ�����ص�ַ��Ϣ�� 
		options.setOpenGps(true);//�Ƿ��GPS
		options.setIsNeedAddress(true);//�Ƿ񷴵�������(���ǰ�����ֵת����ʵ����ϸ��ַ) 
		options.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ1100ms
        mLocClient.setLocOption(options);
        mLocClient.start();
       
        //��λͼ���ʼ��
		myLocationOverlay = new locationOverlay(mMapView);
		//���ö�λ����
	    myLocationOverlay.setData(locData);
	    //��Ӷ�λͼ��
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//�޸Ķ�λ���ݺ�ˢ��ͼ����Ч
		mMapView.refresh();
		
    }
    /**
     * �ֶ�����һ�ζ�λ����
     */
    public void requestLocClick(){
    	isRequest = true;
        mLocClient.requestLocation();
        mLocClient.start();
        Toast.makeText(BaiduMapLocation.this, "���ڶ�λ����", Toast.LENGTH_SHORT).show();
    }
    
    /**
	 * ������������ͼ��
	 */
	public void createPaopao(){
		viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popupText =(TextView) viewCache.findViewById(R.id.textcache);
        //���ݵ����Ӧ�ص�
        PopupClickListener popListener = new PopupClickListener(){
			@Override
			public void onClickedPopup(int index) { 
				//
			}
        };
        pop = new PopupOverlay(mMapView,popListener);
        XMapView.pop = pop;
	}
	/**
     * ��λSDK��������
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) return ; 
            
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            //�������ʾ��λ����Ȧ����accuracy��ֵΪ0����
            locData.accuracy = location.getRadius();
            // �˴��������� locData�ķ�����Ϣ, �����λ SDK δ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
            locData.direction = location.getDirection();
            //���¶�λ����
            myLocationOverlay.setData(locData);
            //����ͼ������ִ��ˢ�º���Ч
            mMapView.refresh();
            //���ֶ�����������״ζ�λʱ���ƶ�����λ��
            if (isRequest || isFirstLoc){
            	//�ƶ���ͼ����λ�� 
                mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
                isRequest = false;
                myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
            } 

            getReceiverData(location);//��ȡȫ������
            
            //�״ζ�λ���
            isFirstLoc = false;
        }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
    
    /** �ӷ��صĽ�����ж�ȡ����*/
    public void getReceiverData(BDLocation locationResult){
    	locationSet.delete(0, locationSet.length());//����ַ���
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
		
		//ȫ�����ݼ�
		Log.i("BaiduMapLocation",locationSet.toString()); 
        Toast.makeText(BaiduMapLocation.this, "��λ�ɹ�", Toast.LENGTH_SHORT).show();
        
        //�Ѷ�λ���ĵ�ַ��ʾ������������ڵ�λ�� 
        ToolsHome.LOCATION_CITY=locationResult.getCity();//��ȡ����λ�ɹ��������� 
        ToolsHome.IS_LOCATION=true;//����Ϊ�Ѷ�λ��ʶ
        
        try {
			Thread.sleep(1000);
			mLocClient.stop();//ֻҪ�ɹ���λһ�ξ���ͣ�������Ҫ�ٴν��ж�λ����Ҫ�ֶ������λ��ť��ʵ�ֶ�λ����
//			finish();
		} catch (InterruptedException e) { 
			e.printStackTrace();
		} 
    }
    
    //�̳�MyLocationOverlay��дdispatchTapʵ�ֵ������
  	public class locationOverlay extends MyLocationOverlay{
  		public locationOverlay(MapView mapView) {
  			super(mapView);
  		}
  		@Override
  		protected boolean dispatchTap() {
  			//�������¼�,��������
  			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText(mLocClient.getLastKnownLocation().getAddrStr());
			pop.showPopup(getBitmapFromView(popupText),new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6)),8);
			return true;
  		}
  		
  	}
  	
	// �����¼���������������ͨ�������������Ȩ��֤�����
	static class MyGeneralListener implements MKGeneralListener {
		private Context myContext;
		public MyGeneralListener(Context myContext){
			this.myContext=myContext;
		}
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(myContext,"���������������", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
//				Toast.makeText(myContext,"������ȷ�ļ���������", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// ��ȨKey����
//				Toast.makeText(myContext,"���� DemoApplication.java�ļ�������ȷ����ȨKey��",Toast.LENGTH_LONG).show();
			}
		}

	}
	
    public Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}

    @Override
    protected void onPause() {
        mMapView.onPause();
        if(mBMapManager!=null){  
        	mBMapManager.stop();  
     } 
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        mMapView.onResume();
        if(mBMapManager!=null){  
        	mBMapManager.start();  
    }
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	//�˳�ʱ���ٶ�λ
        if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
        if(mBMapManager!=null){  
        	mBMapManager.destroy();  
        	mBMapManager=null;  
    }  
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    } 
	

}
