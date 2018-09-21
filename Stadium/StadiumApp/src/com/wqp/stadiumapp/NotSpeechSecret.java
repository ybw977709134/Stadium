package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
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

import com.wqp.stadiumapp.adapter.NotSpeechSecretAndBrokeTheSpaceAdapter;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener;
import com.wqp.webservice.WebGetGuide; 
import com.wqp.webservice.entity.GetGuideBean; 

public class NotSpeechSecret extends Activity implements IXListViewListener, OnClickListener{
	private static String TAG="NotSpeechSecret";
	private ImageView notspeechscret_back_arrow;
	private XListView notspeechscret_listview;
	private List<Map<String,Object>> mListViewData;
	private NotSpeechSecretAndBrokeTheSpaceAdapter mNotSpeechSecretAdapter;
	
	private ProgressDialog mProgressDialog;//进度对话框
	private List<GetGuideBean> WebDataSet=null;
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
				WebDataSet=(List<GetGuideBean>) msg.obj; 
				WebDataSetCount=WebDataSet.size();//设置返回数据集的总长度
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//计算需要分几页显示数据 
//				mListViewData.clear();//清空数据缓存
//				current=0;//重新把当前页面设置为默认值0
				getListViewData();//添加数据 
				mNotSpeechSecretAdapter.notifyDataSetChanged();//更新ListView适配器的数据  
				break;
			case 0x22://标识获取到的数据为空 
				Toast.makeText(NotSpeechSecret.this, "没有找到哦!", 0).show();
			case 0x33://标识上拉加载时已经没有数据了
				Toast.makeText(NotSpeechSecret.this, "亲,没有了哦!", 0).show();	
			default:
				break;
			} 
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.not_speech_scret);
		notspeechscret_back_arrow=(ImageView) findViewById(R.id.notspeechscret_back_arrow);
		notspeechscret_listview=(XListView) findViewById(R.id.notspeechscret_listview);
		
		//为XListView绑定上拉刷新事件,并进行配置
		notspeechscret_listview.setPullLoadEnable(true);
		notspeechscret_listview.setPullRefreshEnable(false);
		notspeechscret_listview.toggleHeadView=true;//不使用头部刷新功能
		
		//为返回按钮绑定事件
		notspeechscret_back_arrow.setOnClickListener(this); 
		conditionQuery();
		initXListView();
		
	}
	
	public void initXListView(){ 
		mListViewData=new ArrayList<Map<String,Object>>();
		
		//给适配器填充数据
		mNotSpeechSecretAdapter=new NotSpeechSecretAndBrokeTheSpaceAdapter(
				NotSpeechSecret.this, 
				getListViewData(), 
				R.layout.notspeechsecret_listview_item, 
				new String[]{"Title","SGContent"}, 
				new int[]{R.id.notspeechsecret_title,R.id.notspeechsecret_content});
		//设置适配器的数据
		notspeechscret_listview.setAdapter(mNotSpeechSecretAdapter);
		//为XListView绑定事件
		notspeechscret_listview.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
//				Toast.makeText(NotSpeechSecret.this, position+"", 0).show(); 
				Intent intent=new Intent(NotSpeechSecret.this,NotSpeechScrectDetails.class);
				Bundle extras=new Bundle();
				extras.putSerializable("GetGuideBean", WebDataSet.get(position-1));
				intent.putExtras(extras);
				NotSpeechSecret.this.startActivity(intent);//跳转到不能说的秘密的详情界面
			} 
		});
		
	}
	
	/** 给ListView进行填充数据,默认每页只显示10条数据 */
	private List<Map<String,Object>> getListViewData(){
		if(WebDataSet==null || WebDataSet.size() == 0){//如果WebDataSet数据集不为空，并且长度等于0就做用户提醒操作
//			Toast.makeText(NotSpeechSecret.this, "没有找到哦", 0).show();
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
					map.put("Title", "标题:"+WebDataSet.get(i).getTitle());//标题	
					map.put("SGContent", "内容:"+WebDataSet.get(i).getSgcontent());//内容
					mListViewData.add(map); 
				} 
				current++;//把当前页加1 
			}  
			Log.i("当前的数据长度是:",mListViewData.size()+"");
		}  
		return mListViewData; 
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notspeechscret_back_arrow://点击了返回按钮
			finish();
			break; 
		} 
	}
	
	/**
	 * 当用户点击了场馆查询页面的查询按钮之后会调用此方法
	 */
	public void conditionQuery() { 
			mProgressDialog=new ProgressDialog(NotSpeechSecret.this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("加载中...");
			mProgressDialog.setIndeterminate(true); 
			mProgressDialog.show();
			new Thread(){   
				public void run() {
					try {   //不能说的秘密，参数是：SGType='文章'
						List<GetGuideBean> results=WebGetGuide.getGetGuideData("SGType='文章'");
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
				mNotSpeechSecretAdapter.notifyDataSetChanged();//更新ListView适配器的数据
				onLoad(); 
			} 
		}, 2000);  
	}
	
	/** 停止ListView底部刷新 */
	private void onLoad() {
		notspeechscret_listview.stopRefresh();
		notspeechscret_listview.stopLoadMore();
		notspeechscret_listview.setRefreshTime("刚刚");
		notspeechscret_listview.invalidate();
	}

	
}
