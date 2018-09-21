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

/** 退订 页面*/
public class RetreatOrder extends Activity implements OnClickListener{ 
	private static String TAG="RetreatOrder";
	private ImageView retreat_back_arrow;   
	private EditText retreat_open_bank,retreat_account,retreat_names;
	private Button verify;
	
	private int MakeID;//存放从上一个页面发送过来的用户预约的订单ID
	
	/** 定义消息管理器*/
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			retreat_open_bank.setText("");//请空银行名称
			retreat_account.setText("");//清空帐号
			retreat_names.setText("");//清空户名
			switch (msg.what) {
			case 0x11://退订成功
				Toast.makeText(RetreatOrder.this, "退订提交成功", 0).show(); 
				Intent _intent=new Intent(RetreatOrder.this,MyOrder.class);//跳转到我的预约界面
				startActivity(_intent); 
				break;

			case 0x12://退订失败
				Toast.makeText(RetreatOrder.this, "退订提交失败", 0).show(); 
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
		case R.id.retreat_back_arrow://点击了返回按钮
			finish();
			break; 
		case R.id.verify://点击了确定按钮   
			if(MakeID==-1) return;
			final String bank=retreat_open_bank.getText().toString().trim();
			final String account=retreat_account.getText().toString().trim();
			final String names=retreat_names.getText().toString().trim();
			if(TextUtils.isEmpty(bank)){
				Toast.makeText(RetreatOrder.this, "开户银行不能为空", 0).show();
				return;
			}
			if(TextUtils.isEmpty(account)){
				Toast.makeText(RetreatOrder.this, "帐号不能为空", 0).show();
				return;
			}
			if(TextUtils.isEmpty(names)){
				Toast.makeText(RetreatOrder.this, "户名不能为空", 0).show();
				return;
			} 
			
			//进行退订操作
			new Thread(){
				public void run() { 
					try {
						JSONObject object=new JSONObject();
						object.put("MakeID", MakeID);
						object.put("BankCode", account);
						object.put("OpenBank", bank);
						object.put("BankUserName", names);
						boolean result=WebRemoveMake.getRemoveMakeData(object.toString());
						if(result){//退订成功
							mHandler.sendEmptyMessage(0x11);
						}else{//退订失败
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
 