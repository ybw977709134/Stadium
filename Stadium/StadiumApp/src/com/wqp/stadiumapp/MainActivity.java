package com.wqp.stadiumapp;
  
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.wqp.stadiumapp.fragment.HomeFragment;
import com.wqp.stadiumapp.fragment.PersonalFragment;
import com.wqp.stadiumapp.fragment.StadiumFragment;
import com.wqp.stadiumapp.utils.CommonSlidMenu;
import com.wqp.stadiumapp.utils.Exit;
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.webservice.WebGetSports;
import com.wqp.webservice.entity.GetSportBean;

public class MainActivity extends FragmentActivity implements OnClickListener { 
	private static String TAG="MainActivity";
	private ImageView main_home_icon,main_stadium_icon,main_personalcenter_icon;
	
	//��������Fragment
	private HomeFragment mHomeFragment;
	private StadiumFragment mStadiumFragment;
	private PersonalFragment mPersonalFragment; 
	
	//����FragmentManager������
	private FragmentManager fm; 
	
	private List<GetSportBean> DataSet;//�ӷ���˷��ص����ݼ�
	private int xx=0;
	//������Ϣ������
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x11:
				DataSet=(List<GetSportBean>) msg.obj; 
				if(DataSet!=null && DataSet.size()>0){ 
					Log.i(TAG,"�ӷ���˻�ȡ�����˶��������ݳ���DataSet�Ĳ���Ϊ:"+DataSet.size()+"��");
					if(!ToolsHome.addDataSet(DataSet)){
						xx++;
						if(xx<2){
							conditionQuery();
						}else{
							xx=0;
						}
					}else{
						Log.i(TAG,"�ӷ���˻�ȡ�����˶��������ݳ���Ϊ:"+ToolsHome.SPORTTYPE_Integer.size()+"��");
					}
				} 
				break; 
			case 0x22:
				Log.i(TAG,"Ӧ���״����������˶�����ʧ����");
				break;
			} 
		};
	};
	/**
	 * Launch Home activity helper
	 * @param c c context where launch home from (used by Navigate)
	 */
	public static void launch(Context c){
		Intent intent=new Intent(c,MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		c.startActivity(intent);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        
        //������ͨ�û��������ݻظ�����Ϣ����
        UserGlobal.user_intent.setAction("com.wqp.stadiumapp.UserService");
        startService(UserGlobal.user_intent);
        
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		conditionQuery();//�ӷ���˼�������
        fm=getSupportFragmentManager();//��ȡ��FragmentManager������
        initView();//��ʼ����� 
        
        setChoiceFragment(R.id.main_stadium);//��Ϊ��������Լս�����Ҫֱ����ת��������������Ҫ�ѽ���ʵ����һ�¡�
        if(CommonSlidMenu.mSlidMenu!=null && CommonSlidMenu.mSlidMenu.isSlidingEnabled()){ 
			CommonSlidMenu.mSlidMenu.setSlidingEnabled(false); 
		} 
        setChoiceFragment(R.id.main_home);//���ó�������Ĭ��ҳ��  
    }
    
    /** ��������г�ʼ�����󶨵���¼� */
    private void initView(){ 
    	main_home_icon=(ImageView) findViewById(R.id.main_home);
    	main_stadium_icon=(ImageView) findViewById(R.id.main_stadium);
    	main_personalcenter_icon=(ImageView) findViewById(R.id.main_personalcenter); 
    	
    	main_home_icon.setOnClickListener(this);
    	main_stadium_icon.setOnClickListener(this);
    	main_personalcenter_icon.setOnClickListener(this); 
    } 
    
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.main_home:
			setChoiceFragment(R.id.main_home);
			if(CommonSlidMenu.mSlidMenu!=null && CommonSlidMenu.mSlidMenu.isSlidingEnabled()){ 
				CommonSlidMenu.mSlidMenu.setSlidingEnabled(false); 
			} 
			break;
		case R.id.main_stadium:
			if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){//�ж��û��Ƿ��Ѿ�������¼
				setChoiceFragment(R.id.main_stadium); 
				if(CommonSlidMenu.mSlidMenu!=null && !CommonSlidMenu.mSlidMenu.isSlidingEnabled()){ 
					CommonSlidMenu.mSlidMenu.setSlidingEnabled(true);
					CommonSlidMenu.mSlidMenu.toggle();
				}
			}else{
				Toast.makeText(MainActivity.this, "���ȵ�¼", 0).show();
				Intent intenteLogin=new Intent(MainActivity.this,Login.class);
				MainActivity.this.startActivity(intenteLogin);
			} 
			break;
		case R.id.main_personalcenter:
			if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){//����û��ɹ���¼�Ϳ����л�ҳ�����
				setChoiceFragment(R.id.main_personalcenter);
				if(CommonSlidMenu.mSlidMenu!=null && CommonSlidMenu.mSlidMenu.isSlidingEnabled()){ 
					CommonSlidMenu.mSlidMenu.setSlidingEnabled(false); 
				}
			}else{//û�е�¼����ת����¼����
				Toast.makeText(MainActivity.this, "���ȵ�¼", 0).show();
				Intent jump=new Intent(MainActivity.this,Login.class);
				startActivity(jump);//��ת����¼����
			}
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
		case R.id.main_home:
			//����ImageView 
			main_home_icon.setImageResource(R.drawable.home_select);
			if(mHomeFragment==null){
				mHomeFragment=new HomeFragment();
				transaction.add(R.id.main_framelayout, mHomeFragment,"HomeFragment");
			}else{
				transaction.show(mHomeFragment);
			}
			break;
		case R.id.main_stadium:
			main_stadium_icon.setImageResource(R.drawable.stadium_select);
			if(mStadiumFragment==null){
				mStadiumFragment=new StadiumFragment();
				transaction.add(R.id.main_framelayout, mStadiumFragment,"StadiumFragment");
			}else{
				transaction.show(mStadiumFragment);
			}
			break;
		case R.id.main_personalcenter:
			//����ImageView 
			main_personalcenter_icon.setImageResource(R.drawable.personal_select);
			if(mPersonalFragment==null){
				mPersonalFragment=new PersonalFragment();
				transaction.add(R.id.main_framelayout, mPersonalFragment,"PersonalFragment");
			}else{
				transaction.show(mPersonalFragment);
			}
			break;
		}
		//�м���Ҫ�ύ���񣬷���᲻��ʾ����
		transaction.commit();
	}
    
    
	/** ����Fragment������������(����ImageView,TextView) */
	private void resetAllFragment(){
		//����ImageView���� 
		main_home_icon.setImageResource(R.drawable.home_normal);
		main_stadium_icon.setImageResource(R.drawable.stadium_normal);
		main_personalcenter_icon.setImageResource(R.drawable.personal_normal); 
	}
	
	/** ��Fragmentҳ��ȫ������ */
	public void hideAllFragment(FragmentTransaction transaction){
		if(mHomeFragment!=null){
			transaction.hide(mHomeFragment);
		}
		if(mStadiumFragment!=null){
			transaction.hide(mStadiumFragment);
		}
		if(mPersonalFragment!=null){
			transaction.hide(mPersonalFragment);
		}
	}
	
	
    /** �˵�*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
//		stopService(UserGlobal.user_intent);//ֹͣ��������ͨ�û�����
//		stopService(UserGlobal.admin_intent);//ֹͣ�����ĳ����û�����
		Exit.getInstance().destroyActivity();//�������е�Activity����
		android.os.Process.killProcess(android.os.Process.myPid());//ֱ��ɱ�����̽����˳����
		return super.onOptionsItemSelected(item);
	}
	
	private int mBackKeyPressedTimes=0;
	//���˼�
	@Override
	public void onBackPressed() { 
		if(mBackKeyPressedTimes==0){
			Toast.makeText(MainActivity.this, "�ٰ�һ���˳�����", 0).show();
			mBackKeyPressedTimes=1;
			new Thread(){
				public void run(){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}finally{
						mBackKeyPressedTimes=0;
					}
				}
			}.start();
			return;
		}else{
//			stopService(UserGlobal.user_intent);//ֹͣ��������ͨ�û�����
//			stopService(UserGlobal.admin_intent);//ֹͣ�����ĳ����û�����
			Exit.getInstance().destroyActivity();//�������е�Activity����
			android.os.Process.killProcess(android.os.Process.myPid());//ֱ��ɱ�����̽����˳����
		} 
		super.onBackPressed(); 
	}
	
	/** �����˶���������*/
	public void conditionQuery() {   
		new Thread(){   
			public void run() {
				try { 
					List<GetSportBean> results=WebGetSports.getGetSportsData("");
					if(results!=null){
						Message msg=new Message();
						msg.what=0x11;//��ʶ������ȡ��������
						msg.obj=results;
						mHandler.sendMessage(msg); 
						Log.i(TAG,"Web���̼߳������ݽ���,���Ѿ��������ݵ�Handler��");
					}else{
						mHandler.sendEmptyMessage(0x22);//���ͱ�ʶ��ȡ��������Ϊ��
					}
				} catch (Exception e) {  
					e.printStackTrace(); 
				}  
			};
		}.start();
	}
    
}
