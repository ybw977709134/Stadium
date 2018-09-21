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

/** ԤԼȯ ҳ��*/
public class OrderTicket extends Activity implements OnClickListener{ 
	private static String TAG="OrderTicket";
	private ImageView order_ticket_back_arrow;   
	private TextView ticket_stadium_names,ticket_sport_type,ticket_moneys,
	ticket_actual_names,ticket_order_times,ticket_state,ticket_auth_code;
	private Button ticket_unsubscribe;
	
	private GetUserMakeBean mGetUserMakeBean;//��Ŵ���һ��ҳ�淢�͹������û�ԤԼ�Ķ�������
 
	
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
			fillData();//�������
		} 
	}  
	   

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_ticket_back_arrow://����˷��ذ�ť
			finish();
			break; 
		case R.id.ticket_unsubscribe://������˶���ť
			//�жϵ�ǰԤԼȯ��״̬:δʹ�á���Ԥ������ʹ�õ�
			String state=ticket_state.getText().toString().trim();
			if(TextUtils.isEmpty(state)){return;}
			if(isTicketState(state)){//����ǲ����˶�
				Toast.makeText(OrderTicket.this, "���Ķ���ԤԼ�ɹ�,�޷����˶�", 0).show();
			}else{
				//��ת���˶�ҳ��,���ж����˶�����
				if(mGetUserMakeBean==null) return;
				Intent _intent=new Intent(OrderTicket.this,RetreatOrder.class);
				_intent.putExtra("MakeID", mGetUserMakeBean.getMakeInfoID());
				startActivity(_intent);
				finish();
			}
			break;
		} 
	}  
	
	private String[] stateSet={"��ʹ��","��ԤԼ"};
	/** �ж�ԤԼȯ�Ƿ�����˶�*/
	private boolean isTicketState(String state){
		for (String str : stateSet) {
			if(state.contains(str)){
				return true;
			} 
		}
		return false;
	}
	
	/**��ÿ���ֶν���������ݲ���*/
	private void fillData(){
		ticket_stadium_names.setText(mGetUserMakeBean.getVenuesName());//��������
		ticket_sport_type.setText(mGetUserMakeBean.getProjectName());//�˶�����
		ticket_moneys.setText("0Ԫ");//��� ,�˲�����δȷ������
		ticket_actual_names.setText(mGetUserMakeBean.getBankUserName());//��ʵ����
		ticket_order_times.setText(mGetUserMakeBean.getMakeTime());//ԤԼʱ��
		ticket_state.setText(mGetUserMakeBean.getState());//����״̬
		ticket_auth_code.setText(mGetUserMakeBean.getMakeCode());//��֤�� 
	}
	
}
 