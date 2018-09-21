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
	private ImageView login_back_arrow;//返回图标按钮
	private EditText input_username,input_password;
	private CheckBox remember_password,auto_login;
	private Button register_button,login_button;
	private TextView forget_password;
	private ProgressDialog mProgressDialog;
	
	private SharedPreferences mSharedPrefs;
	private SharedPreferences.Editor mEditor;
	
	//定义消息管理器
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://登录成功
				Log.i("登录状态:","登录成功");
				break; 
			case 0x12://您输入有误
				Toast.makeText(Login.this, "您输入有误", 0).show(); 
				break;
			case 0x13://登录失败 
				Toast.makeText(Login.this, "登录失败", 1).show();
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
		
		//获取到SharedPrederences实例
		mSharedPrefs=getSharedPreferences("login_data", Context.MODE_PRIVATE);
		mEditor=mSharedPrefs.edit();
		
		String username=mSharedPrefs.getString("username", null);
		String password=mSharedPrefs.getString("password", null);
		boolean autoRes=mSharedPrefs.getBoolean("auto_log", false);
		
		if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
			input_username.setText(username);//填充用户名
			input_password.setText(password);//填充密码
		} 
		if(autoRes && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
			onClick(login_button);//调用点击事件的登录按钮操作
		}
	}
	 
	
	/** 对页面上的所有组件进行初始化，并绑定事件*/
	private void initView(){
		login_back_arrow=(ImageView) findViewById(R.id.login_back_arrow);
		input_username=(EditText) findViewById(R.id.input_username);
		input_password=(EditText) findViewById(R.id.input_password);
		remember_password=(CheckBox) findViewById(R.id.remember_password);
		auto_login=(CheckBox) findViewById(R.id.auto_login);
		register_button=(Button) findViewById(R.id.register_button);
		login_button=(Button) findViewById(R.id.login_button);
		forget_password=(TextView) findViewById(R.id.forget_password); 
		forget_password.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//给TextView添加下划线
		
		login_back_arrow.setOnClickListener(this);//返回图标
		login_button.setOnClickListener(this);//登录按钮
		register_button.setOnClickListener(this);//注册按钮
		forget_password.setOnClickListener(this);//忘记密码
	}

	@Override
	public void onClick(View v) {  
		Intent intent=null;
		switch(v.getId()){
		case R.id.login_back_arrow://返回 
			finish();
			break;
		case R.id.login_button://登录
			if(!ToolsNavigate.isNetworkAvailable(getApplicationContext())){
				Toast.makeText(Login.this, "亲,网络没打开哦", 1).show();return;
			}
			final String account=input_username.getText().toString().trim();
			final String passowrd=input_password.getText().toString().trim();
			boolean remember_psw=remember_password.isChecked();
			boolean auto_log=auto_login.isChecked(); 
			
			if((account!=null && !TextUtils.isEmpty(account)) && (passowrd!=null && !TextUtils.isEmpty(passowrd))){ 
				//如里需要记录用户名和密码
				if(remember_psw){
					mEditor.clear();
					mEditor.putString("username", account);
					mEditor.putString("password", passowrd);
					mEditor.commit();
				}
				
				//如果需要记录自动登录
				if(auto_log){
					mEditor.putBoolean("auto_log", true);
					mEditor.commit();
				}
				
				mProgressDialog=new ProgressDialog(Login.this);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setMessage("登录中...");
				mProgressDialog.setIndeterminate(true); 
				mProgressDialog.setCancelable(false);
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.show();
				new Thread(){//开启线程处理
					public void run() { 
						try {
							JSONObject object=new JSONObject();
							object.put("UserName", account);
							object.put("Password", passowrd);
							String str=object.toString();
							Log.i("Log","用户使用登录的数据为:"+str);
							GetLoginBean glb=WebLogin.getLoginData(str); 
							if(glb!=null){
								GetLoginBean tempGLB=new GetLoginBean();
								tempGLB.setUserExist(UserGlobal.UserExist=glb.isUserExist());//把全局标识设置成为登录成功的状态
								tempGLB.setUserID(UserGlobal.UserID=glb.getUserID()); //更新全局标识用户的ID号   
								//根据正常登录用户的ID号查询一次该用户的信息
								JSONObject object1=new JSONObject();
								object1.put("UserID", glb.getUserID()); 
								List<GetUserInfoBean> users=WebGetAUser.getGetAUserData(object1.toString());
								if(users!=null){
									tempGLB.setUserType(users.get(0).getUsertype());//设置当前登录用户的类型
									tempGLB.setNickName(UserGlobal.NickName=users.get(0).getNickname());//更新全局标识用户的NickName
									tempGLB.setSex(UserGlobal.sex=users.get(0).getSex());//更新全局标识用户的sex
									tempGLB.setAge(UserGlobal.Age=users.get(0).getAge());//更新全局标识用户的Age
									tempGLB.setName(UserGlobal.Name=users.get(0).getName());//更新全局标识用户的Name
									tempGLB.setUserName(UserGlobal.UserName=users.get(0).getUsername());//更新全局标识用户的UserName
									tempGLB.setPicture(UserGlobal.Picture=users.get(0).getPicture());//更新全局标识用户的Picture
									tempGLB.toString();//打印用户正常登录之后的信息到LogCat上面 
									
									/** 如果是场馆用户*/
									if(tempGLB.getUserType().equalsIgnoreCase("场馆用户")){
										Intent venues_intent=new Intent(Login.this,VenuesAdmin.class);
										mHandler.sendEmptyMessage(0x11);//发送登录成功 
										startActivity(venues_intent);//跳转到成功登录页面(场馆用户)
									}else{/**如果是普通用户*/
										Intent log_intent=new Intent(Login.this,MainActivity.class); 
										mHandler.sendEmptyMessage(0x11);//发送登录成功 
										startActivity(log_intent);//跳转到成功登录页面(普通用户)
									} 
									overridePendingTransition(R.anim.in_activity, R.anim.out_activity);//设置Activity进入与退出时的动画效果		
									finish();
								}else{
									Log.i("用户在第一次正常登录成功之后使用ID查询了一次用户的个人信息,结果:","数据查询失败了!");
								} 
							}else{
								mHandler.sendEmptyMessage(0x13);//发送登录失败
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
				mHandler.sendEmptyMessage(0x12); //发送您输入有误
			} 
			break;
		case R.id.register_button://注册
//			Toast.makeText(Login.this, "注册", 0).show();
			intent=new Intent(Login.this,RegisterNewUser.class);
			startActivity(intent);//跳转到新用户注册页面
			overridePendingTransition(R.anim.in_activity, R.anim.out_activity);//设置Activity进入与退出时的动画效果
			break;
		case R.id.forget_password://忘记密码
			Toast.makeText(Login.this, "忘记密码", 0).show();
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
