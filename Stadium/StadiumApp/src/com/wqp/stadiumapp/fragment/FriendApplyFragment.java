package com.wqp.stadiumapp.fragment;
 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button; 
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wqp.stadiumapp.R; 
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.webservice.WebAddReservation;
import com.wqp.webservice.WebGetSports;
import com.wqp.webservice.entity.AddReservationBean;
import com.wqp.webservice.entity.GetSportBean;

public class FriendApplyFragment extends Fragment implements OnClickListener{
	private String TAG="FriendApplyFragment"; 
	private List<GetSportBean> DataSet;//返回的数据集
	private GetSportBean mGetSportBean;//用于存储获取到的运动信息
	private ArrayAdapter<String> adapter_sporttype;//运动类型Spinner适配器
	private AddReservationBean mAddReservationBean;//存入用户输入的所有数据
	private static int mSportType=-1;//运动类型，这个数据是用户选择了下拉列表之后会更新,否则为是null
	private static String mVenuesCost=null;//存放用户选择的费用支出 

	public Calendar time=Calendar.getInstance(Locale.CHINA);
	public SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DatePicker datePicker;
	private TimePicker timePicker; 
	private AlertDialog dialog; 
	private View mMainView;
	
	//场馆费用数据
	private String[] friend_apply_venuescostData={"不限","费用均摊","发布者支付","竟赛败方支付","AA制","其它方式"};
	
	private Spinner friend_apply_sporttype,friend_apply_venuescost;
	private Button friend_apply_confirm;
	private ImageView friend_apply_applyendtime_calculator,friend_apply_starttime_calculator,friend_apply_endtime_calculator;
	private EditText friend_apply_acttitle,friend_apply_venuesname,friend_apply_applyendtime,
	friend_apply_starttime,friend_apply_endtime,friend_apply_takecount,friend_apply_refereecount,friend_apply_guidecount,
	friend_apply_actcon,friend_apply_actnotice;
	
	//获取到日历实例
//	private Calendar mCalendar=Calendar.getInstance();
	
	//定义消息管理器
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x11:
				DataSet=(List<GetSportBean>) msg.obj; 
				initSpinner();//对Spinner控件加载数据
				adapter_sporttype.notifyDataSetChanged();
				break;
			case 0x22://发送标识获取到的数据为空
				
				break;
			case 0x33://添加活动成功
				Toast.makeText(getActivity(), "添加活动成功", 0).show();
				break;
			case 0x44://添加活动失败
				Toast.makeText(getActivity(), "添加活动失败", 0).show();
				break;
			default:
				break;
			}
			
		};
	};
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.friend_apply, container,false); 
		mMainView=view;
		conditionQuery();//从服务端加载数据
		initView(view);//初始化页面上的所有组件 
		mAddReservationBean=new AddReservationBean();//创建用户输入数据存储位置的实例
		
		//设置软键盘的状态
