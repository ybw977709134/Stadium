package com.wqp.stadiumapp;
 
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView; 
import android.widget.Toast;

import com.wqp.webservice.WebRemoveMake; 

/** �˶� ҳ��*/
public class RetreatOrder extends Activity implements OnClickListener{ 
	private static String TAG="RetreatOrder";
	private ImageView retreat_back_arrow;   
	private EditText retreat_open_bank,retreat_account,retreat_names;
	private Button verify;
	
	private int MakeID;//��Ŵ���һ��ҳ�淢�͹������û�ԤԼ�Ķ���ID
	
	/** ������Ϣ������*/
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			retreat_open_bank.setText("");//�����������
			retreat_account.setText("");//����ʺ�
			retreat_names.setText("");//��ջ���
			switch (msg.what) {
			case 0x11://�˶��ɹ�
				Toast.makeText(RetreatOrder.this, "�˶��ύ�ɹ�", 0).show(); 
				Intent _intent=new Intent(RetreatOrder.this,MyOrder.class);//��ת���ҵ�ԤԼ����
				startActivity(_intent); 
				break;

			case 0x12://�˶�ʧ��
				Toast.makeText(RetreatOrder.this, "�˶��ύʧ��", 0).show(); 
				break;
			}
		};
	};
 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.retreat_order);  
		Intent intent=getIntent(); 
		MakeID=intent.getIntExtra("MakeID", -1);
		
		retreat_back_arrow=(ImageView) findViewById(R.id.retreat_back_arrow); 
		retreat_open_bank=(EditText)findViewById(R.id.retreat_open_bank);
		retreat_account=(EditText)findViewById(R.id.retreat_account);
		retreat_names=(EditText)findViewById(R.id.retreat_names); 
		verify=(Button)findViewById(R.id.verify);
		
		retreat_back_arrow.setOnClickListener(this);
		verify.setOnClickListener(this);
		
		  
	}  
	   

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retreat_back_arrow://����˷��ذ�ť
			finish();
			break; 
		case R.id.verify://�����ȷ����ť   
			if(MakeID==-1) return;
			final String bank=retreat_open_bank.getText().toString().trim();
			final String account=retreat_account.getText().toString().trim();
			final String names=retreat_names.getText().toString().trim();
			if(TextUtils.isEmpty(bank)){
				Toast.makeText(RetreatOrder.this, "�������в���Ϊ��", 0).show();
				return;
			}
			if(TextUtils.isEmpty(account)){
				Toast.makeText(RetreatOrder.this, "�ʺŲ���Ϊ��", 0).show();
				return;
			}
			if(TextUtils.isEmpty(names)){
				Toast.makeText(RetreatOrder.this, "��������Ϊ��", 0).show();
				return;
			} 
			
			//�����˶�����
			new Thread(){
				public void run() { 
					try {
						JSONObject object=new JSONObject();
						object.put("MakeID", MakeID);
						object.put("BankCode", account);
						object.put("OpenBank", bank);
						object.put("BankUserName", names);
						boolean result=WebRemoveMake.getRemoveMakeData(object.toString());
						if(result){//�˶��ɹ�
							mHandler.sendEmptyMessage(0x11);
						}else{//�˶�ʧ��
							mHandler.sendEmptyMessage(0x12);
						} 
					} catch (JSONException e) { 
						e.printStackTrace();
					} 
				};
			}.start();
			break;
		} 
	}   
	 
	
}
 