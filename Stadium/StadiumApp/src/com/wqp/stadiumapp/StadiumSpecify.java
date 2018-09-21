package com.wqp.stadiumapp;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wqp.stadiumapp.adapter.StadiumSpecifyAdapter;
import com.wqp.stadiumapp.utils.ToolsNavigate;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener;
import com.wqp.webservice.WebGetComment;
import com.wqp.webservice.entity.GetCommentBean;
import com.wqp.webservice.entity.GetVenuesInfoBean;

public class StadiumSpecify extends Activity implements IXListViewListener, OnClickListener {  
	private static String TAG="StadiumSpecify";
	private Button order;//页面图片下面的预约按钮
	private ImageView stadium_news_back_arrow;//头部的返回按钮图标
	private XListView stadium_specify_listview; 
	private View mHeadView;
	private LayoutInflater inflater;
	private List<Map<String,Object>> mListViewData;
	private StadiumSpecifyAdapter mStadiumSpecifyAdapter; 
	private Intent mIntent;//接收从其它页面传递过来的意图
	private GetVenuesInfoBean mGetVenuesInfoBean;//接收从其它页面传递过来的数据对象
	//场馆详情页面的所有组件
	private ImageView specify_VenuesImager;//显示场馆图片
	private TextView specify_VenuesName,specify_VenuesArea,specify_Address,specify_ReservePhone,
	specify_VenuesEmail,specify_RideRoute,specify_zhandiArea,specify_WaterTemperature,
	specify_jianzhuArea,specify_WaterDepth,specify_changdiArea,specify_Headroom,specify_GroundMaterial,
	specify_InternalFacilities,specify_Investment,specify_VenuesType,specify_VenuesEnvironment,
	specify_ResponsiblePeople,specify_Contact,specify_ContactPhone,specify_DirectorDept,specify_Briefing,
	specify_BuiltTime,specify_UseTime,specify_SeatNumber,specify_AvgNumber,specify_WhetherRecomm,specify_Map;
	