//		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
		
		return view;
	}
	
	/** 对页面上的所有需要组件初始化*/
	public void initView(View v){
		friend_apply_sporttype=(Spinner) v.findViewById(R.id.friend_apply_sporttype);//运动类型
		friend_apply_acttitle=(EditText) v.findViewById(R.id.friend_apply_acttitle);//活动标题
		friend_apply_venuesname=(EditText) v.findViewById(R.id.friend_apply_venuesname);//举办场馆
		friend_apply_venuescost= (Spinner) v.findViewById(R.id.friend_apply_venuescost);//费用支出
		friend_apply_applyendtime=(EditText) v.findViewById(R.id.friend_apply_applyendtime);//截止报名时间
		friend_apply_applyendtime_calculator=(ImageView) v.findViewById(R.id.friend_apply_applyendtime_calculator);//截止报名时间日历
		friend_apply_starttime=(EditText) v.findViewById(R.id.friend_apply_starttime);//开始时间
		friend_apply_starttime_calculator=(ImageView) v.findViewById(R.id.friend_apply_starttime_calculator);//开始时间日历
		friend_apply_endtime=(EditText) v.findViewById(R.id.friend_apply_endtime);//截止时间
		friend_apply_endtime_calculator=(ImageView) v.findViewById(R.id.friend_apply_endtime_calculator);//截止时间日历
		friend_apply_takecount=(EditText) v.findViewById(R.id.friend_apply_takecount);//人数上限
		friend_apply_refereecount=(EditText) v.findViewById(R.id.friend_apply_refereecount);//裁判员人数
		friend_apply_guidecount=(EditText) v.findViewById(R.id.friend_apply_guidecount);//指导员人数
		friend_apply_actcon=(EditText) v.findViewById(R.id.friend_apply_actcon);//具体内容
		friend_apply_actnotice=(EditText) v.findViewById(R.id.friend_apply_actnotice);//活动公告
		friend_apply_confirm=(Button) v.findViewById(R.id.friend_apply_confirm);//确定
		
		friend_apply_applyendtime_calculator.setOnClickListener(this);
		friend_apply_starttime_calculator.setOnClickListener(this);
		friend_apply_endtime_calculator.setOnClickListener(this);
		friend_apply_confirm.setOnClickListener(this); 
		 
		ArrayAdapter<String> friend_apply_venuescostAdapter=new ArrayAdapter<String>(
				getActivity(),
				R.layout.custom_empty_textview,
				friend_apply_venuescostData);
		friend_apply_venuescost.setPrompt("请选择费用支出类型");//设置Spinner的标题
		//给费用支出的Spinner设置适配器 
		friend_apply_venuescost.setAdapter(friend_apply_venuescostAdapter);
		friend_apply_venuescost.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { 
				mVenuesCost=friend_apply_venuescostData[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { }
			
		});
	}
	
	/** 对Spinner控件加载数据*/
	public void initSpinner(){  
		List<String> data=new ArrayList<String>();
		mGetSportBean=null;
		mGetSportBean=new GetSportBean();
		for (GetSportBean gsb : DataSet) {
			data.add(gsb.getSportname());
			mGetSportBean.setSportid(gsb.getSportid());
			mGetSportBean.setSportname(gsb.getSportname());
			mGetSportBean.setIsrec(gsb.isIsrec());
		}
		adapter_sporttype=new ArrayAdapter<String>(
		getActivity(), 
		R.layout.custom_empty_textview, 
		data);
		friend_apply_sporttype.setPrompt("请选择运动类型");//设置Spinner的标题
		//给运动类型的Spinner设置适配器
		friend_apply_sporttype.setAdapter(adapter_sporttype);
		friend_apply_sporttype.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { 
				Log.i("用户从下拉列表中选择的是:",DataSet.get(position).getSportname());
				mSportType=DataSet.get(position).getSportid();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { }  
		});
		
	} 
	
 
	/** 开启线程进行加载数据*/
	public void conditionQuery() {   
		new Thread(){    
			public void run() {
				try { 
					List<GetSportBean> results=WebGetSports.getGetSportsData("");
					if(results!=null && results.size()>0){
						Message msg=new Message();
						msg.what=0x11;//标识正常获取到数据了
						msg.obj=results;
						mHandler.sendMessage(msg); 
						Log.i(TAG,"Web端线程加载数据结束,我已经发送数据到Handler了");
					}else{
						mHandler.sendEmptyMessage(0x22);//发送标识获取到的数据为空
					}
				} catch (Exception e) {  
					e.printStackTrace(); 
				}  
			};
		}.start();
	} 
	 

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friend_apply_applyendtime_calculator://截止报名时间日历  
			dateTimePickerDialog(friend_apply_applyendtime); 			
			break;
		case R.id.friend_apply_starttime_calculator://开始时间日历
			dateTimePickerDialog(friend_apply_starttime); 		
			break;
		case R.id.friend_apply_endtime_calculator://截止时间日历
			dateTimePickerDialog(friend_apply_endtime); 
			break;
		case R.id.friend_apply_confirm://确定按钮
			if(mAddReservationBean==null){return;}
			if(mSportType>-1){
				mAddReservationBean.setSportType(mSportType);//填写运动类型
				mSportType=-1;
			}else{
				Toast.makeText(getActivity(), "请选择运动类型", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_acttitle.getText().toString())){
				mAddReservationBean.setActTitle(friend_apply_acttitle.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "请填写活动标题", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_venuesname.getText().toString())){
				mAddReservationBean.setVenuesName(friend_apply_venuesname.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "请填写举办场馆", 0).show();return;
			}
			if(!TextUtils.isEmpty(mVenuesCost)){//场馆费用支出
				mAddReservationBean.setVenuesCost(mVenuesCost); 
			}else{
				Toast.makeText(getActivity(), "请填写费用支出", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_applyendtime.getText().toString())){
				mAddReservationBean.setApplyEndTime(friend_apply_applyendtime.getText().toString());
			}else{
				Toast.makeText(getActivity(), "请填写截止报名时间", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_starttime.getText().toString())){
				mAddReservationBean.setStartTime(friend_apply_starttime.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "请填写开始时间", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_endtime.getText().toString())){
				mAddReservationBean.setEndTime(friend_apply_endtime.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "请填写截止时间", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_takecount.getText().toString())){
				mAddReservationBean.setTakeCount(Integer.valueOf(friend_apply_takecount.getText().toString().trim()).intValue());
			}else{
				Toast.makeText(getActivity(), "请填写人数上限", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_refereecount.getText().toString())){
				mAddReservationBean.setRefereeCount(Integer.valueOf(friend_apply_refereecount.getText().toString().trim()).intValue());	
			}else{
				Toast.makeText(getActivity(), "请填写裁判员人数", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_guidecount.getText().toString())){
				mAddReservationBean.setGuideCount(Integer.valueOf(friend_apply_guidecount.getText().toString().trim()).intValue());	
			}else{
				Toast.makeText(getActivity(), "请填写指导员人数", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_actcon.getText().toString())){
				mAddReservationBean.setActCon(friend_apply_actcon.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "请填写具体内容", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_actnotice.getText().toString())){
				mAddReservationBean.setActNotice(friend_apply_actnotice.getText().toString().trim());	
			}else{
				Toast.makeText(getActivity(), "请填写活动公告", 0).show();return;
			}
			new Thread(){  
				public void run() {  
					mSportType=-1;//清零运动类型
					mVenuesCost=null;//清零费用支出
					String str=ObjectParseString(mAddReservationBean);
					Log.i("用户添加的数据为:",str);
					boolean result=WebAddReservation.getAddReservationData(str);//添加数据到服务端
					Message addmsg=new Message();
					if(result){
						addmsg.what=0x33;
						mHandler.sendMessage(addmsg);
					}else{
						addmsg.what=0x44;//
						mHandler.sendMessage(addmsg);
					} 
				};
			}.start();
			break; 
		} 
	}
	
	/** 把AddReservationBean里面的数据转换成为JSON格式的字符串形式返回*/
	private String ObjectParseString(AddReservationBean ab){ 
		JSONObject object=new JSONObject();
		try{ 
			object.put("SportType",ab.getSportType().intValue()); 
			object.put("ActTitle", ab.getActTitle()); 
			object.put("VenuesName", ab.getVenuesName());
			object.put("VenuesCost", ab.getVenuesCost());
			object.put("StartTime", ab.getStartTime());
			object.put("EndTime", ab.getEndTime());
			object.put("ApplyEndTime", ab.getApplyEndTime());
			object.put("TakeCount", ab.getTakeCount());
			object.put("RefereeCount", ab.getRefereeCount());
			object.put("GuideCount", ab.getGuideCount());
			object.put("ActCon", ab.getActCon());
			object.put("ActNotice", ab.getActNotice());
			object.put("CreateUser", UserGlobal.UserID);//创建人就 填写的是正常登录用户的用户昵称 
			return object.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onDestroy() {
		Log.i("方法FriendApplyFragment","执行了onDestroy()");
		super.onDestroy();
	}
	
	/** 生成一个组合的对话框,并返回*/
	public AlertDialog dateTimePickerDialog(final EditText dataView){
		LinearLayout dateTimeLayout=(LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_date_time_selected, null);	
		datePicker=(DatePicker) dateTimeLayout.findViewById(R.id.selected_datepicker);
		timePicker=(TimePicker) dateTimeLayout.findViewById(R.id.selected_timepicker); 
		timePicker.setIs24HourView(true); 
		OnTimeChangedListener timeListener=new OnTimeChangedListener(){ 
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				time.set(Calendar.HOUR_OF_DAY, hourOfDay);
				time.set(Calendar.MINUTE, minute);
			} 
		};
		//为时间选择器设置监听
		timePicker.setOnTimeChangedListener(timeListener);
		
		OnDateChangedListener dateListener=new OnDateChangedListener(){ 
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				time.set(Calendar.YEAR, year);
				time.set(Calendar.MONTH, monthOfYear);
				time.set(Calendar.DAY_OF_MONTH, dayOfMonth); 
			} 
		};
		//为日期选择器设置监听
		datePicker.init(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), dateListener);
 		
		timePicker.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(time.get(Calendar.MINUTE));
		
		dialog=new AlertDialog.Builder(getActivity())
		.setTitle("请选择日期和时间")
		.setView(dateTimeLayout)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				datePicker.clearFocus();//清除日期选择器的焦点
				timePicker.clearFocus();//清除时间选择器的焦点
				time.set(Calendar.YEAR, datePicker.getYear());  
                time.set(Calendar.MONTH, datePicker.getMonth());  
                time.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());  
                time.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());  
                time.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                updateLabel(dataView);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) { 
				if(dataView!=null){
					dataView.setText("");
				}
			}
		}).show();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	
	/** 更新标签*/
	public void updateLabel(EditText dataView){
		if(dataView!=null){
			dataView.setText(format.format(time.getTime()));
		} 
	} 
	
}
