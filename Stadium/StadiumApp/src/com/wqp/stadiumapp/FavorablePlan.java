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
 * �ý��������Żݻ���棬�˹�����ʱȡ���������Ѿ����������ˣ���ʱ���ÿ����ˡ�
 * @author wqp
 * @2015��1��20��1:12:59 remark
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
//		//Ϊ���ذ�ť���¼�
//		favorable_plan_back_arrow.setOnClickListener(new OnClickListener(){ 
//			@Override
//			public void onClick(View v) { 
//				CommonSlidMenu.mSlidMenu.toggle();//��߲˵�����
//			} 
//		});
//		
//		
//		//ΪListView������ˢ���¼�,����������
//		favorable_plan_listview.setPullLoadEnable(true);
//		favorable_plan_listview.setPullRefreshEnable(false);//ͷ��ˢ�¹ر�
//		favorable_plan_listview.toggleHeadView=true;//��ʹ��ͷ��ˢ�¹���
//		
//		initListView();//Ϊlistview������ݲ����¼�
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
//		//Ϊlistview���������
//		favorable_plan_listview.setAdapter(mFavorablePlanListViewAdapter);
//		
//		//ΪListViewע������¼�
//		favorable_plan_listview.setXListViewListener(this);
//	}
//	
//	
//	
//	/** ��ListView����������� */
//	private List<Map<String,Object>> getListViewData(){
//		//JavaBean��װ����
//		FavorablePlanBean fpb=new FavorablePlanBean(
//				R.drawable.navigation_five_one, "11.3-19����H8̨��", "�������",
//				"2014-12-12��-2014-12-29", "H8̨��", 20,25);
//		
//		for(int i=0;i<6;i++){
//			Map<String,Object> map=new HashMap<String,Object>();
//			map.put("image", fpb.getImage());
//			map.put("title", fpb.getTitle());
//			map.put("initiator", fpb.getInitiator());
//			map.put("plantime", fpb.getPlantime());
//			map.put("planaddress", fpb.getPlanaddress());
//			map.put("praise", fpb.getPraise());//��������
//			map.put("message", fpb.getMessage());//��Ϣ����
//			mListViewData.add(map); 
//		}
//		return mListViewData;
//	}
//
//	/** ͷ������ˢ��,��δʹ��*/
//	@Override
//	public void onRefresh() { 
//		
//	}
//
//	
//	/**�����ListView�ײ����ظ���ʹ��,����ʹ�õ���Handler���м�������*/
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
//	/** ֹͣListView�ײ�ˢ�� */
//	private void onLoad() {
//		favorable_plan_listview.stopRefresh();
//		favorable_plan_listview.stopLoadMore();
//		favorable_plan_listview.setRefreshTime("�ո�");
//	}
//	
	
}
