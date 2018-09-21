package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment; 
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener; 
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener; 
import android.widget.ImageView;  
import android.widget.Toast; 
import com.wqp.stadiumapp.adapter.PrivateFormulateAdapter; 
import com.wqp.stadiumapp.utils.CommonSlidMenu; 
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener;  
import com.wqp.webservice.WebGetVenuesExpert;  
import com.wqp.webservice.entity.GetVenuesExpertBean; 

public class PrivateFormulate extends Fragment implements OnClickListener, IXListViewListener{
	private static String TAG="PrivateFormulate";
	private ImageView private_formulate_back_arrow,private_formulate_shared;
	private XListView private_formulate_listview; 
	private List<Map<String,Object>> mListViewData;
	private PrivateFormulateAdapter mPrivateFormulateAdapter;  

	private ProgressDialog mProgressDialog;//进度对话框 
	private List<GetVenuesExpertBean> WebDataSet=null;
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
				WebDataSet=(List<GetVenuesExpertBean>) msg.obj; 
				WebDataSetCount=WebDataSet.size();//设置返回数据集的总长度
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//计算需要分几页显示数据 
				mListViewData.clear();//清空数据缓存
				current=0;//重新把当前页面设置为默认值0
				getListViewData();//添加数据 
				mPrivateFormulateAdapter.notifyDataSetChanged();//更新ListView适配器的数据  
				break;
			case 0x22://标识获取到的数据为空 
				Toast.makeText(getActivity(), "没有找到哦!", 0).show();
			case 0x33://标识上拉加载时已经没有数据了
				Toast.makeText(getActivity(), "亲,没有了哦!", 0).show();	
			default:
				break;
			} 
		};
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.private_formulate, container,false); 
		initView(view);//对页面组件进行初始化
		initListView();;//对XListView填充数据操作
		
		conditionQuery();//从服务端获取数据
		return view;
	}
	
	private void initView(View view){
		private_formulate_back_arrow=(ImageView) view.findViewById(R.id.private_formulate_back_arrow);
		private_formulate_shared=(ImageView) view.findViewById(R.id.private_formulate_shared);
		
		private_formulate_listview=(XListView) view.findViewById(R.id.private_formulate_listview); 
		//为XListView绑定上拉刷新事件,并进行配置
		private_formulate_listview.setPullLoadEnable(true);
		private_formulate_listview.setPullRefreshEnable(false);
		private_formulate_listview.toggleHeadView=true;//不使用头部刷新功能 
		
		private_formulate_back_arrow.setOnClickListener(this);
		private_formulate_shared.setOnClickListener(this);   
	}

	//对XListView初始化和填充数据,并绑定事件
	private void initListView(){
		mListViewData=new ArrayList<Map<String,Object>>();
		
		mPrivateFormulateAdapter =new PrivateFormulateAdapter(getActivity(), 
				getListViewData(),
				R.layout.private_formulate_listview_item, 
				new String[]{"Nickname","Sex","ZhuanJiaType","VenuesName","Hobby"},
				new int[]{
						R.id.private_formulate_nickname,
						R.id.private_formulate_sex,
						R.id.private_formulate_zhuanjiatype,
						R.id.private_formulate_venuestype,
						R.id.private_formulate_hobby});
		//使用适配器填充数据
		private_formulate_listview.setAdapter(mPrivateFormulateAdapter); 
		private_formulate_listview.setXListViewListener(this);//设置滚动监听
		
		private_formulate_listview.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				
			} 
		});
	} 
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.private_formulate_back_arrow://返回按钮
			CommonSlidMenu.mSlidMenu.toggle();//侧边菜单出现
			break;
		case R.id.private_formulate_shared://分享图标
			Toast.makeText(getActivity(), "分享", 0).show();
			break; 
		} 
	} 

	/** 给ListView进行填充数据,默认每页只显示10条数据 */
	private List<Map<String,Object>> getListViewData(){
		if(WebDataSet==null || WebDataSet.size() == 0){//如果WebDataSet数据集不为空，并且长度等于0就做用户提醒操作
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
					map.put("Nickname", "昵称:"+WebDataSet.get(i).getNickName());//专家昵称
					map.put("Sex", "性别:"+WebDataSet.get(i).getSex());//专家性别
					map.put("ZhuanJiaType", "专家类型:"+WebDataSet.get(i).getZhuanJiaType());//专家类型
					map.put("VenuesName", "所属场馆:"+WebDataSet.get(i).getVenuesName());//所属场馆
					map.put("Hobby", "运动项目:"+WebDataSet.get(i).getHobby());//运动项目 
					mListViewData.add(map); 
				} 
				current++;//把当前页加1 
			} 
		}
		Log.i("当前的数据长度是:",mListViewData.size()+"");
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
				getListViewData();//添加数据
				mPrivateFormulateAdapter.notifyDataSetChanged();//更新ListView适配器的数据
				onLoad(); 
			} 
		}, 2000);  
	}
	
	/** 停止ListView底部刷新 */
	private void onLoad() {
		private_formulate_listview.stopRefresh();
		private_formulate_listview.stopLoadMore();
		private_formulate_listview.setRefreshTime("刚刚");
		private_formulate_listview.invalidate();
	}
	
	/**
	 * 当用户点击了私人定制页面的查询按钮之后会调用此方法
	 */
	public void conditionQuery() { 
			mProgressDialog=new ProgressDialog(getActivity());
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("加载中...");
			mProgressDialog.setIndeterminate(true); 
			mProgressDialog.show();
			new Thread(){   
				public void run() {
					try {   //获取运动专家信息(私人订制)不传参查询所有运动专家 ,传入参数：E.ExpertID =1 获取某个运动专家的信息 	
						List<GetVenuesExpertBean> results=WebGetVenuesExpert.getGetVenuesExpertData("");
						
						//获取运动专家对应的运动信息
						if(results!=null){
							for (GetVenuesExpertBean getVenuesExpertBean : results) {
								String hobby=getVenuesExpertBean.getHobby();
								if(hobby!=null){
									String[] servial=hobby.split(",");
									StringBuilder sb=new StringBuilder();
									for(int i=0;i<servial.length;i++){
										//根据 传入参数：SportID=1  获取某个运动的信息
										String str=ToolsHome.getSportName(Integer.valueOf(servial[i]));
										if(str!=null){
											sb.append(str+" ");
										} 
//										List<GetSportBean> oo=WebGetSports.getGetSportsData("SportID="+servial[i]);
//										sb.append(oo.get(0).getSportname()+" ");
									}
									getVenuesExpertBean.setHobby(sb.toString());//再把获取到详细数据设置回对象当中
								} 
							}
						} 
						
						
						if(results!=null){
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
  
}
