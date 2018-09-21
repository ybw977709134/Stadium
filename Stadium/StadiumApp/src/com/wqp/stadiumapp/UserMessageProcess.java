package com.wqp.stadiumapp;
 
import java.text.DecimalFormat;
 
import com.wqp.stadiumapp.utils.UserGlobal; 
import com.wqp.webservice.entity.Get5SecondUBean;

import android.app.Activity;
import android.app.AlertDialog; 
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 用户收到场馆消息回复后,在通知栏里面响应点击，再显示此处理界面  
 */
public class UserMessageProcess extends Activity {
	private TextView users_id,pro_name,venues_ids,pricess,create_times;
	private Get5SecondUBean mGet5SecondUBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_message_process); 
		
		Intent intent=getIntent();
		mGet5SecondUBean=(Get5SecondUBean) intent.getSerializableExtra("Get5SecondUBean");
		if(mGet5SecondUBean==null){
			Toast.makeText(UserMessageProcess.this, "信息为空", 0).show();
			finish();
			return;
		}
		//欢迎进入到用户消息处理界面,这里使用一个对话框把用户点击的消息弹出供用户选择操作
		//AlertDialog.Builder...
		//如果选择确定就走支付页面，如果选择取消就发送取消标识到服务羰端即可
		View v=View.inflate(UserMessageProcess.this, R.layout.user_message_process_view, null);
		users_id=(TextView) v.findViewById(R.id.users_id);
		pro_name=(TextView) v.findViewById(R.id.pro_name);
		venues_ids=(TextView) v.findViewById(R.id.venues_ids);
		pricess=(TextView) v.findViewById(R.id.pricess);
		create_times=(TextView) v.findViewById(R.id.create_times);
		
/*		根据场馆的ID号获取场馆的名称
		new Thread(){
			public void run() {
				try {
					JSONObject obj=new JSONObject();
					obj.put("VenuesID", mGet5SecondUBean.getVenuesIDs());
					WebGetVenues.getGetVenuesData(obj.toString());
					
					
					
				} catch (JSONException e) { 
					e.printStackTrace();
				} 
			};
		}.start();
*/		
		//给组件映射数据
		users_id.setText("订单用户名："+UserGlobal.UserName);
		pro_name.setText("运动的项目："+mGet5SecondUBean.getProName());
		venues_ids.setText("参加的场馆(暂时为场馆的ID)："+mGet5SecondUBean.getVenuesIDs());
		pricess.setText("门票的金额："+new DecimalFormat(".00").format(mGet5SecondUBean.getPrice()));//把float类型数据转换成保留两位小数		
		create_times.setText("订单创建时间："+mGet5SecondUBean.getCreateTime()); 
		
		AlertDialog.Builder builder=new AlertDialog.Builder(UserMessageProcess.this)
		.setTitle("请核实您的订单,确定付款吗?")
		.setView(v)
		.setPositiveButton("付款", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//如果点击了付款操作就跳转到支付页面
				Intent _intent_=new Intent(UserMessageProcess.this,StadiumOrder.class);//从当前页面跳转到支付的页面,但要传3个参数过去
				
				startActivity(_intent_);
				finish();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//如果点击了取消操作就直接退出当前页面
				
				finish();
			}
		});
		builder.create().show();
	}
	
}
