package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message; 
import android.util.Log; 
import android.view.View; 
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;
 
import com.wqp.stadiumapp.R; 
import com.wqp.stadiumapp.adapter.MyOrderAdapter;
import com.wqp.stadiumapp.utils.ToolsNavigate; 
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener; 
import com.wqp.webservice.WebGetUserMake;
import com.wqp.webservice.WebGetVenues; 
import com.wqp.webservice.entity.GetUserMakeBean;
import com.wqp.webservice.entity.GetVenuesInfoBean;

/** 我的预约 页面*/
public class MyOrder extends Activity implements IXListViewListener, OnClickListener{ 
	private static String TAG="MyOrder";
	private ImageView myorder_back_arrow;
	private XListView mXListView;
	private List<Map<String,Object>> mListViewData;//为listview进行填充数据
	private MyOrderAdapter mMyOrderAdapter;
	@SuppressWarnings("unused")
	private ImageView mImageViewState;//应战条目里面右边有个添加图标，当用户成功添加活动之后就需要更新这个图标
	
	private ProgressDialog mProgressDialog;//进度对话框
	private List<GetUserMakeBean> WebDataSet=null;
	private int WebDataSetCount=0;//存储查询到的数据集总长度
	private int mTotalPage=0;//根据数据总长度需要几页显示数据
	private int TEN=10;//默认每页显示的数据长度
	private int current=0;//当前页,默认为第一页  mTotalPage - currentIndex  
	