	private List<GetCommentBean> WebDataSet=null;
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
				WebDataSet= (List<GetCommentBean>) msg.obj;
				WebDataSetCount=WebDataSet.size();//设置返回数据集的总长度
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//计算需要分几页显示数据 
				getListViewData();//添加数据
				mStadiumSpecifyAdapter.notifyDataSetChanged();//更新ListView适配器的数据
				break;
			case 0x22://标识获取到的数据为空 
//				Toast.makeText(StadiumSpecify.this, "没有找到哦!", 0).show();
			case 0x33://标识上拉加载时已经没有数据了
				Toast.makeText(StadiumSpecify.this, "亲,没有了哦!", 0).show();	
			default:
				break;
			} 
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stadium_specify);
		//接收从其实页面传递过来的Intent,里面携带的有场馆详细数据
		mIntent=getIntent();
		mGetVenuesInfoBean=(GetVenuesInfoBean) mIntent.getSerializableExtra("VenuesInfo");
		
		//开启线程加载用户评论信息
		newThreadLoadComment();
		
		stadium_specify_listview=(XListView) findViewById(R.id.stadium_specify_listview);
		stadium_news_back_arrow=(ImageView) findViewById(R.id.stadium_news_back_arrow);
		inflater=LayoutInflater.from(this);
		initHeaderView();//为ListView添加头部视图
		
		//为页面头部的返回按钮图标绑定点击事件
		stadium_news_back_arrow.setOnClickListener(this); 
		
	}
	
	/** 为ListView添加头部视图，并且对listview进行填充数据 */
	private void initHeaderView(){
		//获得场馆详情页面头部所有组件的实例
		mHeadView = inflater.inflate(R.layout.stadium_specify_header,null); 
		order=(Button) mHeadView.findViewById(R.id.order);
		order.setOnClickListener(this);//为预约按钮绑定点击事件
		initSpecifyView(mHeadView);//对场馆详情页面上面的所有组件进行实例化操作，并赋初值
		stadium_specify_listview.addHeaderView(mHeadView); 
		
		//为ListView绑定上拉刷新事件,并进行配置
		stadium_specify_listview.setPullLoadEnable(true);
		stadium_specify_listview.setPullRefreshEnable(false);//头部刷新关闭
		stadium_specify_listview.toggleHeadView=true;//不使用头部刷新功能 
		
		//给ListView填充数据
		mListViewData=new ArrayList<Map<String,Object>>(); 
		
		mStadiumSpecifyAdapter=new StadiumSpecifyAdapter(
				this, 
				getListViewData(), 
				R.layout.stadium_specify_listview_item, 
				new String[]{"Picture","UserName","Createtime","Con"}, 
				new int[]{
						R.id.stadium_specify_listview_image,
						R.id.stadium_specify_listview_name,
						R.id.stadium_specify_listview_spacetime,
						R.id.stadium_specify_listview_content});
		
		//为listview填充适配器
		stadium_specify_listview.setAdapter(mStadiumSpecifyAdapter); 
		//为ListView注册监听事件
		stadium_specify_listview.setXListViewListener(this);
		
	} 
	
	/** 给ListView进行填充数据,默认每页只显示10条数据 */
	private List<Map<String,Object>> getListViewData(){
		if(WebDataSet!=null && WebDataSet.size() == 0){//如果WebDataSet数据集不为空，并且长度等于0就做用户提醒操作
//			Toast.makeText(StadiumSpecify.this, "没有找到哦", 0).show();
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
					map.put("Picture", WebDataSet.get(i).getUserid());//用户的头像,这里是图片的Uri路径，然后到ListView适配器当中进行异步加载获取到图片	
					map.put("UserName", UserGlobal.NickName);//点评人(这个是根据用户的ID)查询到的数据，所以这里就等于当前正常登录的用户名
					map.put("Createtime", "点评时间："+WebDataSet.get(i).getCreatetime());//点评时间 
					map.put("Con", "点评内容："+WebDataSet.get(i).getCon());//点评内容
					mListViewData.add(map);
				} 
				current++;//把当前页加1 
			} 
		}
		return mListViewData; 
	}
	
	private void newThreadLoadComment(){
		new Thread(){ 
			public void run() { 
					//根据场馆的ID进行查询
					String condition;
					if(mGetVenuesInfoBean!=null){
						condition="KeyID="+mGetVenuesInfoBean.getVenuesid().intValue()+" and KeyType='场馆'";
					}else{
						condition="";
					} 
					Log.i("用户评价查询的条件是:",condition);
					List<GetCommentBean> results=WebGetComment.getGetCommentData(condition);
					if(results!=null){
						Message msg=new Message();
						msg.what=0x11;//标识正常获取到数据了
						msg.obj=results;
						mHandler.sendMessage(msg); 
						Log.i(TAG,"Web端线程加载数据结束,我已经发送数据到Handler了");
					}else{
						mHandler.sendEmptyMessage(0x22);//发送标识获取到的数据为空
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
				if(!ToolsNavigate.isNetworkAvailable(getApplicationContext())){
					Toast.makeText(StadiumSpecify.this, "亲,网络没打开哦", 1).show();return;
				}
				if(current==mTotalPage || WebDataSetCount==0){
					mHandler.sendEmptyMessage(0x33);//发送已经没有新数据的标识
					Log.i(TAG,"在ListView当中我也向Handler发送消息过去了");
					onLoad();
					return;
				}
				getListViewData();
				mStadiumSpecifyAdapter.notifyDataSetChanged();
				onLoad();
			} 
		}, 2000);
	}
	
	/** 停止ListView底部刷新 */
	private void onLoad() {
		stadium_specify_listview.stopRefresh();
		stadium_specify_listview.stopLoadMore();
		stadium_specify_listview.setRefreshTime("刚刚");
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.stadium_news_back_arrow://你点击了页面头部的返回按钮图标
			finish();
			break;
		case R.id.order://点击了预约按钮
			if(mGetVenuesInfoBean!=null){
				Intent order_intent=new Intent(StadiumSpecify.this,StadiumOrder.class);
				order_intent.putExtra("VenuesID", mGetVenuesInfoBean.getVenuesid());//写入场馆id
				order_intent.putExtra("VenuesImage", mGetVenuesInfoBean.getVenuesimage());//写入场馆图片路径
				order_intent.putExtra("VenuesName", mGetVenuesInfoBean.getVenuesname());//写入场馆名称
				startActivity(order_intent);
			}
			break;
		}
	}
	
	/**对场馆详情页面上面的所有组件进行实例化操作*/
	private void initSpecifyView(View vv){
		specify_VenuesImager=(ImageView) vv.findViewById(R.id.specify_VenuesImager);
		specify_VenuesName=(TextView) vv.findViewById(R.id.specify_VenuesName);
		specify_VenuesArea=(TextView) vv.findViewById(R.id.specify_VenuesArea);
		specify_Address=(TextView) vv.findViewById(R.id.specify_Address);
		specify_ReservePhone=(TextView) vv.findViewById(R.id.specify_ReservePhone);
		specify_VenuesEmail=(TextView) vv.findViewById(R.id.specify_VenuesEmail);
		specify_RideRoute=(TextView) vv.findViewById(R.id.specify_RideRoute);
		specify_zhandiArea=(TextView) vv.findViewById(R.id.specify_zhandiArea);
		specify_WaterTemperature=(TextView) vv.findViewById(R.id.specify_WaterTemperature);
		specify_jianzhuArea=(TextView) vv.findViewById(R.id.specify_jianzhuArea);
		specify_WaterDepth=(TextView) vv.findViewById(R.id.specify_WaterDepth);
		specify_changdiArea=(TextView) vv.findViewById(R.id.specify_changdiArea);
		specify_Headroom=(TextView) vv.findViewById(R.id.specify_Headroom);
		specify_GroundMaterial=(TextView) vv.findViewById(R.id.specify_GroundMaterial);
		specify_InternalFacilities=(TextView) vv.findViewById(R.id.specify_InternalFacilities);
		specify_Investment=(TextView) vv.findViewById(R.id.specify_Investment);
		specify_VenuesType=(TextView) vv.findViewById(R.id.specify_VenuesType);
		specify_VenuesEnvironment=(TextView) vv.findViewById(R.id.specify_VenuesEnvironment);
		specify_ResponsiblePeople=(TextView) vv.findViewById(R.id.specify_ResponsiblePeople);
		specify_Contact=(TextView) vv.findViewById(R.id.specify_Contact);
		specify_ContactPhone=(TextView) vv.findViewById(R.id.specify_ContactPhone);
		specify_DirectorDept=(TextView) vv.findViewById(R.id.specify_DirectorDept);
		specify_Briefing=(TextView) vv.findViewById(R.id.specify_Briefing);
		specify_BuiltTime=(TextView) vv.findViewById(R.id.specify_BuiltTime);
		specify_UseTime=(TextView) vv.findViewById(R.id.specify_UseTime);
		specify_SeatNumber=(TextView) vv.findViewById(R.id.specify_SeatNumber);
		specify_AvgNumber=(TextView) vv.findViewById(R.id.specify_AvgNumber);
		specify_WhetherRecomm=(TextView) vv.findViewById(R.id.specify_WhetherRecomm);
		specify_Map=(TextView) vv.findViewById(R.id.specify_Map);
		
		if(mGetVenuesInfoBean!=null){
			//背景图片暂未设置 
			ImageLoader.getInstance().displayImage(WebServiceUtils.IMAGE_URL+mGetVenuesInfoBean.getVenuesimage(),specify_VenuesImager, 
					AppApplication.options, new SimpleImageLoadingListener(),new ImageLoadingProgressListener(){

						@Override
						public void onProgressUpdate(String arg0, View view, int current, int total) {
							Log.i(TAG,"场馆详情图片异步加载的数据:total="+total+",current="+current);
							
						} 
					});
			specify_VenuesName.setText(mGetVenuesInfoBean.getVenuesname());//场馆名称
			specify_VenuesArea.setText("场馆所在区域:"+mGetVenuesInfoBean.getVenuesarea());//场馆所在区域
			specify_Address.setText("详细地址:"+mGetVenuesInfoBean.getAddress());//详细地址
			specify_ReservePhone.setText("预定电话:"+mGetVenuesInfoBean.getReservephone());//预定电话
			specify_VenuesEmail.setText("场馆邮箱:"+mGetVenuesInfoBean.getVenuesemail());//场馆邮箱
			specify_RideRoute.setText("乘车路线:"+mGetVenuesInfoBean.getRideroute());//乘车路线
			specify_zhandiArea.setText("占地面积:"+mGetVenuesInfoBean.getZhangdiarea().toString()+"㎡");//占地面积
			specify_WaterTemperature.setText("水温:"+mGetVenuesInfoBean.getWatertemperature().toString()+"℃");//水温
			specify_jianzhuArea.setText("建筑面积:"+mGetVenuesInfoBean.getJianzhuarea().toString()+"㎡");//建筑面积
			specify_WaterDepth.setText("水深:"+mGetVenuesInfoBean.getWaterdepth()+"m");//水深
			specify_changdiArea.setText("场地面积:"+mGetVenuesInfoBean.getChangdiarea().toString()+"㎡");//场地面积
			specify_Headroom.setText("高度:"+mGetVenuesInfoBean.getHeadroom().toString()+"m");//净空高度
			specify_GroundMaterial.setText("地面材料:"+mGetVenuesInfoBean.getGroundmaterial());//地面材料
			specify_InternalFacilities.setText("内部设施:"+mGetVenuesInfoBean.getInternalfacilities());//内部设施
			specify_Investment.setText("投资金额:"+mGetVenuesInfoBean.getInvestment().toString()+"(万元)");//投资金额	
			specify_VenuesType.setText("运营性质:"+mGetVenuesInfoBean.getVenuestype());//运营性质 
			specify_VenuesEnvironment.setText("所属环境:"+mGetVenuesInfoBean.getVenuesenvironment());//所属环境
			specify_ResponsiblePeople.setText("场馆负责人:"+mGetVenuesInfoBean.getResponsiblepeople());//场馆负责人
			specify_Contact.setText("场馆联系人:"+mGetVenuesInfoBean.getContact());//场馆联系人
			specify_ContactPhone.setText("联系电话:"+mGetVenuesInfoBean.getContactphone());//联系电话
			specify_DirectorDept.setText("主管部门:"+mGetVenuesInfoBean.getDirectordept());//主管部门
			specify_Briefing.setText("场馆简介:"+mGetVenuesInfoBean.getBridfing());//场馆简介
			specify_BuiltTime.setText("建成时间:"+mGetVenuesInfoBean.getBuilttime());//建成时间
			specify_UseTime.setText("投入使用时间:"+mGetVenuesInfoBean.getUsetime());//投入使用时间
			specify_SeatNumber.setText("场馆坐席数量:"+mGetVenuesInfoBean.getSeatnumber().toString());//场馆坐席数量
			specify_AvgNumber.setText("每日平均客流量:"+mGetVenuesInfoBean.getAvgnumber().toString());//每日平均客流量
			specify_WhetherRecomm.setText("是否推荐:"+mGetVenuesInfoBean.getWhetherlock());//是否推荐 
			specify_Map.setText("地图链接:"+mGetVenuesInfoBean.getMap());//地图链接  
		}
	}
	
}
