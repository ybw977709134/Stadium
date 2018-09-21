package com.wqp.stadiumapp;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wqp.webservice.entity.GetUserMakeBean;

/** 预约券 页面*/
public class OrderTicket extends Activity implements OnClickListener{ 
	private static String TAG="OrderTicket";
	private ImageView order_ticket_back_arrow;   
	private TextView ticket_stadium_names,ticket_sport_type,ticket_moneys,
	ticket_actual_names,ticket_order_times,ticket_state,ticket_auth_code;
	private Button ticket_unsubscribe;
	
	private GetUserMakeBean mGetUserMakeBean;//存放从上一个页面发送过来的用户预约的订单数据
 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_ticket);  
		Intent intent=getIntent();
		mGetUserMakeBean=(GetUserMakeBean) intent.getSerializableExtra("GetUserMakeBean");
		
		order_ticket_back_arrow=(ImageView) findViewById(R.id.order_ticket_back_arrow); 
		ticket_stadium_names=(TextView)findViewById(R.id.ticket_stadium_names);
		ticket_sport_type=(TextView)findViewById(R.id.ticket_sport_type);
		ticket_moneys=(TextView)findViewById(R.id.ticket_moneys);
		ticket_actual_names=(TextView)findViewById(R.id.ticket_actual_names);
		ticket_order_times=(TextView)findViewById(R.id.ticket_order_times);
		ticket_state=(TextView)findViewById(R.id.ticket_state);
		ticket_auth_code=(TextView)findViewById(R.id.ticket_auth_code);
		ticket_unsubscribe=(Button)findViewById(R.id.ticket_unsubscribe);
		
		order_ticket_back_arrow.setOnClickListener(this);
		ticket_unsubscribe.setOnClickListener(this);
		
		if(mGetUserMakeBean!=null){
			fillData();//填充数据
		} 
	}  
	   

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_ticket_back_arrow://点击了返回按钮
			finish();
			break; 
		case R.id.ticket_unsubscribe://点击了退订按钮
			//判断当前预约券的状态:未使用、已预订、已使用等
			String state=ticket_state.getText().toString().trim();
			if(TextUtils.isEmpty(state)){return;}
			if(isTicketState(state)){//如果是不能退订
				Toast.makeText(OrderTicket.this, "您的订单预约成功,无法再退订", 0).show();
			}else{
				//跳转到退订页面,进行订单退订操作
				if(mGetUserMakeBean==null) return;
				Intent _intent=new Intent(OrderTicket.this,RetreatOrder.class);
				_intent.putExtra("MakeID", mGetUserMakeBean.getMakeInfoID());
				startActivity(_intent);
				finish();
			}
			break;
		} 
	}  
	
	private String[] stateSet={"已使用","已预约"};
	/** 判断预约券是否可以退订*/
	private boolean isTicketState(String state){
		for (String str : stateSet) {
			if(state.contains(str)){
				return true;
			} 
		}
		return false;
	}
	
	/**对每个字段进行填充数据操作*/
	private void fillData(){
		ticket_stadium_names.setText(mGetUserMakeBean.getVenuesName());//场馆名称
		ticket_sport_type.setText(mGetUserMakeBean.getProjectName());//运动类型
		ticket_moneys.setText("0元");//金额 ,此参数还未确定下来
		ticket_actual_names.setText(mGetUserMakeBean.getBankUserName());//真实姓名
		ticket_order_times.setText(mGetUserMakeBean.getMakeTime());//预约时间
		ticket_state.setText(mGetUserMakeBean.getState());//订单状态
		ticket_auth_code.setText(mGetUserMakeBean.getMakeCode());//验证码 
	}
	
}
 