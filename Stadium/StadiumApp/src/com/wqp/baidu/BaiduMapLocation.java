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
	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public BMapManager mBMapManager=null; 
	private StringBuilder locationSet=new StringBuilder();//存放定位之后返回的结果集 
	
	//定位图层
	locationOverlay myLocationOverlay = null;
	//弹出泡泡图层
	private PopupOverlay   pop  = null;//弹出泡泡图层，浏览节点时使用
	private TextView  popupText = null;//泡泡view
	private View viewCache = null;
	
	//地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
	//如果不处理touch事件，则无需继承，直接使用MapView即可
	public XMapView mMapView = null;	// 地图View
	private MapController mMapController = null;
	private MKSearch mMKSearch = null;//用于信息检索服务 

	//UI相关
	OnCheckedChangeListener radioButtonListener = null;
	TextView requestLocButton ,btSerach;
	boolean isRequest = false;//是否手动触发请求定位
	boolean isFirstLoc = true;//是否首次定位
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 使用地图sdk前需先初始化BMapManager.
         * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
         * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
         */ 
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(getApplicationContext());
            /** 如果BMapManager没有初始化则初始化BMapManager  */
            mBMapManager.init(UtilKey.BAIDU_KEY,new MyGeneralListener(getApplicationContext()));
        }
        setContentView(R.layout.baidu_map_location); 
       	txtAddr=(EditText)findViewById(R.id.txtAddr);//获取到用户输入框组件
       	
      //监听搜索单击事件
       	btSerach= (TextView)findViewById(R.id.btOk);
    	btSerach.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//获取到用户输入的城市名
				mMKSearch.poiSearchInCity("", txtAddr.getText().toString().trim());  					
			}
		});
        //定位按钮
        requestLocButton = (TextView)findViewById(R.id.btget);
	    requestLocButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestLocClick();
			}
		});
	    
		//地图初始化
        mMapView = (XMapView)findViewById(R.id.bmapView);
        mMapView.setTraffic(true);//设置地图模式为交通视图(也可为卫星视图) 
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(15);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
      //信息检索初始化
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
					Toast.makeText(BaiduMapLocation.this, "抱歉，未找到结果",Toast.LENGTH_LONG).show();
					return;
				} else if (error != 0 || res == null) {
					Toast.makeText(BaiduMapLocation.this, "搜索出错啦..",Toast.LENGTH_LONG).show();
					return;
				}else{
					Toast.makeText(BaiduMapLocation.this, "搜索成功",Toast.LENGTH_LONG).show();
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
        //定位初始化
        mLocClient = new LocationClient(getApplicationContext());
        locData = new LocationData();
        mLocClient.registerLocationListener( myListener );
        LocationClientOption options=new LocationClientOption(); 
		options.setCoorType("bd09ll");//设置返回定位结果坐标系的类型，默认是 百度加密经纬度
		options.setAddrType("all");//值为 all时，表示返回地址信息，其他值都表示不返回地址信息。 
		options.setOpenGps(true);//是否打开GPS
		options.setIsNeedAddress(true);//是否反地量编码(就是把坐标值转换成实际详细地址) 
		options.setScanSpan(5000);//设置发起定位请求的间隔时间为1100ms
        mLocClient.setLocOption(options);
        mLocClient.start();
       
        //定位图层初始化
		myLocationOverlay = new locationOverlay(mMapView);
		//设置定位数据
	    myLocationOverlay.setData(locData);
	    //添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//修改定位数据后刷新图层生效
		mMapView.refresh();
		
    }
    /**
     * 手动触发一次定位请求
     */
    public void requestLocClick(){
    	isRequest = true;
        mLocClient.requestLocation();
        mLocClient.start();
        Toast.makeText(BaiduMapLocation.this, "正在定位……", Toast.LENGTH_SHORT).show();
    }
    
    /**
	 * 创建弹出泡泡图层
	 */
	public void createPaopao(){
		viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popupText =(TextView) viewCache.findViewById(R.id.textcache);
        //泡泡点击响应回调
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
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) return ; 
            
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            //如果不显示定位精度圈，将accuracy赋值为0即可
            locData.accuracy = location.getRadius();
            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
            locData.direction = location.getDirection();
            //更新定位数据
            myLocationOverlay.setData(locData);
            //更新图层数据执行刷新后生效
            mMapView.refresh();
            //是手动触发请求或首次定位时，移动到定位点
            if (isRequest || isFirstLoc){
            	//移动地图到定位点 
                mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
                isRequest = false;
                myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
            } 

            getReceiverData(location);//获取全部数据
            
            //首次定位完成
            isFirstLoc = false;
        }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
    
    /** 从返回的结果集中读取数据*/
    public void getReceiverData(BDLocation locationResult){
    	locationSet.delete(0, locationSet.length());//清空字符串
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
		
		//全部数据集
		Log.i("BaiduMapLocation",locationSet.toString()); 
        Toast.makeText(BaiduMapLocation.this, "定位成功", Toast.LENGTH_SHORT).show();
        
        //把定位到的地址显示到主界面的所在地位置 
        ToolsHome.LOCATION_CITY=locationResult.getCity();//获取到定位成功城市名称 
        ToolsHome.IS_LOCATION=true;//设置为已定位标识
        
        try {
			Thread.sleep(1000);
			mLocClient.stop();//只要成功定位一次就暂停，如果需要再次进行定位就需要手动点击定位按钮来实现定位操作
//			finish();
		} catch (InterruptedException e) { 
			e.printStackTrace();
		} 
    }
    
    //继承MyLocationOverlay重写dispatchTap实现点击处理
  	public class locationOverlay extends MyLocationOverlay{
  		public locationOverlay(MapView mapView) {
  			super(mapView);
  		}
  		@Override
  		protected boolean dispatchTap() {
  			//处理点击事件,弹出泡泡
  			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText(mLocClient.getLastKnownLocation().getAddrStr());
			pop.showPopup(getBitmapFromView(popupText),new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6)),8);
			return true;
  		}
  		
  	}
  	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {
		private Context myContext;
		public MyGeneralListener(Context myContext){
			this.myContext=myContext;
		}
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(myContext,"您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
//				Toast.makeText(myContext,"输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
//				Toast.makeText(myContext,"请在 DemoApplication.java文件输入正确的授权Key！",Toast.LENGTH_LONG).show();
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
    	//退出时销毁定位
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
