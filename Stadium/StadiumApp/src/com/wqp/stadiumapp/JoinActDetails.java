package com.wqp.stadiumapp;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.WebGetUserOnAct;
import com.wqp.webservice.WebJoinAct;
import com.wqp.webservice.entity.GetActInfoBean;
import com.wqp.webservice.entity.JoinActResultBean;

public class JoinActDetails extends Activity implements OnClickListener {
	private String TAG="JoinActDetails";
	private ImageView joinact_detail_back_arrow,joinact_detail_pubimg;
	private TextView joinact_detail_venuesname,joinact_detail_sporttype,joinact_detail_takecount,
	joinact_detail_starttime,joinact_detail_applyendtime,joinact_detail_venuesaddress;
	private Button joinact_detail_attend;
	
	private GetActInfoBean mGAIB;
	private ProgressDialog mProgressDialog;//进度对话框
	private JoinActResultBean jrb;//用户添加活动成功之后返回的数据信息集
	
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x44:
				jrb=(JoinActResultBean) msg.obj;
				if(jrb!=null){
					if(jrb.isUserExist() && jrb.getNoName().equals("0") && jrb.getOnName().equals("0")){
						joinact_detail_attend.setText("已报名");
						Toast.makeText(JoinActDetails.this, "报名应战成功!", 0).show(); 
					}else if(!jrb.getNoName().equals("0")){
						Toast.makeText(JoinActDetails.this, "用户名不存在,报名应战失败!", 0).show();
					}else if(!jrb.getOnName().equals("0")){
						Toast.makeText(JoinActDetails.this, "加入申请已发送 等待审核中!", 0).show();
						finish();
					} 	
				} 
				break; 
			case 0x55://接收加入应战失败
				Toast.makeText(JoinActDetails.this, "报名应战失败了!", 0).show();
				break;
			case 0x99://表示用户已经参加该条目上面的活动了，不能再进行参加
				Toast.makeText(JoinActDetails.this, "您已经参加本次活动了", 0).show();
				break;
			} 
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_act_detail);
		mGAIB=(GetActInfoBean) getIntent().getSerializableExtra("GetActInfoBean"); 
		initView();
		
		if(mGAIB!=null){
			//进行填充数据操作
			initPageData();
		} 
	}
	
	/** 对页面所有组件初始化并绑定事件*/
	public void initView(){
		joinact_detail_back_arrow=(ImageView) findViewById(R.id.joinact_detail_back_arrow);//返回图标按钮
		joinact_detail_pubimg=(ImageView) findViewById(R.id.joinact_detail_pubimg);//介绍图片
		joinact_detail_venuesname=(TextView) findViewById(R.id.joinact_detail_venuesname);//场馆名称
		joinact_detail_sporttype=(TextView) findViewById(R.id.joinact_detail_sporttype);//运动类型
		joinact_detail_takecount=(TextView) findViewById(R.id.joinact_detail_takecount);//活动人数
		joinact_detail_starttime=(TextView) findViewById(R.id.joinact_detail_starttime);//活动时间
		joinact_detail_applyendtime=(TextView) findViewById(R.id.joinact_detail_applyendtime);//截止报名时间
		joinact_detail_venuesaddress=(TextView) findViewById(R.id.joinact_detail_venuesaddress);//场馆地址
		joinact_detail_attend=(Button) findViewById(R.id.joinact_detail_attend);//报名按钮
		
		joinact_detail_back_arrow.setOnClickListener(this);
		joinact_detail_attend.setOnClickListener(this); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.joinact_detail_back_arrow://点击了返回图标按钮
			finish();
			break;

		case R.id.joinact_detail_attend://点击了报名按钮
			if(mGAIB!=null && !mGAIB.isImgstate()){ //未报名时
				joinAction();
			}else{ //已报名
//				Toast.makeText(JoinActDetails.this, "已经报名", 0).show(); 
			}
			break;
		} 
	}
	
	/** 对页面进行填充数据操作*/
	public void initPageData(){
		joinact_detail_venuesname.setText(mGAIB.getVenuesname());//场馆名称
		joinact_detail_sporttype.setText("活动类型:"+mGAIB.getSporttypestring());//活动类型
		joinact_detail_takecount.setText("活动人数:"+mGAIB.getTakecount());//活动人数
		joinact_detail_starttime.setText("活动时间:"+mGAIB.getStarttime());//活动时间
		joinact_detail_applyendtime.setText("报名截止时间:"+mGAIB.getApplyendtime());//报名截止时间
		joinact_detail_venuesaddress.setText("活动公告:"+mGAIB.getActnotice());//场馆地址更改为活动公告
		//把场馆活动图片设置到ImageView组件上面
		ImageLoader.getInstance().displayImage( WebServiceUtils.IMAGE_URL+mGAIB.getPubimg(), joinact_detail_pubimg, 
				AppApplication.options, new SimpleImageLoadingListener(), new ImageLoadingProgressListener(){ 					@Override
					public void onProgressUpdate(String arg0, View view, int current, int total) { 
						Log.i(TAG,"场馆活动图片异步加载的数据:total="+total+",current="+current);
					} 
				});
		if(mGAIB.isImgstate()){//如果当前用户已经参加了活动
			joinact_detail_attend.setText("已报名");
		}else{//当前用户还未参加活动
			joinact_detail_attend.setText("报名");
		}
	}
	
	/** 点击了报名按钮加入应战*/
	private void joinAction(){ 
		//发送数据到服务端进行加入应战
		try{
			final JSONObject object=new JSONObject();
			object.put("UserName", UserGlobal.UserName);//参数是正常登录的用户名(邮箱)
			object.put("ActID", mGAIB.getActid().intValue());//参数是活动的ID
			Log.i(TAG,"加入应战传入到服务端的参数为:"+object.toString());  
			mProgressDialog=new ProgressDialog(JoinActDetails.this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("正在加入中...");
			mProgressDialog.setIndeterminate(true); 
			mProgressDialog.setCancelable(false);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.show();
			new Thread(){
				public void run(){ 
					//首先判断用户点击的这个条目用户是否已经参加应战
					List<Integer> ids=WebGetUserOnAct.getGetUserOnActData(UserGlobal.UserID);
					if(ids!=null){//说明已经有参加应战活动,然后跟当前用户点击的条目活动ID进行匹配
						for (Integer integer : ids) {
							if(integer==mGAIB.getActid().intValue()){//如果查询到的活动ID等于当前用户点击的条目ID
								mHandler.sendEmptyMessage(0x99);//发送已经参数该条目上活动的标识
								return;
							}
						}
					}  
					//没有参加过任何活动就进行参加应战活动 
					JoinActResultBean token=WebJoinAct.getJoinActData(object.toString());  
					Log.i(TAG,"当点击报名按钮加入应战时返回的状态集是:"+token);
					if(token!=null){
						Message msg=new Message();
						msg.what=0x44; 
						msg.obj=token;
						mHandler.sendMessage(msg);//发送加入应战成功标识 和操作条目的View
					}else{
						mHandler.sendEmptyMessage(0x55);//发送加入应战失败标识
					} 
				}
			}.start();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mProgressDialog.dismiss();
		} 
	}
	
	
	
}
