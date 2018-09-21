package com.wqp.stadiumapp.fragment;
   
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wqp.stadiumapp.FavorablePlan;
import com.wqp.stadiumapp.InvitationWar; 
import com.wqp.stadiumapp.PrivateFormulate;
import com.wqp.stadiumapp.R;  
import com.wqp.stadiumapp.StadiumNews;
import com.wqp.stadiumapp.StadiumQuery;
import com.wqp.stadiumapp.utils.CommonSlidMenu;
 
import com.wqp.stadiumapp.utils.UserGlobal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction; 
import android.text.TextUtils;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.View.OnClickListener; 
import android.view.ViewGroup;  
import android.widget.ImageView;
import android.widget.LinearLayout; 
import android.widget.TextView;
import android.widget.Toast; 

public class StadiumFragment extends Fragment implements OnClickListener{  
	public FragmentManager fm;
	//侧滑菜单上面的组件
	private LinearLayout slide_menu_stadiumquery,slide_menu_privateformulate,
	slide_menu_domainwar,slide_menu_favorableplan,slide_menu_stadiumnews;
	private ImageView stadium_query_menu_index,stadium_query_menu_user_image,
					  stadium_query_menu_setting;
	private TextView stadium_query_menu_user_account;
	
	//场馆查询，私人定制，地盘约战，优惠活动，场馆新闻的Fragment声明
	private StadiumQuery mStadiumQuery;
	private PrivateFormulate mPrivateFormulate;
	private InvitationWar mInvitationWar;
	private FavorablePlan mFavorablePlan;
	private StadiumNews mStadiumNews;  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.stadium_fragment, container,false); 
		fm=getFragmentManager(); 
		
        //设置侧滑菜单
		CommonSlidMenu.mSlidMenu =new SlidingMenu(getActivity());
		CommonSlidMenu.mSlidMenu.setMode(SlidingMenu.LEFT);//左侧菜单
		CommonSlidMenu.mSlidMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//设置要使菜单滑动，触碰屏幕的范围
		CommonSlidMenu.mSlidMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//SlidingMenu划出时主页面显示的剩余宽度	
		CommonSlidMenu.mSlidMenu.setFadeDegree(0.35F);//SlidingMenu滑动时的渐变程度
		CommonSlidMenu.mSlidMenu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
		CommonSlidMenu.mSlidMenu.setMenu(R.layout.stadium_query_menu);//设置菜单的布局 
		//从侧滑菜单当中获取到每个条目的组件
		stadium_query_menu_index=(ImageView) CommonSlidMenu.mSlidMenu.findViewById(R.id.stadium_query_menu_index);
		stadium_query_menu_user_image=(ImageView) CommonSlidMenu.mSlidMenu.findViewById(R.id.stadium_query_menu_user_image);
		stadium_query_menu_setting=(ImageView) CommonSlidMenu.mSlidMenu.findViewById(R.id.stadium_query_menu_setting);
		stadium_query_menu_user_account=(TextView) CommonSlidMenu.mSlidMenu.findViewById(R.id.stadium_query_menu_user_account);
		
		slide_menu_stadiumquery=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_stadiumquery);
		slide_menu_privateformulate=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_privateformulate);
		slide_menu_domainwar=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_domainwar);
		slide_menu_favorableplan=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_favorableplan);
		slide_menu_stadiumnews=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_stadiumnews);
		CommonSlidMenu.mSlidMenu.setSlidingEnabled(false);//禁止 
		
		//设置正常登录用户的昵称到侧滑菜单栏上面
		if(TextUtils.isEmpty(UserGlobal.NickName)){
			stadium_query_menu_user_account.setText(UserGlobal.NickName);
		}
		
		
		//为菜单的选项绑定点击事件
		stadium_query_menu_index.setOnClickListener(this);
		stadium_query_menu_setting.setOnClickListener(this);
		slide_menu_stadiumquery.setOnClickListener(this);
		slide_menu_privateformulate.setOnClickListener(this);
		slide_menu_domainwar.setOnClickListener(this);
		slide_menu_favorableplan.setOnClickListener(this);
		slide_menu_stadiumnews.setOnClickListener(this); 
 
		
        setChoiceFragment(R.id.slide_menu_stadiumquery);//设置默认界面是场馆查询
		return view;
	} 
	
	@Override
	public void onClick(View v) { 
		switch(v.getId()){
		case R.id.stadium_query_menu_index://点击了顶部目录的图标
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.stadium_query_menu_setting://点击了设置的图标
//			Toast.makeText(getActivity(), "设置", 0).show();
			break;
		case R.id.slide_menu_stadiumquery://场馆查询
			setChoiceFragment(R.id.slide_menu_stadiumquery); 
//			Toast.makeText(getActivity(), "场馆查询", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.slide_menu_privateformulate://私人定制
			setChoiceFragment(R.id.slide_menu_privateformulate); 
//			Toast.makeText(getActivity(), "私人定制", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.slide_menu_domainwar://地盘约战
			setChoiceFragment(R.id.slide_menu_domainwar); 
//			Toast.makeText(getActivity(), "地盘约战", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.slide_menu_favorableplan://优惠活动
			setChoiceFragment(R.id.slide_menu_favorableplan); 
//			Toast.makeText(getActivity(), "优惠活动", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.slide_menu_stadiumnews://场馆新闻
			setChoiceFragment(R.id.slide_menu_stadiumnews); 
//			Toast.makeText(getActivity(), "场馆新闻", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break; 
		} 
	} 
	
	/**对用户点击的底部进行处理 */
	public void setChoiceFragment(int id){
		//开启FragmentTransaction事务,最后需要提交事务
		FragmentTransaction transaction=fm.beginTransaction();
		//首先重置+隐藏所有的Fragment,避免出现界面不流畅的现象发生
		resetAllFragment();//重置Fragment里面的所有组件
		hideAllFragment(transaction);//隐藏 
		switch(id){
		case R.id.slide_menu_stadiumquery://场馆查询
			//设置ImageView
			//设置TextView
			if(mStadiumQuery==null){
				mStadiumQuery=new StadiumQuery();
				transaction.add(R.id.stadium_query_framelayout, mStadiumQuery);
			}else{
				transaction.show(mStadiumQuery);
			}
			break;
		case R.id.slide_menu_privateformulate://私人定制
//			main_home_icon.setImageResource(R.drawable.)
			if(mPrivateFormulate==null){
				mPrivateFormulate=new PrivateFormulate();
				transaction.add(R.id.stadium_query_framelayout, mPrivateFormulate);
			}else{
				transaction.show(mPrivateFormulate);
			}
			break;
		case R.id.slide_menu_domainwar://地盘约战
			//设置ImageView
			//设置TextView
			if(mInvitationWar==null){
				mInvitationWar=new InvitationWar();
				transaction.add(R.id.stadium_query_framelayout, mInvitationWar);
			}else{
				transaction.show(mInvitationWar);
			}
			break;
			
		case R.id.slide_menu_favorableplan://优惠活动 
			if(mFavorablePlan==null){
				mFavorablePlan=new FavorablePlan();
				transaction.add(R.id.stadium_query_framelayout, mFavorablePlan);
			}else{
				transaction.show(mFavorablePlan);
			}
			break;
		case R.id.slide_menu_stadiumnews://场馆新闻
			//设置ImageView
			//设置TextView
			if(mStadiumNews==null){
				mStadiumNews=new StadiumNews();
				transaction.add(R.id.stadium_query_framelayout, mStadiumNews);
			}else{
				transaction.show(mStadiumNews);
			}
			break;
		}
		//切记需要提交事务，否则会不显示界面
		transaction.commit();
	}
    
    
	/** 重置Fragment里面的所有组件(包括ImageView,TextView) */
	private void resetAllFragment(){
		//设置ImageView重置
		//设置TextView重置
	}
	
	/** 把Fragment页面全部隐藏 */
	private void hideAllFragment(FragmentTransaction transaction){
		if(mStadiumQuery!=null){
			transaction.hide(mStadiumQuery);
		}
		if(mPrivateFormulate!=null){
			transaction.hide(mPrivateFormulate);
		}
		if(mInvitationWar!=null){
			transaction.hide(mInvitationWar);
		}
		if(mFavorablePlan!=null){
			transaction.hide(mFavorablePlan);
		}
		if(mStadiumNews!=null){
			transaction.hide(mStadiumNews);
		}
	} 
	
	/** 注册约战广播接收者 */
	private ReceiverBroadcase mReceiverBroadcase;
	/** 注册应战广播接收者 */
	private ReceiverBroadcaseYingZhan mReceiverBroadcaseYingZhan;
	@Override
	public void onAttach(Activity activity) { 
		/** 注册约战广播*/
		mReceiverBroadcase=new ReceiverBroadcase();
		IntentFilter filter=new IntentFilter();
		filter.addAction("COM.WQP.STADIUMAPP_YUEZHAN");
		activity.registerReceiver(mReceiverBroadcase, filter); //注册约战广播
		
		/** 注册应战广播*/
		mReceiverBroadcaseYingZhan=new ReceiverBroadcaseYingZhan();
		IntentFilter filter11=new IntentFilter();
		filter11.addAction("ZZ_COM.WQP.STADIUMAPP_YINGZHAN");
		activity.registerReceiver(mReceiverBroadcaseYingZhan, filter11); //注册约战广播 
		
		super.onAttach(activity); 
	}
 
	
	@Override
	public void onDestroyView() { 
		getActivity().unregisterReceiver(mReceiverBroadcase);//注销约战广播
		getActivity().unregisterReceiver(mReceiverBroadcaseYingZhan);//注销应战广播
		super.onDestroyView();  
	}
	
	/** 定义约战广播接收者,实现接收到广播后进行启动该页面的操作*/
	class ReceiverBroadcase extends BroadcastReceiver{ 
		public ReceiverBroadcase(){} 
		@Override
		public void onReceive(Context context, Intent intent) { 
			setChoiceFragment(R.id.slide_menu_domainwar);  
			Intent aplay_action=new Intent();
			aplay_action.setAction("COM.WQP.STADIUMAPP_YUEZHAN_ACTION");
			getActivity().sendBroadcast(aplay_action);//发送约战里面的发起活动广播
//			Toast.makeText(getActivity(), "接收到约战广播了", 0).show();
		} 
	}
	
	/** 定义应战广播接收者,实现接收到广播后进行启动该页面的操作*/
	class ReceiverBroadcaseYingZhan extends BroadcastReceiver{ 
		public ReceiverBroadcaseYingZhan(){} 
		@Override
		public void onReceive(Context context, Intent intent) { 
			setChoiceFragment(R.id.slide_menu_domainwar); 
			
			Intent query_action=new Intent();
			query_action.setAction("COM.WQP.STADIUMAPP_YINGZHAN_ACTION");
			getActivity().sendBroadcast(query_action);//发送应战里面的活动查询广播
//			Toast.makeText(getActivity(), "接收到应战广播了", 0).show();
		} 
	} 
	
	
	
	
	/** custom interface */
	public interface IStadiumFragment{
		void XStadiumFragment(StadiumFragment sf);
	}
	 
	/** custom abstract class */
	public abstract class AbsStadiumFragment{
		
	}
}
