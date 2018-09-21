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

/** �����û�����Ϣ*/
public class UserUpdate extends Activity implements OnClickListener {
	private ImageView user_update_back_arrow;
	private Button user_update_new_operation;
	private EditText user_update_new_mima,user_update_new_nickname,user_update_new_age;
	private RadioGroup user_update_new_sex_group;
	private String sex=null;//������ʱ�洢�û����µ��Ա��ֶεĲ���
	
	/** ������Ϣ������*/
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://���³ɹ�
				Toast.makeText(UserUpdate.this, "���³ɹ�", 0).show();
				finish();
				break;
			case 0x22://���ϸ���ʧ��
				Toast.makeText(UserUpdate.this, "���ϸ���ʧ��", 0).show();
				break;
			case 0x33://�������ʧ��
				Toast.makeText(UserUpdate.this, "�������ʧ��", 0).show();
				break;
			case 0x44://����ʧ��
				Toast.makeText(UserUpdate.this, "����ʧ��", 0).show();
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
	
	/** ��ҳ���ϵ����������ʼ���������¼�*/
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
					case R.id.user_update_new_sex_female: sex="Ů"; break;
					case R.id.user_update_new_sex_male: sex="��"; break;
				}
			}
		}); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_update_new_operation://����˸����û����ϵİ�ť
			if(!ToolsNavigate.isNetworkAvailable(getApplicationContext())){
				Toast.makeText(UserUpdate.this, "��,����û��Ŷ", 1).show();return;
			}
			final String password=user_update_new_mima.getText().toString().trim();
			final String nickname=user_update_new_nickname.getText().toString().trim();
			final String age=user_update_new_age.getText().toString().trim();
			if((password!=null && !TextUtils.isEmpty(password)) && (nickname!=null && !TextUtils.isEmpty(nickname)) && (age!=null && !TextUtils.isEmpty(age)) && (sex!=null && !TextUtils.isEmpty(sex))){
				new Thread(){
					public void run() { 
						try {
							//�����û�����
							JSONObject object=new JSONObject();
							object.put("UserID", UserGlobal.UserID);
							object.put("NickName", nickname);
							object.put("Age", age);
							object.put("Sex", sex); 
							boolean token=WebUserUpdate.getUserUpdateData(object.toString());
							
							//�����û�����
							JSONObject pswobject=new JSONObject();
							pswobject.put("UserID", UserGlobal.UserID);//������¼�û���ID
							pswobject.put("PassWord", password);//������
							boolean pswstate=WebPWDUpdate.getPWDUpdateData(pswobject.toString());
							
							//�жϸ��µĽ�� 
							if(token && pswstate){
								handler.sendEmptyMessage(0x11);//���͸��³ɹ�
							}else if(!token){
								handler.sendEmptyMessage(0x22);//�������ϸ���ʧ��
							}else if(!pswstate){
								handler.sendEmptyMessage(0x33);//�����������ʧ��
							}else{
								handler.sendEmptyMessage(0x44);//���Ͳ���ʧ��
							}
						} catch (JSONException e) {  e.printStackTrace(); } 
					};
				}.start(); 
			}else{
				Toast.makeText(UserUpdate.this, "������д������", 0).show();
			} 
			break;  
		case R.id.user_update_back_arrow://��ҳ������ķ��ذ�ť
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
