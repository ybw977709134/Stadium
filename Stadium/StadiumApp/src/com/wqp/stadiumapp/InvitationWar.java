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
	private ImageView cursorImage;//����ͼƬ
	private int offset = 0;// ����ͼƬƫ����
	private int bmpW;// ����ͼƬ���
	private int currIndex = 0;// ��ǰҳ�����
	private InvitationWarViewPagerAdapter adapter; 
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.invitation_war, container,false);
		initeView(view);  
		InitImageView();
		initViewPager(); 
		
		return view;
	} 
	
	/** ��ʼ��ҳ���ϵ�����������¼�*/
	private void initeView(View view){
		invitation_war_back_arrow=(ImageView) view.findViewById(R.id.invitation_war_back_arrow);
		friend_replay_text=(TextView) view.findViewById(R.id.friend_replay_text);
		friend_recommend_text=(TextView) view.findViewById(R.id.friend_recommend_text);
		cursorImage=(ImageView) view.findViewById(R.id.cursor_image);
		mViewPager=(ViewPager) view.findViewById(R.id.invitation_war_viewpager);
		friend_replay_text.setOnClickListener(this);
		friend_recommend_text.setOnClickListener(this);
		
		//Ϊ���ذ�ť�󶨲˵�����Ӧ�¼�
		invitation_war_back_arrow.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) { 
				CommonSlidMenu.mSlidMenu.toggle();//��߲˵�����
			}
		});
	}
	
	/**��ViewPager���г�ʼ��*/
	private void initViewPager(){
		mFragmentList=new ArrayList<Fragment>();  
		mFragmentList.add(new FriendApplyFragment());//����
		mFragmentList.add(new FriendRecommendFragment());//���ѯ
		adapter=new InvitationWarViewPagerAdapter(getChildFragmentManager(), mFragmentList);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	/** �Զ����ĳ�ʼ��*/
	private void InitImageView() { 
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor_image).getWidth();// ��ȡͼƬ���
		int screenW = UtilScreen.getWindowWidth(getActivity());// ��ȡ�ֱ��ʿ��
		offset = (screenW / 2 - bmpW)/2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursorImage.setImageMatrix(matrix);// ���ö�����ʼλ��
	}
	
	
	@Override
	public void onClick(View v) { 
		switch(v.getId()){
		case R.id.friend_replay_text://��������
			mViewPager.setCurrentItem(0);
			break;
		case R.id.friend_recommend_text://�����Ƽ�
			mViewPager.setCurrentItem(1);
			break;
		}
	}
	
	private class MyOnPageChangeListener implements OnPageChangeListener{
		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
//		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����
		@Override
		public void onPageScrollStateChanged(int arg0) { }

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }

		@Override
		public void onPageSelected(int position) { 
			Animation animation = new TranslateAnimation(one*currIndex, one*position, 0, 0);
			currIndex = position;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(500);
			cursorImage.startAnimation(animation); 
		}
		
	} 
	
	@Override
	public void onDestroy() {
		mViewPager.setCurrentItem(0);//���ó�Ĭ�Ͻ���
		getActivity().unregisterReceiver(mReceiverBroadcase);//ע��������Լս���㲥
		getActivity().unregisterReceiver(mReceiverBroadcaseYingZhan);//ע�����ѯ��Ӧս���㲥
		super.onDestroy();
	}
	
	
	/** ע�ᷢ����Լս���㲥������ */
	private ReceiverBroadcase mReceiverBroadcase;
	/** ע����ѯ��Ӧս���㲥������ */
	private ReceiverBroadcaseYingZhan mReceiverBroadcaseYingZhan;
	@Override
	public void onAttach(Activity activity) { 
		/** ע�ᷢ����Լս���㲥*/
		mReceiverBroadcase=new ReceiverBroadcase();
		IntentFilter filter=new IntentFilter();
		filter.addAction("COM.WQP.STADIUMAPP_YUEZHAN_ACTION");
		activity.registerReceiver(mReceiverBroadcase, filter); //ע��Լս�㲥
		
		/** ע����ѯ��Ӧս���㲥*/
		mReceiverBroadcaseYingZhan=new ReceiverBroadcaseYingZhan();
		IntentFilter filter11=new IntentFilter();
		filter11.addAction("COM.WQP.STADIUMAPP_YINGZHAN_ACTION");
		activity.registerReceiver(mReceiverBroadcaseYingZhan, filter11); //ע����ѯ��Ӧս���㲥 
		super.onAttach(activity); 
	}
	
	
	/** ���巢����Լս���㲥������,ʵ�ֽ��յ��㲥�����������ҳ��Ĳ���*/
	public class ReceiverBroadcase extends BroadcastReceiver{ 
		public ReceiverBroadcase(){} 
		@Override
		public void onReceive(Context context, Intent intent) { 
			mViewPager.setCurrentItem(0);  
//			Toast.makeText(getActivity(), "���յ������㲥��", 0).show();
		} 
	}
	
	/** ������ѯ��Ӧս���㲥������,ʵ�ֽ��յ��㲥�����������ҳ��Ĳ���*/
	public class ReceiverBroadcaseYingZhan extends BroadcastReceiver{ 
		public ReceiverBroadcaseYingZhan(){} 
		@Override
		public void onReceive(Context context, Intent intent) { 
			mViewPager.setCurrentItem(1);  
//			Toast.makeText(getActivity(), "���յ����ѯ�㲥��", 0).show();
		} 
	}
}
