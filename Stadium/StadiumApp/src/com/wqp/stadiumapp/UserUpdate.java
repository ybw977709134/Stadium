package com.wqp.stadiumapp;

import org.json.JSONException;
import org.json.JSONObject;

import com.wqp.stadiumapp.utils.ToolsNavigate;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.webservice.WebPWDUpdate;
import com.wqp.webservice.WebUserUpdate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/** 更新用户的信息*/
public class UserUpdate extends Activity implements OnClickListener {
	private ImageView user_update_back_arrow;
	private Button user_update_new_operation;
	private EditText user_update_new_mima,user_update_new_nickname,user_update_new_age;
	private RadioGroup user_update_new_sex_group;
	private String sex=null;//用于临时存储用户更新的性别字段的参数
	
	/** 定义消息管理器*/
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://更新成功
				Toast.makeText(UserUpdate.this, "更新成功", 0).show();
				finish();
				break;
			case 0x22://资料更新失败
				Toast.makeText(UserUpdate.this, "资料更新失败", 0).show();
				break;
			case 0x33://密码更新失败
				Toast.makeText(UserUpdate.this, "密码更新失败", 0).show();
				break;
			case 0x44://操作失败
				Toast.makeText(UserUpdate.this, "操作失败", 0).show();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_update);
		initView();
		
	}
	
	/** 对页面上的所有组件初始化，并绑定事件*/
	public void initView(){
		user_update_back_arrow=(ImageView) findViewById(R.id.user_update_back_arrow);
		user_update_new_operation=(Button) findViewById(R.id.user_update_new_operation);
		user_update_new_mima=(EditText) findViewById(R.id.user_update_new_mima);
		user_update_new_nickname=(EditText) findViewById(R.id.user_update_new_nickname);
		user_update_new_age=(EditText) findViewById(R.id.user_update_new_age);
		user_update_new_sex_group=(RadioGroup) findViewById(R.id.user_update_new_sex_group);
		
		user_update_back_arrow.setOnClickListener(this);
		user_update_new_operation.setOnClickListener(this);
		user_update_new_sex_group.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) { 
				switch (checkedId) {
					case R.id.user_update_new_sex_female: sex="女"; break;
					case R.id.user_update_new_sex_male: sex="男"; break;
				}
			}
		}); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_update_new_operation://点击了更新用户资料的按钮
			if(!ToolsNavigate.isNetworkAvailable(getApplicationContext())){
				Toast.makeText(UserUpdate.this, "亲,网络没打开哦", 1).show();return;
			}
			final String password=user_update_new_mima.getText().toString().trim();
			final String nickname=user_update_new_nickname.getText().toString().trim();
			final String age=user_update_new_age.getText().toString().trim();
			if((password!=null && !TextUtils.isEmpty(password)) && (nickname!=null && !TextUtils.isEmpty(nickname)) && (age!=null && !TextUtils.isEmpty(age)) && (sex!=null && !TextUtils.isEmpty(sex))){
				new Thread(){
					public void run() { 
						try {
							//更新用户资料
							JSONObject object=new JSONObject();
							object.put("UserID", UserGlobal.UserID);
							object.put("NickName", nickname);
							object.put("Age", age);
							object.put("Sex", sex); 
							boolean token=WebUserUpdate.getUserUpdateData(object.toString());
							
							//更新用户密码
							JSONObject pswobject=new JSONObject();
							pswobject.put("UserID", UserGlobal.UserID);//正常登录用户的ID
							pswobject.put("PassWord", password);//新密码
							boolean pswstate=WebPWDUpdate.getPWDUpdateData(pswobject.toString());
							
							//判断更新的结果 
							if(token && pswstate){
								handler.sendEmptyMessage(0x11);//发送更新成功
							}else if(!token){
								handler.sendEmptyMessage(0x22);//发送资料更新失败
							}else if(!pswstate){
								handler.sendEmptyMessage(0x33);//发送密码更新失败
							}else{
								handler.sendEmptyMessage(0x44);//发送操作失败
							}
						} catch (JSONException e) {  e.printStackTrace(); } 
					};
				}.start(); 
			}else{
				Toast.makeText(UserUpdate.this, "请检查填写的资料", 0).show();
			} 
			break;  
		case R.id.user_update_back_arrow://该页面上面的返回按钮
			finish();
			break;
		} 
	}
	
	@Override
	public void onDestroy() {
		user_update_new_mima.setText("");
		user_update_new_nickname.setText("");
		user_update_new_age.setText(""); 
		super.onDestroy();
	}
	
}
