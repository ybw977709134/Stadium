package com.wqp.stadiumapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.wqp.stadiumapp.utils.ToolsNavigate; 
import com.wqp.webservice.WebAddUser; 

public class RegisterNewUser extends Activity implements OnClickListener {
	@SuppressWarnings("unused")
	private static final String TAG = "RegisterNewUser";
	private ImageView register_new_user_back_arrow;
	private Button register_new_user_operation;
	private RadioGroup register_new_user_sex;
	private EditText register_new_user_account,register_new_user_password,register_new_user_name,
					 register_new_user_nickname,register_new_user_phone,register_new_user_age;
	
	private static String sex=null;//�����û�ѡ����Ա�ֵ(�л���Ů)
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://ע��ɹ�
				Toast.makeText(RegisterNewUser.this, "ע��ɹ�", 1).show();
				finish();//ע��ɹ���͹ر�ע��ҳ��
				break;
			case 0x22://ע��ʧ��
				Toast.makeText(RegisterNewUser.this, "ע��ʧ��", 1).show();
			case 0x33://ע��������д����
				Toast.makeText(RegisterNewUser.this, "������д����,��ȷ��", 1).show();
				break;
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_new_user);
		initView(); 
	}
	
	/** ��ҳ���������г�ʼ��,��ע�ᰴť���¼�*/
	private void initView(){
		register_new_user_back_arrow=(ImageView) findViewById(R.id.register_new_user_back_arrow);
		register_new_user_account=(EditText) findViewById(R.id.register_new_user_account);
		register_new_user_password=(EditText) findViewById(R.id.register_new_user_password);
		register_new_user_name=(EditText) findViewById(R.id.register_new_user_name);
		register_new_user_nickname=(EditText) findViewById(R.id.register_new_user_nickname);
		register_new_user_phone=(EditText) findViewById(R.id.register_new_user_phone);
		register_new_user_operation=(Button) findViewById(R.id.register_new_user_operation);
		register_new_user_age=(EditText) findViewById(R.id.register_new_user_age);
		register_new_user_sex=(RadioGroup) findViewById(R.id.register_new_user_sex);
		
		register_new_user_operation.setOnClickListener(this);
		register_new_user_back_arrow.setOnClickListener(this);
		register_new_user_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.register_new_user_sex_female:
					sex="Ů";
					break;
				case R.id.register_new_user_sex_male:
					sex="��";
					break;
				default:
					break;
				} 
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_new_user_operation://�����ע�ᰴť
			if(!ToolsNavigate.isNetworkAvailable(getApplicationContext())){
				Toast.makeText(RegisterNewUser.this, "��,����û��Ŷ", 1).show();return;
			}
			final String username=register_new_user_account.getText().toString().trim();
			final String password=register_new_user_password.getText().toString().trim();
			final String name=register_new_user_name.getText().toString().trim();
			final String nickname=register_new_user_nickname.getText().toString().trim();
			final String phone=register_new_user_phone.getText().toString().trim();
			final String age=register_new_user_age.getText().toString().trim();
			
			Log.i("�û�ע�������������Ϊ:",username+","+password+","+name+","+nickname+","+phone+","+age+","+sex);
			
			if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(nickname) && !TextUtils.isEmpty(phone)){			
				new Thread(){//�����̴߳���
					boolean token=false;//���ڱ�ʶ���û��ڷ�������ע���Ƿ�ɹ�
					public void run(){
						JSONObject object=new JSONObject();
						try {
							object.put("UserName", username);
							object.put("password", password);
							object.put("Name", name);
							object.put("Nickname", nickname);
							object.put("Phone", phone);
							String result=object.toString();
							Log.i("���û���д��ע��������:",result); 
							token=WebAddUser.getAddUserData(result);//���û���д��������json��ʽ���͵�����˽���ע�����
							if(token){
								handler.sendEmptyMessage(0x11);//����ע��ɹ�
							}else{
								handler.sendEmptyMessage(0x22);//����ע��ʧ��
							} 
						} catch (JSONException e) { 
							e.printStackTrace();
						}
					}
				}.start();
			}else{
				handler.sendEmptyMessage(0x33);//����������д����
			}
			break; 
		case R.id.register_new_user_back_arrow://���ذ�ť
			finish();
			break;
		default:
			break;
		} 
	}
	
	
	@Override
	protected void onDestroy() {
		register_new_user_account.setText("");
		register_new_user_password.setText("");
		register_new_user_name.setText("");
		register_new_user_nickname.setText("");
		register_new_user_phone.setText("");
		register_new_user_age.setText(""); 
		super.onDestroy();
	}
	
}
