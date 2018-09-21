package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wqp.stadiumapp.adapter.QuerySposrtsVenuesAdapter;
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.WebAddUserVM;
import com.wqp.webservice.WebGetSportsVenues;
import com.wqp.webservice.entity.GetSportsVenuesBean;

/** 用户点击首页上面的"足球"、"篮球"等图标后，会跳转到此页面*/
public class ConditionQueryPage extends Activity implements OnClickListener {
	private static final String TAG = "ConditionQueryPage";
	private ImageView condition_query_back_arrow;
	private TextView condition_sporttype;
	private EditText condition_area,condition_time;
	private Button condition_query,condition_submit;
	private LinearLayout condition_page,condition_listview;
	private ListView show_list;
	
	/** 该集合用于存放根据条件查询到的场馆名称*/
//	private List<String> mQueryData=new ArrayList<String>();
	
	/** 该集合用于存放用户从ListView当中选择的具体哪些条目编号*/
//	private List<Integer> mChoiceIden=new ArrayList<Integer>();
	private int mIdentifier=0;
	private int sporttype;//用户在主界面ViewPager中点击的运动类型编号
	
	private List<GetSportsVenuesBean> WebDataSet=null;//存放从服务端获取到的数据集
	public static List<Integer> venuesIDs=new ArrayList<Integer>();//存放用户选择的场馆ID结果集
	private QuerySposrtsVenuesAdapter adapter;//按条件查询场馆的适配器
	private List<Map<String,Object>> mListViewData;//存入listview每个条目的数据  
	
	
	//定义消息管理器
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0x11://获取到数据成功
				WebDataSet=(List<GetSportsVenuesBean>) msg.obj;
				showQueryData();//通过ListView进行显示数据  
				break;
			case 0x12://加载数据为空了，失败
				Log.i(TAG,"根据条件查询数据失败了");
				Toast.makeText(ConditionQueryPage.this, "没有查询到符合条件的场馆信息", 0).show();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.condition_query_page);
		sporttype=getIntent().getIntExtra("text", 0);
		initView();
		
		condition_sporttype.setText(ToolsHome.getSportName(sporttype));  
		
	}
	
	/** 对页面上的所有组件进行初始化操作,并绑定点击事件*/
	public void initView(){
		condition_page=(LinearLayout) findViewById(R.id.condition_page);//条件查询页面
		condition_listview=(LinearLayout) findViewById(R.id.condition_listview);//listview显示查询结果页面
		condition_query_back_arrow=(ImageView) findViewById(R.id.condition_query_back_arrow);//返回按钮
		condition_sporttype=(TextView) findViewById(R.id.condition_sporttype);//显示运动类型
		condition_area=(EditText) findViewById(R.id.condition_area);//选择场馆区域
		condition_time=(EditText) findViewById(R.id.condition_time);//选择时间
		condition_query=(Button) findViewById(R.id.condition_query);//查询按钮 
		show_list=(ListView) findViewById(R.id.show_list);//展示数据的listview组件
		
		
		condition_query_back_arrow.setOnClickListener(this);
		condition_area.setOnClickListener(this);
		condition_time.setOnClickListener(this);
		condition_query.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.condition_query_back_arrow://页面返回按钮
			finish();
			break;
		case R.id.condition_area://选择场馆区域
			conditionArea();//调用选择场馆区域的方法
			break;
		case R.id.condition_time://选择时间
			conditionTime();//调用选择时间的方法
			break;
		case R.id.condition_query://查询按钮
			String sportType=condition_sporttype.getText().toString().trim();
			String area=condition_area.getText().toString().trim();
			String time=condition_time.getText().toString().trim();
			if(TextUtils.isEmpty(sportType)){
				Toast.makeText(ConditionQueryPage.this, "运动类型不能为空", 0).show();
				return;
			}
			if(TextUtils.isEmpty(area)){
				Toast.makeText(ConditionQueryPage.this, "场馆区域不能为空", 0).show();
				return;
			}
			if(TextUtils.isEmpty(time)){
				Toast.makeText(ConditionQueryPage.this, "运动时间不能为空", 0).show();
				return;
			}
			
			if(TextUtils.isEmpty(condition_sporttype.getText().toString())) return;
			
			//根据以上条件进行查询数据当中
			Toast.makeText(ConditionQueryPage.this, "正在按条件查询当中...", 0).show(); 
			
			new Thread(){
				public void run() {
					try {
						JSONObject object=new JSONObject();
						object.put("Area",condition_area.getText().toString().trim());
						object.put("SportsName", condition_sporttype.getText().toString().trim());
						List<GetSportsVenuesBean> result=WebGetSportsVenues.getGetSportsVenuesData(object.toString());
						if(result!=null && result.size()>0){//判断查询到的结果不能为空，并且数据长度要大于0,或者显示未查询到数据。
							Message msg=new Message();
							msg.what=0x11;
							msg.obj=result;
							mHandler.sendMessage(msg);
						}else{
							mHandler.sendEmptyMessage(0x12);//发送获取失败标识到Handler当中
						} 
					} catch (JSONException e) { 
						e.printStackTrace();
					}
				};
			}.start(); 
			break;
		case R.id.condition_submit://点击了提交按钮
			if(venuesIDs.size()<=0){
				finish();
				return;
			}
			 for (final Integer i : venuesIDs) {
				new Thread(){
					public void run() { 
						try {
							JSONObject object=new JSONObject();
//							object.put("ProName", condition_sporttype.getText().toString().trim());
							
							/** 此字段是固定值,仅用于测试使用*/
							object.put("ProName", "空手道");
							
							object.put("MakeTime", condition_time.getText().toString().trim());
							object.put("UserID", UserGlobal.UserID);
							object.put("VenuesIDs", WebDataSet.get(i).getVenuesID()); 
							Log.i(TAG,"根据用户选择的场馆提交至服务器端的数据为："+object.toString());
							boolean flag=WebAddUserVM.getAddUserVMData(object.toString());
							Log.i("用户向服务端提交数据","当前普通用户向服务端提交数据之后返回的结果为:"+flag); 
						} catch (JSONException e) { 
							e.printStackTrace();
						}
					};
				}.start();
				
			} 
			 Toast.makeText(ConditionQueryPage.this, "订单提交成功", 0).show();
			//退回到首页
			finish();
			break;
		}
		
	}

	/**
	 * 显示ListView界面进行展示数据
	 */
	private void showQueryData() {
		//隐藏条件查询页面
		condition_page.setVisibility(View.GONE);
		mIdentifier=1;//标识已经隐藏了条件查询页面
		
		//显示ListView页面进行展示
		condition_listview.setVisibility(View.VISIBLE);
		//添加ListView页面底部的提交按钮
		View footer_view=View.inflate(ConditionQueryPage.this, R.layout.condition_button, null);
		condition_submit=(Button) footer_view.findViewById(R.id.condition_submit); //获取到Button组件
		show_list.addFooterView(footer_view);//把Button添加到ListView的底部 
		
		mListViewData=new ArrayList<Map<String,Object>>(); 
		
		adapter=new QuerySposrtsVenuesAdapter(
				ConditionQueryPage.this,
				getListViewData(),
				R.layout.sportvenues_listview_item,
				new String[]{"VenuesImager","VenuesName","Address","ReservePhone","RideRoute","VenuesEnvironment"},
				new int[]{
					R.id.query_listview_image,
					R.id.query_listview_title,
					R.id.query_listview_address,
					R.id.query_listview_telephone,
					R.id.query_listview_route,
					R.id.query_listview_environment,
					R.id.query_checkbox
				});
//		show_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		show_list.setAdapter(adapter);
		//为listview的条目绑定点击事件
		show_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i("PRINT","您刚才点击的条目:"+view);
				CheckBox check=(CheckBox) view.findViewById(R.id.query_checkbox);
				Integer i=position;
				if(check.isChecked()){
					check.setChecked(false); 
					if(venuesIDs.contains(i)){
						venuesIDs.remove(i);
					}else{
//						venuesIDs.add(i);
					}
				}else{
					check.setChecked(true);
					if(venuesIDs.contains(i)){
//						venuesIDs.remove(i);
					}else{
						venuesIDs.add(i);
					}
				} 
			} 
		});
		
		//为提交按钮绑定点击事件
		condition_submit.setOnClickListener(this);
	}
	
	/** 给ListView进行填充数据,默认每页只显示10条数据 */
	private List<Map<String,Object>> getListViewData(){ 
		if(WebDataSet!=null)Log.i(TAG,"现在WebDataSet的长度为:"+WebDataSet.size());
		
		if(WebDataSet!=null && WebDataSet.size() == 0){//如果WebDataSet数据集不为空，并且长度等于0就做用户提醒操作
//			Toast.makeText(getActivity(), "没有找到哦", 0).show();
		}else if(WebDataSet!=null && WebDataSet.size() > 0){ 
				for (int i=0; i<WebDataSet.size(); i++) {
					Log.i(TAG,"i值等于="+i);
					Map<String,Object> map=new HashMap<String,Object>();
					Log.i(TAG,"获取到的图片路径为="+WebDataSet.get(i).getVenuesImager()); 
					map.put("VenuesImager", WebServiceUtils.IMAGE_URL+WebDataSet.get(i).getVenuesImager());//场馆图片,这里是图片的Uri路径，然后到ListView适配器当中进行异步加载获取到图片	
					map.put("VenuesName", WebDataSet.get(i).getVenuesName());//场馆名称
					map.put("Address", "地址："+WebDataSet.get(i).getAddress());//详细地址
					map.put("ReservePhone", "电话："+WebDataSet.get(i).getReservePhone());//预定电话
					map.put("RideRoute", "路线："+WebDataSet.get(i).getRideRoute());//乘车路线
					map.put("VenuesEnvironment", "所属环境："+WebDataSet.get(i).getVenuesEnvironment());//场馆所属环境 
					map.put("VenuesID", WebDataSet.get(i).getVenuesID());
					mListViewData.add(map);
				}   
		}else{
			Log.i(TAG,"给适配器填充的数据为null");
		}
		return mListViewData; 
	}
	
	/**选择时间的方法*/
	private void conditionTime() {
		View view=View.inflate(ConditionQueryPage.this, R.layout.date_time_dialog, null);
		final DatePicker datePicker=(DatePicker) view.findViewById(R.id.date_pickers);
		final TimePicker timePicker=(TimePicker) view.findViewById(R.id.time_pickers);
		
		//创建时间选择对话框
		AlertDialog.Builder builder=new AlertDialog.Builder(ConditionQueryPage.this);
		builder.setView(view);
		
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
		
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(cal.get(Calendar.DAY_OF_MONTH));
		timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
		builder.setTitle("请选择日期和时间");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				StringBuffer sb=new StringBuffer();
				sb.append(String.format("%d-%02d-%02d",  
                        datePicker.getYear(),  
                        datePicker.getMonth() + 1,  
                        datePicker.getDayOfMonth()));
				sb.append("	");
				sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
				condition_time.setText(sb);
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	/** 选择场馆区域的方法*/
	private void conditionArea() {
		/** 目前只涉及北京地区的所有场馆*/
		final String[] data={"东城区","东城区","东城区","宣武区","朝阳区","海淀区","丰台区","石景山区","门头沟区","房山区","通州区",
				"昌平区","大兴区","顺义区","怀柔区","平谷区"," 密云县","延庆县"}; 
		
		//创建场馆选择对话框
		AlertDialog.Builder builder=new AlertDialog.Builder(ConditionQueryPage.this)
		.setTitle("请选择场馆区域")
		.setAdapter(new ArrayAdapter<String>(ConditionQueryPage.this, R.layout.custom_empty_textview, data), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//填充EditText文本里面的内容
				condition_area.setText(data[which]); 
				dialog.cancel();
			}
		}); 
		builder.create().show(); 
	}
}
