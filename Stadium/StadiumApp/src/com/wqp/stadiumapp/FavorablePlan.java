package com.wqp.stadiumapp;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
// 
//import android.os.Bundle;
//import android.os.Handler;
import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.ImageView; 
//
//import com.wqp.stadiumapp.adapter.FavorablePlanListViewAdapter;
//import com.wqp.stadiumapp.entity.FavorablePlanBean; 
//import com.wqp.stadiumapp.utils.CommonSlidMenu;
//import com.wqp.view.XListView;
//import com.wqp.view.XListView.IXListViewListener;

/**
 * 该界面属于优惠活动界面，此功能暂时取消，界面已经隐藏起来了，暂时不用开发了。
 * @author wqp
 * @2015年1月20日1:12:59 remark
 */
public class FavorablePlan extends Fragment{// implements IXListViewListener {
//	private Handler mHandler=new Handler();
//	private ImageView favorable_plan_back_arrow;
//	private XListView favorable_plan_listview;
//	private List<Map<String,Object>> mListViewData;
//	private FavorablePlanListViewAdapter mFavorablePlanListViewAdapter;
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View view=inflater.inflate(R.layout.favorable_plan, container,false);
//		favorable_plan_back_arrow=(ImageView) view.findViewById(R.id.favorable_plan_back_arrow);
//		favorable_plan_listview=(XListView) view.findViewById(R.id.favorable_plan_listview);
//		
//		//为返回按钮绑定事件
//		favorable_plan_back_arrow.setOnClickListener(new OnClickListener(){ 
//			@Override
//			public void onClick(View v) { 
//				CommonSlidMenu.mSlidMenu.toggle();//侧边菜单出现
//			} 
//		});
//		
//		
//		//为ListView绑定上拉刷新事件,并进行配置
//		favorable_plan_listview.setPullLoadEnable(true);
//		favorable_plan_listview.setPullRefreshEnable(false);//头部刷新关闭
//		favorable_plan_listview.toggleHeadView=true;//不使用头部刷新功能
//		
//		initListView();//为listview填充数据并绑定事件
//		
//		return view;
//	}  
//	
//	private void initListView(){
//		mListViewData=new ArrayList<Map<String,Object>>(); 
//		
//		mFavorablePlanListViewAdapter=new FavorablePlanListViewAdapter(getActivity(), 
//				getListViewData(), 
//				R.layout.favorable_plan_listview_item, 
//				new String[]{"image","title","initiator","plantime","planaddress","praise","message"}, 
//				new int[]{
//						R.id.favorable_plan_listview_image,
//						R.id.favorable_plan_listview_title,
//						R.id.favorable_plan_listview_initiator,
//						R.id.favorable_plan_listview_plantime,
//						R.id.favorable_plan_listview_planaddress,
//						R.id.favorable_plan_listview_praise,
//						R.id.favorable_plan_listview_message });  
//		
//		//为listview填充适配器
//		favorable_plan_listview.setAdapter(mFavorablePlanListViewAdapter);
//		
//		//为ListView注册监听事件
//		favorable_plan_listview.setXListViewListener(this);
//	}
//	
//	
//	
//	/** 给ListView进行填充数据 */
//	private List<Map<String,Object>> getListViewData(){
//		//JavaBean封装数据
//		FavorablePlanBean fpb=new FavorablePlanBean(
//				R.drawable.navigation_five_one, "11.3-19周六H8台球活动", "单身情歌",
//				"2014-12-12至-2014-12-29", "H8台球", 20,25);
//		
//		for(int i=0;i<6;i++){
//			Map<String,Object> map=new HashMap<String,Object>();
//			map.put("image", fpb.getImage());
//			map.put("title", fpb.getTitle());
//			map.put("initiator", fpb.getInitiator());
//			map.put("plantime", fpb.getPlantime());
//			map.put("planaddress", fpb.getPlanaddress());
//			map.put("praise", fpb.getPraise());//点赞数量
//			map.put("message", fpb.getMessage());//消息数量
//			mListViewData.add(map); 
//		}
//		return mListViewData;
//	}
//
//	/** 头部下拉刷新,暂未使用*/
//	@Override
//	public void onRefresh() { 
//		
//	}
//
//	
//	/**这个是ListView底部加载更多使用,这里使用到了Handler进行加载数据*/
//	@Override
//	public void onLoadMore() { 
//		mHandler.postDelayed(new Runnable(){ 
//			@Override
//			public void run() { 
//				getListViewData();
//				mFavorablePlanListViewAdapter.notifyDataSetChanged();
//				onLoad();
//			} 
//		}, 2000);
//	}
//	
//	/** 停止ListView底部刷新 */
//	private void onLoad() {
//		favorable_plan_listview.stopRefresh();
//		favorable_plan_listview.stopLoadMore();
//		favorable_plan_listview.setRefreshTime("刚刚");
//	}
//	
	
}
