package com.wqp.stadiumapp;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wqp.stadiumapp.utils.ToolsNavigate;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.webservice.WebGetAUser;
import com.wqp.webservice.WebLogin;
import com.wqp.webservice.entity.GetLoginBean;
import com.wqp.webservice.entity.GetUserInfoBean;

public class Login extends Activity implements OnClickListener {
	@SuppressWarnings("unused")
	private LinearLayout login_register;
	private ImageView login_back_arrow;//����ͼ�갴ť
	private EditText input_username,input_password;
	private CheckBox remember_password,auto_login;
	private Button register_button,login_button;
	private TextView forget_password;
	private ProgressDialog mProgressDialog;
	
	private SharedPreferences mSharedPrefs;
	private SharedPreferences.Editor mEditor;
	
	//������Ϣ������
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://��¼�ɹ�
				Log.i("��¼״̬:","��¼�ɹ�");
				break; 
			case 0x12://����������
				Toast.makeText(Login.this, "����������", 0).show(); 
				break;
			case 0x13://��¼ʧ�� 
				Toast.makeText(Login.this, "��¼ʧ��", 1).show();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
//		int ScreenWidth=UtilScreen.getWindowWidth(Login.this);
//		int ScreenHeight=UtilScreen.getWindowHeight(Login.this); 
//		login_register=(LinearLayout) findViewById(R.id.login_register); 
//		AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ScreenWidth-80, ScreenHeight-100);
//		login_register.setLayoutParams(lp); 

		setContentView(R.layout.login_register); 
		initView();  
		
		//��ȡ��SharedPrederencesʵ��
		mSharedPrefs=getSharedPreferences("login_data", Context.MODE_PRIVATE);
		mEditor=mSharedPrefs.edit();
		
		String username=mSharedPrefs.getString("username", null);
		String password=mSharedPrefs.getString("password", null);
		boolean autoRes=mSharedPrefs.getBoolean("auto_log", false);
		
