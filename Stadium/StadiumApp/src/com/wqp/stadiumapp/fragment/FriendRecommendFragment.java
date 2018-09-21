package com.wqp.stadiumapp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.wqp.stadiumapp.JoinActDetails;
import com.wqp.stadiumapp.R;
import com.wqp.stadiumapp.adapter.FriendRecommendAdapter;
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.stadiumapp.utils.ToolsNavigate;  
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener;
import com.wqp.webservice.WebGetArtInfo;  
import com.wqp.webservice.WebGetUserOnAct;
import com.wqp.webservice.entity.GetActInfoBean; 

public class FriendRecommendFragment extends Fragment implements IXListViewListener{ 
	private static String TAG="FriendRecommendFragment";
	private XListView mXListView;
	private List<Map<String,Object>> mListViewData;//为listview进行填充数据
	private FriendRecommendAdapter mFriendRecommendAdapter;
	private ImageView mImageViewState;//应战条目里面右边有个添加图标，当用户成功添加活动之后就需要更新这个图标
	
	private ProgressDialog mProgressDialog;//进度对话框
	private List<GetActInfoBean> WebDataSet=null;
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
				WebDataSet=(List<GetActInfoBean>) msg.obj; 
				WebDataSetCount=WebDataSet.size();//设置返回数据集的总长度
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//计算需要分几页显示数据 
				initListView();//为listview填充数据并绑定事件  
				mFriendRecommendAdapter.notifyDataSetChanged();//更新ListView适配器的数据  
				break;
			case 0x22://标识获取到的数据为空 
				Toast.makeText(getActivity(), "没有找到哦!", 0).show();
				break;
			case 0x33://标识上拉加载时已经没有数据了
				Toast.makeText(getActivity(), "亲,没有了哦!", 0).show(); 
				break;  
			};
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		conditionQuery();//从服务端加载应战数据  
		
		View view=inflater.inflate(R.layout.friend_recommend, container, false);
		mXListView=(XListView) view.findViewById(R.id.friend_recommend_xlistview);
		
		//为ListView绑定上拉刷新事件,并进行配置
		mXListView.setPullLoadEnable(true);
		mXListView.setPullRefreshEnable(false);//头部刷新关闭
		mXListView.toggleHeadView=true;//不使用头部刷新功能
