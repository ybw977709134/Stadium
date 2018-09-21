package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle; 
import android.support.v4.app.Fragment; 
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
import com.wqp.stadiumapp.adapter.InvitationWarViewPagerAdapter;
import com.wqp.stadiumapp.fragment.FriendApplyFragment;
import com.wqp.stadiumapp.fragment.FriendRecommendFragment; 
import com.wqp.stadiumapp.utils.CommonSlidMenu;
import com.wqp.stadiumapp.utils.UtilScreen;

public class InvitationWar extends Fragment implements OnClickListener{
	private ViewPager mViewPager;
	private ImageView invitation_war_back_arrow;
//	private Fragment FriendApplyFragment,FriendRecommendFragment;
	private List<Fragment> mFragmentList;
	private TextView friend_replay_text,friend_recommend_text;
	private ImageView cursorImage;//动画图片
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	private int currIndex = 0;// 当前页卡编号
	private InvitationWarViewPagerAdapter adapter; 
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.invitation_war, container,false);
		initeView(view);  
		InitImageView();
		initViewPager(); 
		
		return view;
	} 
	
	/** 初始化页面上的组件，并绑定事件*/
	private void initeView(View view){
		invitation_war_back_arrow=(ImageView) view.findViewById(R.id.invitation_war_back_arrow);
		friend_replay_text=(TextView) view.findViewById(R.id.friend_replay_text);
		friend_recommend_text=(TextView) view.findViewById(R.id.friend_recommend_text);
		cursorImage=(ImageView) view.findViewById(R.id.cursor_image);
		mViewPager=(ViewPager) view.findViewById(R.id.invitation_war_viewpager);
		friend_replay_text.setOnClickListener(this);
		friend_recommend_text.setOnClickListener(this);
		
		//为返回按钮绑定菜单栏响应事件
		invitation_war_back_arrow.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) { 
				CommonSlidMenu.mSlidMenu.toggle();//侧边菜单出现
			}
		});
	}
	
	/**对ViewPager进行初始化*/
	private void initViewPager(){
		mFragmentList=new ArrayList<Fragment>();  
		mFragmentList.add(new FriendApplyFragment());//发起活动
		mFragmentList.add(new FriendRecommendFragment());//活动查询
		adapter=new InvitationWarViewPagerAdapter(getChildFragmentManager(), mFragmentList);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	/** 对动画的初始化*/
	private void InitImageView() { 
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor_image).getWidth();// 获取图片宽度
		int screenW = UtilScreen.getWindowWidth(getActivity());// 获取分辨率宽度
		offset = (screenW / 2 - bmpW)/2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursorImage.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	
	@Override
	public void onClick(View v) { 
		switch(v.getId()){
		case R.id.friend_replay_text://好友申请
			mViewPager.setCurrentItem(0);
			break;
		case R.id.friend_recommend_text://好友推荐
			mViewPager.setCurrentItem(1);
			break;
		}
	}
	
	private class MyOnPageChangeListener implements OnPageChangeListener{
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
//		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		@Override
		public void onPageScrollStateChanged(int arg0) { }

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }

		@Override
		public void onPageSelected(int position) { 
			Animation animation = new TranslateAnimation(one*currIndex, one*position, 0, 0);
			currIndex = position;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(500);
			cursorImage.startAnimation(animation); 
		}
		
	} 
	
	@Override
	public void onDestroy() {
		mViewPager.setCurrentItem(0);//设置成默认界面
		getActivity().unregisterReceiver(mReceiverBroadcase);//注销发起活动（约战）广播
		getActivity().unregisterReceiver(mReceiverBroadcaseYingZhan);//注销活动查询（应战）广播
		super.onDestroy();
	}
	
	
	/** 注册发起活动（约战）广播接收者 */
	private ReceiverBroadcase mReceiverBroadcase;
	/** 注册活动查询（应战）广播接收者 */
	private ReceiverBroadcaseYingZhan mReceiverBroadcaseYingZhan;
	@Override
	public void onAttach(Activity activity) { 
		/** 注册发起活动（约战）广播*/
		mReceiverBroadcase=new ReceiverBroadcase();
		IntentFilter filter=new IntentFilter();
		filter.addAction("COM.WQP.STADIUMAPP_YUEZHAN_ACTION");
		activity.registerReceiver(mReceiverBroadcase, filter); //注册约战广播
		
		/** 注册活动查询（应战）广播*/
		mReceiverBroadcaseYingZhan=new ReceiverBroadcaseYingZhan();
		IntentFilter filter11=new IntentFilter();
		filter11.addAction("COM.WQP.STADIUMAPP_YINGZHAN_ACTION");
		activity.registerReceiver(mReceiverBroadcaseYingZhan, filter11); //注册活动查询（应战）广播 
		super.onAttach(activity); 
	}
	
	
	/** 定义发起活动（约战）广播接收者,实现接收到广播后进行启动该页面的操作*/
	public class ReceiverBroadcase extends BroadcastReceiver{ 
		public ReceiverBroadcase(){} 
		@Override
		public void onReceive(Context context, Intent intent) { 
			mViewPager.setCurrentItem(0);  
//			Toast.makeText(getActivity(), "接收到发起活动广播了", 0).show();
		} 
	}
	
	/** 定义活动查询（应战）广播接收者,实现接收到广播后进行启动该页面的操作*/
	public class ReceiverBroadcaseYingZhan extends BroadcastReceiver{ 
		public ReceiverBroadcaseYingZhan(){} 
		@Override
		public void onReceive(Context context, Intent intent) { 
			mViewPager.setCurrentItem(1);  
//			Toast.makeText(getActivity(), "接收到活动查询广播了", 0).show();
		} 
	}
}
