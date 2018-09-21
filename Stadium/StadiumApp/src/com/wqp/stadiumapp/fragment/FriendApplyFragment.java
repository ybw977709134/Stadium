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
	private List<GetSportBean> DataSet;//���ص����ݼ�
	private GetSportBean mGetSportBean;//���ڴ洢��ȡ�����˶���Ϣ
	private ArrayAdapter<String> adapter_sporttype;//�˶�����Spinner������
	private AddReservationBean mAddReservationBean;//�����û��������������
	private static int mSportType=-1;//�˶����ͣ�����������û�ѡ���������б�֮������,����Ϊ��null
	private static String mVenuesCost=null;//����û�ѡ��ķ���֧�� 

	public Calendar time=Calendar.getInstance(Locale.CHINA);
	public SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DatePicker datePicker;
	private TimePicker timePicker; 
	private AlertDialog dialog; 
	private View mMainView;
	
	//���ݷ�������
	private String[] friend_apply_venuescostData={"����","���þ�̯","������֧��","�����ܷ�֧��","AA��","������ʽ"};
	
	private Spinner friend_apply_sporttype,friend_apply_venuescost;
	private Button friend_apply_confirm;
	private ImageView friend_apply_applyendtime_calculator,friend_apply_starttime_calculator,friend_apply_endtime_calculator;
	private EditText friend_apply_acttitle,friend_apply_venuesname,friend_apply_applyendtime,
	friend_apply_starttime,friend_apply_endtime,friend_apply_takecount,friend_apply_refereecount,friend_apply_guidecount,
	friend_apply_actcon,friend_apply_actnotice;
	
	//��ȡ������ʵ��
//	private Calendar mCalendar=Calendar.getInstance();
	
	//������Ϣ������
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x11:
				DataSet=(List<GetSportBean>) msg.obj; 
				initSpinner();//��Spinner�ؼ���������
				adapter_sporttype.notifyDataSetChanged();
				break;
			case 0x22://���ͱ�ʶ��ȡ��������Ϊ��
				
				break;
			case 0x33://��ӻ�ɹ�
				Toast.makeText(getActivity(), "��ӻ�ɹ�", 0).show();
				break;
			case 0x44://��ӻʧ��
				Toast.makeText(getActivity(), "��ӻʧ��", 0).show();
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
		conditionQuery();//�ӷ���˼�������
		initView(view);//��ʼ��ҳ���ϵ�������� 
		mAddReservationBean=new AddReservationBean();//�����û��������ݴ洢λ�õ�ʵ��
		
		//��������̵�״̬