		if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
			input_username.setText(username);//����û���
			input_password.setText(password);//�������
		} 
		if(autoRes && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
			onClick(login_button);//���õ���¼��ĵ�¼��ť����
		}
	}
	 
	
	/** ��ҳ���ϵ�����������г�ʼ���������¼�*/
	private void initView(){
		login_back_arrow=(ImageView) findViewById(R.id.login_back_arrow);
		input_username=(EditText) findViewById(R.id.input_username);
		input_password=(EditText) findViewById(R.id.input_password);
		remember_password=(CheckBox) findViewById(R.id.remember_password);
		auto_login=(CheckBox) findViewById(R.id.auto_login);
		register_button=(Button) findViewById(R.id.register_button);
		login_button=(Button) findViewById(R.id.login_button);
		forget_password=(TextView) findViewById(R.id.forget_password); 
		forget_password.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//��TextView����»���
		
		login_back_arrow.setOnClickListener(this);//����ͼ��
		login_button.setOnClickListener(this);//��¼��ť
		register_button.setOnClickListener(this);//ע�ᰴť
		forget_password.setOnClickListener(this);//��������
	}

	@Override
	public void onClick(View v) {  
		Intent intent=null;
		switch(v.getId()){
		case R.id.login_back_arrow://���� 
			finish();
			break;
		case R.id.login_button://��¼
			if(!ToolsNavigate.isNetworkAvailable(getApplicationContext())){
				Toast.makeText(Login.this, "��,����û��Ŷ", 1).show();return;
			}
			final String account=input_username.getText().toString().trim();
			final String passowrd=input_password.getText().toString().trim();
			boolean remember_psw=remember_password.isChecked();
			boolean auto_log=auto_login.isChecked(); 
			
			if((account!=null && !TextUtils.isEmpty(account)) && (passowrd!=null && !TextUtils.isEmpty(passowrd))){ 
				//������Ҫ��¼�û���������
				if(remember_psw){
					mEditor.clear();
					mEditor.putString("username", account);
					mEditor.putString("password", passowrd);
					mEditor.commit();
				}
				
				//�����Ҫ��¼�Զ���¼
				if(auto_log){
					mEditor.putBoolean("auto_log", true);
					mEditor.commit();
				}
				
				mProgressDialog=new ProgressDialog(Login.this);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setMessage("��¼��...");
				mProgressDialog.setIndeterminate(true); 
				mProgressDialog.setCancelable(false);
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.show();
				new Thread(){//�����̴߳���
					public void run() { 
						try {
							JSONObject object=new JSONObject();
							object.put("UserName", account);
							object.put("Password", passowrd);
							String str=object.toString();
							Log.i("Log","�û�ʹ�õ�¼������Ϊ:"+str);
							GetLoginBean glb=WebLogin.getLoginData(str); 
							if(glb!=null){
								GetLoginBean tempGLB=new GetLoginBean();
								tempGLB.setUserExist(UserGlobal.UserExist=glb.isUserExist());//��ȫ�ֱ�ʶ���ó�Ϊ��¼�ɹ���״̬
								tempGLB.setUserID(UserGlobal.UserID=glb.getUserID()); //����ȫ�ֱ�ʶ�û���ID��   
								//����������¼�û���ID�Ų�ѯһ�θ��û�����Ϣ
								JSONObject object1=new JSONObject();
								object1.put("UserID", glb.getUserID()); 
								List<GetUserInfoBean> users=WebGetAUser.getGetAUserData(object1.toString());
								if(users!=null){
									tempGLB.setUserType(users.get(0).getUsertype());//���õ�ǰ��¼�û�������
									tempGLB.setNickName(UserGlobal.NickName=users.get(0).getNickname());//����ȫ�ֱ�ʶ�û���NickName
									tempGLB.setSex(UserGlobal.sex=users.get(0).getSex());//����ȫ�ֱ�ʶ�û���sex
									tempGLB.setAge(UserGlobal.Age=users.get(0).getAge());//����ȫ�ֱ�ʶ�û���Age
									tempGLB.setName(UserGlobal.Name=users.get(0).getName());//����ȫ�ֱ�ʶ�û���Name
									tempGLB.setUserName(UserGlobal.UserName=users.get(0).getUsername());//����ȫ�ֱ�ʶ�û���UserName
									tempGLB.setPicture(UserGlobal.Picture=users.get(0).getPicture());//����ȫ�ֱ�ʶ�û���Picture
									tempGLB.toString();//��ӡ�û�������¼֮�����Ϣ��LogCat���� 
									
									/** ����ǳ����û�*/
									if(tempGLB.getUserType().equalsIgnoreCase("�����û�")){
										Intent venues_intent=new Intent(Login.this,VenuesAdmin.class);
										mHandler.sendEmptyMessage(0x11);//���͵�¼�ɹ� 
										startActivity(venues_intent);//��ת���ɹ���¼ҳ��(�����û�)
									}else{/**�������ͨ�û�*/
										Intent log_intent=new Intent(Login.this,MainActivity.class); 
										mHandler.sendEmptyMessage(0x11);//���͵�¼�ɹ� 
										startActivity(log_intent);//��ת���ɹ���¼ҳ��(��ͨ�û�)
									} 
									overridePendingTransition(R.anim.in_activity, R.anim.out_activity);//����Activity�������˳�ʱ�Ķ���Ч��		
									finish();
								}else{
									Log.i("�û��ڵ�һ��������¼�ɹ�֮��ʹ��ID��ѯ��һ���û��ĸ�����Ϣ,���:","���ݲ�ѯʧ����!");
								} 
							}else{
								mHandler.sendEmptyMessage(0x13);//���͵�¼ʧ��
							} 
						} catch (JSONException e) { 
							e.printStackTrace();
						}finally{
							if(mProgressDialog!=null){ 
								mProgressDialog.dismiss(); 
							}
						}
					};
				}.start(); 
			}else{
				mHandler.sendEmptyMessage(0x12); //��������������
			} 
			break;
		case R.id.register_button://ע��
//			Toast.makeText(Login.this, "ע��", 0).show();
			intent=new Intent(Login.this,RegisterNewUser.class);
			startActivity(intent);//��ת�����û�ע��ҳ��
			overridePendingTransition(R.anim.in_activity, R.anim.out_activity);//����Activity�������˳�ʱ�Ķ���Ч��
			break;
		case R.id.forget_password://��������
			Toast.makeText(Login.this, "��������", 0).show();
			break;
		} 
	} 
	
	
	@Override
	protected void onDestroy() {
		input_username.setText("");
		input_password.setText("");
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
		input_username.setText("");
		input_password.setText("");
		super.onStop();
	}
	
}
