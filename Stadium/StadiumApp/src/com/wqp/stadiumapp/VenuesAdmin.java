package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.wqp.stadiumapp.adapter.VenuesAdminAdapter;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.entity.GetVenuesInfoBean;

/**场馆管理员登录的界面*/
public class VenuesAdmin extends Activity {
	private static final String TAG = "VenuesAdmin"; 
	private ListView venuesadminlist; 
	private VenuesAdminAdapter mVenuesAdminAdapter;
	
	private List<GetVenuesInfoBean> WebDataSet=null;
	private List<Map<String,Object>> mListViewData;//存入listview每个条目的数据
	
	//定义消息管理器
	private Handler mHandler=new Handler(){
		 @SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 0x11://成功接收到服务端的数据
				WebDataSet=(List<GetVenuesInfoBean>) msg.obj;
				showData();//进行在ListView当中显示数据
				break;
			case 0x12://获取到数据失败
				Toast.makeText(VenuesAdmin.this, "您暂无获取到订单数据", 0).show();
				break;
			} 
		 };
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venuesadmin);
		venuesadminlist=(ListView) findViewById(R.id.venuesadmin_list);
		
		//开启场馆监听用户提交信息服务
		UserGlobal.admin_intent.setAction("com.wqp.stadiumapp.AdminService");
		startService(UserGlobal.admin_intent);
		
		//开启线程从服务端获到普通用户申请到的场馆数据
		openThredOpertion();
		
	}
	
	/** 开启新线程进行从服务端获取数据操作*/
	private void openThredOpertion() {
		new Thread(){
			public void run() {
				//调用服务端获取场馆用户订单数据接口
				
				
				mHandler.sendMessage(null);	
			};
		}.start(); 
	}

	/**
	 * 显示当前场馆用户接收到的订单信息,这个数据是从服务器获取到的。
	 */
	private void showData() {
		mListViewData=new ArrayList<Map<String,Object>>();
		mVenuesAdminAdapter=new VenuesAdminAdapter(
				VenuesAdmin.this,
				getListViewData(),
				R.layout.venues_admin_listview_item,
				new String[]{"VenuesImager","VenuesName","Address","ReservePhone","RideRoute","VenuesEnvironment"},	
				new int[]{R.id.venuesadmin_listview_image,
						  R.id.venuesadmin_listview_title,
						  R.id.venuesadmin_listview_address,
						  R.id.venuesadmin_listview_telephone,
						  R.id.venuesadmin_listview_route, 
						  R.id.venuesadmin_listview_environment});
		
		venuesadminlist.setAdapter(mVenuesAdminAdapter);
		venuesadminlist.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//场馆管理用户点击了ListView当中的某一条目之后会响应此操作
				int venues_id=((Integer) mListViewData.get(position).get("VenuesID")).intValue();
				Log.i(TAG,"场馆用户选择到的场馆对应ID是："+venues_id);
			} 
		});
	}
	
	/** 给ListView进行填充数据,默认每页只显示10条数据 */
	private List<Map<String,Object>> getListViewData(){ 
		if(WebDataSet!=null)Log.i(TAG,"现在WebDataSet的长度为:"+WebDataSet.size());
		
		if(WebDataSet!=null && WebDataSet.size() == 0){//如果WebDataSet数据集不为空，并且长度等于0就做用户提醒操作
//			Toast.makeText(getActivity(), "没有找到哦", 0).show();
		}else if(WebDataSet!=null && WebDataSet.size() > 0){ 
				for (int i=0; i<WebDataSet.size(); i++) {
					Log.i(TAG,"i值等于="+i);
					Map<String,Object> map=new HashMap<String,Object>();
					Log.i(TAG,"获取到的图片路径为="+WebDataSet.get(i).getVenuesimage()); 
					map.put("VenuesImager", WebServiceUtils.IMAGE_URL+WebDataSet.get(i).getVenuesimage());//场馆图片,这里是图片的Uri路径，然后到ListView适配器当中进行异步加载获取到图片	
					map.put("VenuesName", WebDataSet.get(i).getVenuesname());//场馆名称
					map.put("Address", "地址："+WebDataSet.get(i).getAddress());//详细地址
					map.put("ReservePhone", "电话："+WebDataSet.get(i).getReservephone());//预定电话
					map.put("RideRoute", "路线："+WebDataSet.get(i).getRideroute());//乘车路线
					map.put("VenuesEnvironment", "所属环境："+WebDataSet.get(i).getVenuesenvironment());//场馆所属环境 
					/**这条数据是额外添加的，在适配器里面是不做任何使用,只做用户点之后获取场馆ID号使用*/
					map.put("VenuesID", WebDataSet.get(i).getVenuesid());
					mListViewData.add(map);
				}  
		}else{
			Log.i(TAG,"给适配器填充的数据为null");
		}
		return mListViewData; 
	}
	
}