//		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
		
		return view;
	}
	
	/** ��ҳ���ϵ�������Ҫ�����ʼ��*/
	public void initView(View v){
		friend_apply_sporttype=(Spinner) v.findViewById(R.id.friend_apply_sporttype);//�˶�����
		friend_apply_acttitle=(EditText) v.findViewById(R.id.friend_apply_acttitle);//�����
		friend_apply_venuesname=(EditText) v.findViewById(R.id.friend_apply_venuesname);//�ٰ쳡��
		friend_apply_venuescost= (Spinner) v.findViewById(R.id.friend_apply_venuescost);//����֧��
		friend_apply_applyendtime=(EditText) v.findViewById(R.id.friend_apply_applyendtime);//��ֹ����ʱ��
		friend_apply_applyendtime_calculator=(ImageView) v.findViewById(R.id.friend_apply_applyendtime_calculator);//��ֹ����ʱ������
		friend_apply_starttime=(EditText) v.findViewById(R.id.friend_apply_starttime);//��ʼʱ��
		friend_apply_starttime_calculator=(ImageView) v.findViewById(R.id.friend_apply_starttime_calculator);//��ʼʱ������
		friend_apply_endtime=(EditText) v.findViewById(R.id.friend_apply_endtime);//��ֹʱ��
		friend_apply_endtime_calculator=(ImageView) v.findViewById(R.id.friend_apply_endtime_calculator);//��ֹʱ������
		friend_apply_takecount=(EditText) v.findViewById(R.id.friend_apply_takecount);//��������
		friend_apply_refereecount=(EditText) v.findViewById(R.id.friend_apply_refereecount);//����Ա����
		friend_apply_guidecount=(EditText) v.findViewById(R.id.friend_apply_guidecount);//ָ��Ա����
		friend_apply_actcon=(EditText) v.findViewById(R.id.friend_apply_actcon);//��������
		friend_apply_actnotice=(EditText) v.findViewById(R.id.friend_apply_actnotice);//�����
		friend_apply_confirm=(Button) v.findViewById(R.id.friend_apply_confirm);//ȷ��
		
		friend_apply_applyendtime_calculator.setOnClickListener(this);
		friend_apply_starttime_calculator.setOnClickListener(this);
		friend_apply_endtime_calculator.setOnClickListener(this);
		friend_apply_confirm.setOnClickListener(this); 
		 
		ArrayAdapter<String> friend_apply_venuescostAdapter=new ArrayAdapter<String>(
				getActivity(),
				R.layout.custom_empty_textview,
				friend_apply_venuescostData);
		friend_apply_venuescost.setPrompt("��ѡ�����֧������");//����Spinner�ı���
		//������֧����Spinner���������� 
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
	
	/** ��Spinner�ؼ���������*/
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
		friend_apply_sporttype.setPrompt("��ѡ���˶�����");//����Spinner�ı���
		//���˶����͵�Spinner����������
		friend_apply_sporttype.setAdapter(adapter_sporttype);
		friend_apply_sporttype.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { 
				Log.i("�û��������б���ѡ�����:",DataSet.get(position).getSportname());
				mSportType=DataSet.get(position).getSportid();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { }  
		});
		
	} 
	
 
	/** �����߳̽��м�������*/
	public void conditionQuery() {   
		new Thread(){    
			public void run() {
				try { 
					List<GetSportBean> results=WebGetSports.getGetSportsData("");
					if(results!=null && results.size()>0){
						Message msg=new Message();
						msg.what=0x11;//��ʶ������ȡ��������
						msg.obj=results;
						mHandler.sendMessage(msg); 
						Log.i(TAG,"Web���̼߳������ݽ���,���Ѿ��������ݵ�Handler��");
					}else{
						mHandler.sendEmptyMessage(0x22);//���ͱ�ʶ��ȡ��������Ϊ��
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
		case R.id.friend_apply_applyendtime_calculator://��ֹ����ʱ������  
			dateTimePickerDialog(friend_apply_applyendtime); 			
			break;
		case R.id.friend_apply_starttime_calculator://��ʼʱ������
			dateTimePickerDialog(friend_apply_starttime); 		
			break;
		case R.id.friend_apply_endtime_calculator://��ֹʱ������
			dateTimePickerDialog(friend_apply_endtime); 
			break;
		case R.id.friend_apply_confirm://ȷ����ť
			if(mAddReservationBean==null){return;}
			if(mSportType>-1){
				mAddReservationBean.setSportType(mSportType);//��д�˶�����
				mSportType=-1;
			}else{
				Toast.makeText(getActivity(), "��ѡ���˶�����", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_acttitle.getText().toString())){
				mAddReservationBean.setActTitle(friend_apply_acttitle.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "����д�����", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_venuesname.getText().toString())){
				mAddReservationBean.setVenuesName(friend_apply_venuesname.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "����д�ٰ쳡��", 0).show();return;
			}
			if(!TextUtils.isEmpty(mVenuesCost)){//���ݷ���֧��
				mAddReservationBean.setVenuesCost(mVenuesCost); 
			}else{
				Toast.makeText(getActivity(), "����д����֧��", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_applyendtime.getText().toString())){
				mAddReservationBean.setApplyEndTime(friend_apply_applyendtime.getText().toString());
			}else{
				Toast.makeText(getActivity(), "����д��ֹ����ʱ��", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_starttime.getText().toString())){
				mAddReservationBean.setStartTime(friend_apply_starttime.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "����д��ʼʱ��", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_endtime.getText().toString())){
				mAddReservationBean.setEndTime(friend_apply_endtime.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "����д��ֹʱ��", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_takecount.getText().toString())){
				mAddReservationBean.setTakeCount(Integer.valueOf(friend_apply_takecount.getText().toString().trim()).intValue());
			}else{
				Toast.makeText(getActivity(), "����д��������", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_refereecount.getText().toString())){
				mAddReservationBean.setRefereeCount(Integer.valueOf(friend_apply_refereecount.getText().toString().trim()).intValue());	
			}else{
				Toast.makeText(getActivity(), "����д����Ա����", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_guidecount.getText().toString())){
				mAddReservationBean.setGuideCount(Integer.valueOf(friend_apply_guidecount.getText().toString().trim()).intValue());	
			}else{
				Toast.makeText(getActivity(), "����дָ��Ա����", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_actcon.getText().toString())){
				mAddReservationBean.setActCon(friend_apply_actcon.getText().toString().trim());
			}else{
				Toast.makeText(getActivity(), "����д��������", 0).show();return;
			}
			if(!TextUtils.isEmpty(friend_apply_actnotice.getText().toString())){
				mAddReservationBean.setActNotice(friend_apply_actnotice.getText().toString().trim());	
			}else{
				Toast.makeText(getActivity(), "����д�����", 0).show();return;
			}
			new Thread(){  
				public void run() {  
					mSportType=-1;//�����˶�����
					mVenuesCost=null;//�������֧��
					String str=ObjectParseString(mAddReservationBean);
					Log.i("�û���ӵ�����Ϊ:",str);
					boolean result=WebAddReservation.getAddReservationData(str);//������ݵ������
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
	
	/** ��AddReservationBean���������ת����ΪJSON��ʽ���ַ�����ʽ����*/
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
			object.put("CreateUser", UserGlobal.UserID);//�����˾� ��д����������¼�û����û��ǳ� 
			return object.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onDestroy() {
		Log.i("����FriendApplyFragment","ִ����onDestroy()");
		super.onDestroy();
	}
	
	/** ����һ����ϵĶԻ���,������*/
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
		//Ϊʱ��ѡ�������ü���
		timePicker.setOnTimeChangedListener(timeListener);
		
		OnDateChangedListener dateListener=new OnDateChangedListener(){ 
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				time.set(Calendar.YEAR, year);
				time.set(Calendar.MONTH, monthOfYear);
				time.set(Calendar.DAY_OF_MONTH, dayOfMonth); 
			} 
		};
		//Ϊ����ѡ�������ü���
		datePicker.init(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), dateListener);
 		
		timePicker.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(time.get(Calendar.MINUTE));
		
		dialog=new AlertDialog.Builder(getActivity())
		.setTitle("��ѡ�����ں�ʱ��")
		.setView(dateTimeLayout)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				datePicker.clearFocus();//�������ѡ�����Ľ���
				timePicker.clearFocus();//���ʱ��ѡ�����Ľ���
				time.set(Calendar.YEAR, datePicker.getYear());  
                time.set(Calendar.MONTH, datePicker.getMonth());  
                time.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());  
                time.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());  
                time.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                updateLabel(dataView);
			}
		})
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { 
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
	
	/** ���±�ǩ*/
	public void updateLabel(EditText dataView){
		if(dataView!=null){
			dataView.setText(format.format(time.getTime()));
		} 
	} 
	
}