//		conditionQuery();//从服务端加载应战数据  
		
		return view;
	}
	
	/** 为listview填充数据并绑定事件*/
	private void initListView(){
		mListViewData=new ArrayList<Map<String,Object>>();
		mFriendRecommendAdapter=new FriendRecommendAdapter(
				getActivity(),
				getListViewData(),
				R.layout.friend_recommend_xlistview_item,
				new String[]{"VenuesName","SportType","StartTime","EndTime","TakeCount","Imgstate"},
				new int[]{
					R.id.friend_recommend_xlistview_venuesname,//场馆名称
					R.id.friend_recommend_xlistview_sporttype,//运动类型
					R.id.friend_recommend_xlistview_starttime,//开始时间
					R.id.friend_recommend_xlistview_endtime,//结束时间
					R.id.friend_recommend_xlistview_takecount,//人数上限
					R.id.friend_recommend_xlistview_state}); //添加活动的状态
		mXListView.setAdapter(mFriendRecommendAdapter);
		mXListView.setXListViewListener(this);//为listview绑上上拉刷新事件
		mXListView.setOnItemClickListener(new MyOnItemClickListener()); //为listview绑定点击事件
	}
	
	/**给XListView绑定点击事件*/
	private class MyOnItemClickListener implements OnItemClickListener{ 
		@Override
		public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) { 
//			Toast.makeText(getActivity(), position+"", 0).show(); 
			
			//跳转到加入后页面详细界面
			Intent itemIntent=new Intent(getActivity(),JoinActDetails.class);
			Bundle zzextras=new Bundle();
			zzextras.putSerializable("GetActInfoBean", WebDataSet.get(position-1));
			itemIntent.putExtras(zzextras);
			getActivity().startActivity(itemIntent);//携带数据跳转到JoinActDetails应战详情页面 
		} 
	}
	
	
	/**
	 * 当用户点击了场馆查询页面的查询按钮之后会调用此方法
	 */
	public void conditionQuery() { 
			if(!ToolsNavigate.isNetworkAvailable(getActivity())){ return;}//判断当前网络是否可用
			mProgressDialog=new ProgressDialog(getActivity());
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("加载中...");
			mProgressDialog.setIndeterminate(true); 
			mProgressDialog.show();
			new Thread(){  
				public void run() {
					try { 
						//参数ActTypeID=5是规定写的，已经固定下来了,暂时不传参数就可以获取到全部数据
						 List<GetActInfoBean> results=WebGetArtInfo.getGetArtInfoData("");
						 if(results!=null && results.size()>0){
							 for (GetActInfoBean getActInfoBean : results) {
								 //根据SportID=where来进行查询，返回的就只有一条数据 
								 String str=ToolsHome.getSportName(getActInfoBean.getSporttype());//这个方法是使用的本地方法根据运动类型ID查询运动名称
								 if(str!=null){
									 getActInfoBean.setSporttypestring(str); 
								 } 
							 }
							//获取到当前登录的用户已经参加的活动信息,返回的只是当前用户所参加活动编号ID集合
							 List<Integer> ids=WebGetUserOnAct.getGetUserOnActData(UserGlobal.UserID); 
							 if(ids!=null){
								 for (Integer integers : ids) {
									Log.i("FriendRecommendFragment","从服务返回的当前用户所参加应战活动ID集为:"+integers);
								}
								 for (GetActInfoBean getActInfoBeanS : results) {
									 boolean tempResult=ids.contains(getActInfoBeanS.getActid());
									 Log.i(TAG,getActInfoBeanS.getActtitle()+"活动，用户参数状态是:"+tempResult);
									 getActInfoBeanS.setImgstate(tempResult);//判断当前活动的ID号是否是在用户已经参加活动的ID号范围之类
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
						 }else{
							 mHandler.sendEmptyMessage(0x22);//发送标识获取到的数据为空
						 }
						 
						 //获到数据集里面有个参数为SportType的数据
//						 if(results!=null){
//							 for (GetActInfoBean getActInfoBean : results) {
//								 //根据SportID=where来进行查询，返回的就只有一条数据
//								 List<GetSportBean> oo = WebGetSports.getGetSportsData("SportID="+getActInfoBean.getSporttype().intValue());
//								 if(oo!=null){
//									 getActInfoBean.setSporttypestring(oo.get(0).getSportname());
//								 } 
//							}  
//						 }
						
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
		if(WebDataSet==null || WebDataSet.size() == 0){ return mListViewData;//如果WebDataSet数据集不为空，并且长度等于0就做用户提醒操作
//			Toast.makeText(getActivity(), "没有找到哦", 0).show();
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
					map.put("VenuesName", "场馆名称:"+WebDataSet.get(i).getVenuesname());//场馆名称	
					map.put("SportType", "运动类型:"+WebDataSet.get(i).getSporttypestring());//运动类型 
					map.put("StartTime", "开始时间:"+WebDataSet.get(i).getStarttime());//开始时间
					map.put("EndTime", "结束时间:"+WebDataSet.get(i).getEndtime());//结束时间
					map.put("TakeCount", "人数上限:"+WebDataSet.get(i).getTakecount()+"人");//人数上限
					map.put("ActID", WebDataSet.get(i).getActid().intValue());//此参数是为了传到适配器里面进行使用的
					map.put("Imgstate", WebDataSet.get(i).isImgstate());//此参数是当前用户是否已经参加了该活动
					mListViewData.add(map); 
				} 
				current++;//把当前页加1 
			} 
			Log.i("FriendRecommendFragment>>>当前加载到的场馆的数据长度是:",mListViewData.size()+"");
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
				mFriendRecommendAdapter.notifyDataSetChanged();
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
	

}
 