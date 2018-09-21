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
	//�໬�˵���������
	private LinearLayout slide_menu_stadiumquery,slide_menu_privateformulate,
	slide_menu_domainwar,slide_menu_favorableplan,slide_menu_stadiumnews;
	private ImageView stadium_query_menu_index,stadium_query_menu_user_image,
					  stadium_query_menu_setting;
	private TextView stadium_query_menu_user_account;
	
	//���ݲ�ѯ��˽�˶��ƣ�����Լս���Żݻ���������ŵ�Fragment����
	private StadiumQuery mStadiumQuery;
	private PrivateFormulate mPrivateFormulate;
	private InvitationWar mInvitationWar;
	private FavorablePlan mFavorablePlan;
	private StadiumNews mStadiumNews;  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.stadium_fragment, container,false); 
		fm=getFragmentManager(); 
		
        //���ò໬�˵�
		CommonSlidMenu.mSlidMenu =new SlidingMenu(getActivity());
		CommonSlidMenu.mSlidMenu.setMode(SlidingMenu.LEFT);//���˵�
		CommonSlidMenu.mSlidMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//����Ҫʹ�˵�������������Ļ�ķ�Χ
		CommonSlidMenu.mSlidMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//SlidingMenu����ʱ��ҳ����ʾ��ʣ����	
		CommonSlidMenu.mSlidMenu.setFadeDegree(0.35F);//SlidingMenu����ʱ�Ľ���̶�
		CommonSlidMenu.mSlidMenu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
		CommonSlidMenu.mSlidMenu.setMenu(R.layout.stadium_query_menu);//���ò˵��Ĳ��� 
		//�Ӳ໬�˵����л�ȡ��ÿ����Ŀ�����
		stadium_query_menu_index=(ImageView) CommonSlidMenu.mSlidMenu.findViewById(R.id.stadium_query_menu_index);
		stadium_query_menu_user_image=(ImageView) CommonSlidMenu.mSlidMenu.findViewById(R.id.stadium_query_menu_user_image);
		stadium_query_menu_setting=(ImageView) CommonSlidMenu.mSlidMenu.findViewById(R.id.stadium_query_menu_setting);
		stadium_query_menu_user_account=(TextView) CommonSlidMenu.mSlidMenu.findViewById(R.id.stadium_query_menu_user_account);
		
		slide_menu_stadiumquery=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_stadiumquery);
		slide_menu_privateformulate=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_privateformulate);
		slide_menu_domainwar=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_domainwar);
		slide_menu_favorableplan=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_favorableplan);
		slide_menu_stadiumnews=(LinearLayout) CommonSlidMenu.mSlidMenu.findViewById(R.id.slide_menu_stadiumnews);
		CommonSlidMenu.mSlidMenu.setSlidingEnabled(false);//��ֹ 
		
		//����������¼�û����ǳƵ��໬�˵�������
		if(TextUtils.isEmpty(UserGlobal.NickName)){
			stadium_query_menu_user_account.setText(UserGlobal.NickName);
		}
		
		
		//Ϊ�˵���ѡ��󶨵���¼�
		stadium_query_menu_index.setOnClickListener(this);
		stadium_query_menu_setting.setOnClickListener(this);
		slide_menu_stadiumquery.setOnClickListener(this);
		slide_menu_privateformulate.setOnClickListener(this);
		slide_menu_domainwar.setOnClickListener(this);
		slide_menu_favorableplan.setOnClickListener(this);
		slide_menu_stadiumnews.setOnClickListener(this); 
 
		
        setChoiceFragment(R.id.slide_menu_stadiumquery);//����Ĭ�Ͻ����ǳ��ݲ�ѯ
		return view;
	} 
	
	@Override
	public void onClick(View v) { 
		switch(v.getId()){
		case R.id.stadium_query_menu_index://����˶���Ŀ¼��ͼ��
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.stadium_query_menu_setting://��������õ�ͼ��
//			Toast.makeText(getActivity(), "����", 0).show();
			break;
		case R.id.slide_menu_stadiumquery://���ݲ�ѯ
			setChoiceFragment(R.id.slide_menu_stadiumquery); 
//			Toast.makeText(getActivity(), "���ݲ�ѯ", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.slide_menu_privateformulate://˽�˶���
			setChoiceFragment(R.id.slide_menu_privateformulate); 
//			Toast.makeText(getActivity(), "˽�˶���", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.slide_menu_domainwar://����Լս
			setChoiceFragment(R.id.slide_menu_domainwar); 
//			Toast.makeText(getActivity(), "����Լս", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.slide_menu_favorableplan://�Żݻ
			setChoiceFragment(R.id.slide_menu_favorableplan); 
//			Toast.makeText(getActivity(), "�Żݻ", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break;
		case R.id.slide_menu_stadiumnews://��������
			setChoiceFragment(R.id.slide_menu_stadiumnews); 
//			Toast.makeText(getActivity(), "��������", 0).show();
			CommonSlidMenu.mSlidMenu.toggle();
			break; 
		} 
	} 
	
	/**���û�����ĵײ����д��� */
	public void setChoiceFragment(int id){
		//����FragmentTransaction����,�����Ҫ�ύ����
		FragmentTransaction transaction=fm.beginTransaction();
		//��������+�������е�Fragment,������ֽ��治������������
		resetAllFragment();//����Fragment������������
		hideAllFragment(transaction);//���� 
		switch(id){
		case R.id.slide_menu_stadiumquery://���ݲ�ѯ
			//����ImageView
			//����TextView
			if(mStadiumQuery==null){
				mStadiumQuery=new StadiumQuery();
				transaction.add(R.id.stadium_query_framelayout, mStadiumQuery);
			}else{
				transaction.show(mStadiumQuery);
			}
			break;
		case R.id.slide_menu_privateformulate://˽�˶���
//			main_home_icon.setImageResource(R.drawable.)
			if(mPrivateFormulate==null){
				mPrivateFormulate=new PrivateFormulate();
				transaction.add(R.id.stadium_query_framelayout, mPrivateFormulate);
			}else{
				transaction.show(mPrivateFormulate);
			}
			break;
		case R.id.slide_menu_domainwar://����Լս
			//����ImageView
			//����TextView
			if(mInvitationWar==null){
				mInvitationWar=new InvitationWar();
				transaction.add(R.id.stadium_query_framelayout, mInvitationWar);
			}else{
				transaction.show(mInvitationWar);
			}
			break;
			
		case R.id.slide_menu_favorableplan://�Żݻ 
			if(mFavorablePlan==null){
				mFavorablePlan=new FavorablePlan();
				transaction.add(R.id.stadium_query_framelayout, mFavorablePlan);
			}else{
				transaction.show(mFavorablePlan);
			}
			break;
		case R.id.slide_menu_stadiumnews://��������
			//����ImageView
			//����TextView
			if(mStadiumNews==null){
				mStadiumNews=new StadiumNews();
				transaction.add(R.id.stadium_query_framelayout, mStadiumNews);
			}else{
				transaction.show(mStadiumNews);
			}
			break;
		}
		//�м���Ҫ�ύ���񣬷���᲻��ʾ����
		transaction.commit();
	}
    
    
	/** ����Fragment������������(����ImageView,TextView) */
	private void resetAllFragment(){
		//����ImageView����
		//����TextView����
	}
	
	/** ��Fragmentҳ��ȫ������ */
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
	
	/** ע��Լս�㲥������ */
	private ReceiverBroadcase mReceiverBroadcase;
	/** ע��Ӧս�㲥������ */
	private ReceiverBroadcaseYingZhan mReceiverBroadcaseYingZhan;
	@Override
	public void onAttach(Activity activity) { 
		/** ע��Լս�㲥*/
		mReceiverBroadcase=new ReceiverBroadcase();
		IntentFilter filter=new IntentFilter();
		filter.addAction("COM.WQP.STADIUMAPP_YUEZHAN");
		activity.registerReceiver(mReceiverBroadcase, filter); //ע��Լս�㲥
		
		/** ע��Ӧս�㲥*/
		mReceiverBroadcaseYingZhan=new ReceiverBroadcaseYingZhan();
		IntentFilter filter11=new IntentFilter();
		filter11.addAction("ZZ_COM.WQP.STADIUMAPP_YINGZHAN");
		activity.registerReceiver(mReceiverBroadcaseYingZhan, filter11); //ע��Լս�㲥 
		
		super.onAttach(activity); 
	}
 
	
	@Override
	public void onDestroyView() { 
		getActivity().unregisterReceiver(mReceiverBroadcase);//ע��Լս�㲥
		getActivity().unregisterReceiver(mReceiverBroadcaseYingZhan);//ע��Ӧս�㲥
		super.onDestroyView();  
	}
	
	/** ����Լս�㲥������,ʵ�ֽ��յ��㲥�����������ҳ��Ĳ���*/
	class ReceiverBroadcase extends BroadcastReceiver{ 
		public ReceiverBroadcase(){} 
		@Override
		public void onReceive(Context context, Intent intent) { 
			setChoiceFragment(R.id.slide_menu_domainwar);  
			Intent aplay_action=new Intent();
			aplay_action.setAction("COM.WQP.STADIUMAPP_YUEZHAN_ACTION");
			getActivity().sendBroadcast(aplay_action);//����Լս����ķ����㲥
//			Toast.makeText(getActivity(), "���յ�Լս�㲥��", 0).show();
		} 
	}
	
	/** ����Ӧս�㲥������,ʵ�ֽ��յ��㲥�����������ҳ��Ĳ���*/
	class ReceiverBroadcaseYingZhan extends BroadcastReceiver{ 
		public ReceiverBroadcaseYingZhan(){} 
		@Override
		public void onReceive(Context context, Intent intent) { 
			setChoiceFragment(R.id.slide_menu_domainwar); 
			
			Intent query_action=new Intent();
			query_action.setAction("COM.WQP.STADIUMAPP_YINGZHAN_ACTION");
			getActivity().sendBroadcast(query_action);//����Ӧս����Ļ��ѯ�㲥
//			Toast.makeText(getActivity(), "���յ�Ӧս�㲥��", 0).show();
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