	/** 定义消息管理器*/
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://从线程中查询的数据集返回此处 
				WebDataSet=(List<GetUserMakeBean>) msg.obj; 
				WebDataSetCount=WebDataSet.size();//设置返回数据集的总长度
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//计算需要分几页显示数据 
				initListView();//为listview填充数据并绑定事件  
				mMyOrderAdapter.notifyDataSetChanged();//更新ListView适配器的数据  
				break;
			case 0x22://标识获取到的数据为空 
				Toast.makeText(MyOrder.this, "没有找到哦!", 0).show();
				break;
			case 0x33://标识上拉加载时已经没有数据了
				Toast.makeText(MyOrder.this, "亲,没有了哦!", 0).show(); 
				break;  
			};
		};
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order);   
		conditionQuery();//从服务端加载应战数据   
		
		myorder_back_arrow=(ImageView) findViewById(R.id.myorder_back_arrow);
		mXListView=(XListView)findViewById(R.id.myorder_xlistview);
		
		//为ListView绑定上拉刷新事件,并进行配置
		mXListView.setPullLoadEnable(true);
		mXListView.setPullRefreshEnable(false);//头部刷新关闭
		mXListView.toggleHeadView=true;//不使用头部刷新功能 
		
		myorder_back_arrow.setOnClickListener(this);
	}
	
	/** 为listview填充数据并绑定事件*/
	private void initListView(){
		mListViewData=new ArrayList<Map<String,Object>>();
		mMyOrderAdapter=new MyOrderAdapter(
				MyOrder.this,
				getListViewData(),
				R.layout.my_order_xlistview_item,
				new String[]{"VenuesName","SportType","OrderTime","State"},
				new int[]{
					R.id.myorder_xlistview_venuesname_type,//场馆名称
					R.id.myorder_xlistview_sporttype_type,//运动类型
					R.id.myorder_xlistview_starttime_type,//预约时间 
					R.id.myorder_xlistview_state_type}); //预约的状态
		mXListView.setAdapter(mMyOrderAdapter);
		mXListView.setXListViewListener(this);//为listview绑上上拉刷新事件
		mXListView.setOnItemClickListener(new MyOnItemClickListener()); //为listview绑定点击事件
	}
	
	/**给XListView绑定点击事件*/
	private class MyOnItemClickListener implements OnItemClickListener{ 
		@Override
		public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) { 
//			Toast.makeText(FriendRecommendType.this, position+"", 0).show(); 
			
			//跳转到预约券详细界面
			Intent itemIntent=new Intent(MyOrder.this,OrderTicket.class);
			Bundle zzextras=new Bundle();
			zzextras.putSerializable("GetUserMakeBean", WebDataSet.get(position-1));
			itemIntent.putExtras(zzextras);
			MyOrder.this.startActivity(itemIntent);//携带数据跳转到OrderTicket预约券详情页面 
		} 
	}
	
	
	/**
	 * 当用户点击了场馆查询页面的查询按钮之后会调用此方法
	 */
	public void conditionQuery() { 
			if(!ToolsNavigate.isNetworkAvailable(MyOrder.this)){ return;}//判断当前网络是否可用
			mProgressDialog=new ProgressDialog(MyOrder.this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("加载中...");
			mProgressDialog.setIndeterminate(true); 
			mProgressDialog.show();
			new Thread(){  
				public void run() {
					try { 
						//参数是填写正常登录用户的ID
						JSONObject object=new JSONObject();
						object.put("UserID", UserGlobal.UserID);
						 List<GetUserMakeBean> results=WebGetUserMake.getGetUserMakeData(object.toString());
						 
						 //根据场馆ID获取到场馆名称
						 if(results!=null){ 
							 for (GetUserMakeBean getUserMakeBean : results) {
								 //根据VenuesID来查询场馆名称,参数示例:VI.VenuesID=1
								 List<GetVenuesInfoBean> _list=WebGetVenues.getGetVenuesData("VI.VenuesID="+getUserMakeBean.getVenuesID());
								 if(_list==null) continue; 
								 getUserMakeBean.setVenuesName( _list.get(0).getVenuesname());
							} 
						 }  
						 
						if(results!=null && results.size() > 0 ){
							Message msg=new Message();
							msg.what=0x11;//标识正常获取到数据了
							msg.obj=results;
							mHandler.sendMessage(msg); 
							Log.i(TAG,"Web端线程加载数据结束,我已经发送数据到Handler了");
						}else{
							mHandler.sendEmptyMessage(0x22);//发送标识获取到的数据为空
						}
					} catch (Exception e) {  
						e.printStackTrace();
					}finally{
						mProgressDialog.dismiss();
					}
				};
			}.start(); 
	}

	/** 给ListView进行填充数据,默认每页只显示10条数据 */
	private List<Map<String,Object>> getListViewData(){
		if(WebDataSet==null || WebDataSet.size() == 0){//如果WebDataSet数据集不为空，并且长度等于0就做用户提醒操作
//			Toast.makeText(FriendRecommendType.this, "没有找到哦", 0).show();
		}else if(WebDataSet!=null && WebDataSet.size() > 0){//如果WebDataSet数据集不为空，并且长度大于0就做更新数据操作		
			/** 如果当前页数 小于 总页数,默认当前页从0开始增长,mTotalPage为数据总共分多少页*/
			if(current < mTotalPage){ 
				//参数1：i的值等于当前页乘以每页限制显示的条目数量,默认是10条
				//参数2： var的值等于
				int var=0;
				//如果数据长度在默认10条以内
				if(WebDataSetCount < 10){ var=WebDataSetCount; }
				else{ var = ((WebDataSetCount-(current*TEN))/TEN)>0 ? (current+1)*TEN : ((current+1)*TEN + WebDataSetCount%TEN)-TEN;}
				for (int i=current * TEN;i < var; i++) {
					Map<String,Object> map=new HashMap<String,Object>(); 
					map.put("VenuesName", "场馆名称:"+WebDataSet.get(i).getVenuesName());//场馆名称	
					map.put("SportType", "运动类型:"+WebDataSet.get(i).getProjectName());//ToolsHome.getSportName(WebDataSet.get(i).getProductID()));//运动类型 (这里填写的是 运动项目名称)
					map.put("OrderTime", "预约时间:"+WebDataSet.get(i).getMakeTime());//预约时间  
					map.put("State", WebDataSet.get(i).getState());//预约状态
					mListViewData.add(map); 
				} 
				current++;//把当前页加1 
			} 
			Log.i("当前的数据长度是:",mListViewData.size()+"");
		} 
		return mListViewData; 
	}

	/** 头部下拉刷新,暂未使用*/
	@Override
	public void onRefresh() { 
		
	}

	
	/**这个是ListView底部加载更多使用,这里使用到了Handler进行加载数据*/
	@Override
	public void onLoadMore() { 
		mHandler.postDelayed(new Runnable(){  
			@Override
			public void run() { 
				if(current==mTotalPage || WebDataSetCount==0){
					mHandler.sendEmptyMessage(0x33);//发送已经没有新数据的标识
					Log.i(TAG,"在ListView当中我也向Handler发送消息过去了");
					onLoad();
					return;
				}
				getListViewData();
				mMyOrderAdapter.notifyDataSetChanged();
				onLoad();
			} 
		}, 2000);
	}
	
	/** 停止ListView底部刷新 */
	private void onLoad() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
		mXListView.setRefreshTime("刚刚");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myorder_back_arrow://点击了返回按钮
			finish();
			break; 
		} 
	} 
	

}
 