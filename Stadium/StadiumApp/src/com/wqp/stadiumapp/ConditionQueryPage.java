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

/** �û������ҳ�����"����"��"����"��ͼ��󣬻���ת����ҳ��*/
public class ConditionQueryPage extends Activity implements OnClickListener {
	private static final String TAG = "ConditionQueryPage";
	private ImageView condition_query_back_arrow;
	private TextView condition_sporttype;
	private EditText condition_area,condition_time;
	private Button condition_query,condition_submit;
	private LinearLayout condition_page,condition_listview;
	private ListView show_list;
	
	/** �ü������ڴ�Ÿ���������ѯ���ĳ�������*/
//	private List<String> mQueryData=new ArrayList<String>();
	
	/** �ü������ڴ���û���ListView����ѡ��ľ�����Щ��Ŀ���*/
//	private List<Integer> mChoiceIden=new ArrayList<Integer>();
	private int mIdentifier=0;
	private int sporttype;//�û���������ViewPager�е�����˶����ͱ��
	
	private List<GetSportsVenuesBean> WebDataSet=null;//��Ŵӷ���˻�ȡ�������ݼ�
	public static List<Integer> venuesIDs=new ArrayList<Integer>();//����û�ѡ��ĳ���ID�����
	private QuerySposrtsVenuesAdapter adapter;//��������ѯ���ݵ�������
	private List<Map<String,Object>> mListViewData;//����listviewÿ����Ŀ������  
	
	
	//������Ϣ������
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0x11://��ȡ�����ݳɹ�
				WebDataSet=(List<GetSportsVenuesBean>) msg.obj;
				showQueryData();//ͨ��ListView������ʾ����  
				break;
			case 0x12://��������Ϊ���ˣ�ʧ��
				Log.i(TAG,"����������ѯ����ʧ����");
				Toast.makeText(ConditionQueryPage.this, "û�в�ѯ�����������ĳ�����Ϣ", 0).show();
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
	
	/** ��ҳ���ϵ�����������г�ʼ������,���󶨵���¼�*/
	public void initView(){
		condition_page=(LinearLayout) findViewById(R.id.condition_page);//������ѯҳ��
		condition_listview=(LinearLayout) findViewById(R.id.condition_listview);//listview��ʾ��ѯ���ҳ��
		condition_query_back_arrow=(ImageView) findViewById(R.id.condition_query_back_arrow);//���ذ�ť
		condition_sporttype=(TextView) findViewById(R.id.condition_sporttype);//��ʾ�˶�����
		condition_area=(EditText) findViewById(R.id.condition_area);//ѡ�񳡹�����
		condition_time=(EditText) findViewById(R.id.condition_time);//ѡ��ʱ��
		condition_query=(Button) findViewById(R.id.condition_query);//��ѯ��ť 
		show_list=(ListView) findViewById(R.id.show_list);//չʾ���ݵ�listview���
		
		
		condition_query_back_arrow.setOnClickListener(this);
		condition_area.setOnClickListener(this);
		condition_time.setOnClickListener(this);
		condition_query.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.condition_query_back_arrow://ҳ�淵�ذ�ť
			finish();
			break;
		case R.id.condition_area://ѡ�񳡹�����
			conditionArea();//����ѡ�񳡹�����ķ���
			break;
		case R.id.condition_time://ѡ��ʱ��
			conditionTime();//����ѡ��ʱ��ķ���
			break;
		case R.id.condition_query://��ѯ��ť
			String sportType=condition_sporttype.getText().toString().trim();
			String area=condition_area.getText().toString().trim();
			String time=condition_time.getText().toString().trim();
			if(TextUtils.isEmpty(sportType)){
				Toast.makeText(ConditionQueryPage.this, "�˶����Ͳ���Ϊ��", 0).show();
				return;
			}
			if(TextUtils.isEmpty(area)){
				Toast.makeText(ConditionQueryPage.this, "����������Ϊ��", 0).show();
				return;
			}
			if(TextUtils.isEmpty(time)){
				Toast.makeText(ConditionQueryPage.this, "�˶�ʱ�䲻��Ϊ��", 0).show();
				return;
			}
			
			if(TextUtils.isEmpty(condition_sporttype.getText().toString())) return;
			
			//���������������в�ѯ���ݵ���
			Toast.makeText(ConditionQueryPage.this, "���ڰ�������ѯ����...", 0).show(); 
			
			new Thread(){
				public void run() {
					try {
						JSONObject object=new JSONObject();
						object.put("Area",condition_area.getText().toString().trim());
						object.put("SportsName", condition_sporttype.getText().toString().trim());
						List<GetSportsVenuesBean> result=WebGetSportsVenues.getGetSportsVenuesData(object.toString());
						if(result!=null && result.size()>0){//�жϲ�ѯ���Ľ������Ϊ�գ��������ݳ���Ҫ����0,������ʾδ��ѯ�����ݡ�
							Message msg=new Message();
							msg.what=0x11;
							msg.obj=result;
							mHandler.sendMessage(msg);
						}else{
							mHandler.sendEmptyMessage(0x12);//���ͻ�ȡʧ�ܱ�ʶ��Handler����
						} 
					} catch (JSONException e) { 
						e.printStackTrace();
					}
				};
			}.start(); 
			break;
		case R.id.condition_submit://������ύ��ť
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
							
							/** ���ֶ��ǹ̶�ֵ,�����ڲ���ʹ��*/
							object.put("ProName", "���ֵ�");
							
							object.put("MakeTime", condition_time.getText().toString().trim());
							object.put("UserID", UserGlobal.UserID);
							object.put("VenuesIDs", WebDataSet.get(i).getVenuesID()); 
							Log.i(TAG,"�����û�ѡ��ĳ����ύ���������˵�����Ϊ��"+object.toString());
							boolean flag=WebAddUserVM.getAddUserVMData(object.toString());
							Log.i("�û��������ύ����","��ǰ��ͨ�û��������ύ����֮�󷵻صĽ��Ϊ:"+flag); 
						} catch (JSONException e) { 
							e.printStackTrace();
						}
					};
				}.start();
				
			} 
			 Toast.makeText(ConditionQueryPage.this, "�����ύ�ɹ�", 0).show();
			//�˻ص���ҳ
			finish();
			break;
		}
		
	}

	/**
	 * ��ʾListView�������չʾ����
	 */
	private void showQueryData() {
		//����������ѯҳ��
		condition_page.setVisibility(View.GONE);
		mIdentifier=1;//��ʶ�Ѿ�������������ѯҳ��
		
		//��ʾListViewҳ�����չʾ
		condition_listview.setVisibility(View.VISIBLE);
		//���ListViewҳ��ײ����ύ��ť
		View footer_view=View.inflate(ConditionQueryPage.this, R.layout.condition_button, null);
		condition_submit=(Button) footer_view.findViewById(R.id.condition_submit); //��ȡ��Button���
		show_list.addFooterView(footer_view);//��Button��ӵ�ListView�ĵײ� 
		
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
		//Ϊlistview����Ŀ�󶨵���¼�
		show_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i("PRINT","���ղŵ������Ŀ:"+view);
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
		
		//Ϊ�ύ��ť�󶨵���¼�
		condition_submit.setOnClickListener(this);
	}
	
	/** ��ListView�����������,Ĭ��ÿҳֻ��ʾ10������ */
	private List<Map<String,Object>> getListViewData(){ 
		if(WebDataSet!=null)Log.i(TAG,"����WebDataSet�ĳ���Ϊ:"+WebDataSet.size());
		
		if(WebDataSet!=null && WebDataSet.size() == 0){//���WebDataSet���ݼ���Ϊ�գ����ҳ��ȵ���0�����û����Ѳ���
//			Toast.makeText(getActivity(), "û���ҵ�Ŷ", 0).show();
		}else if(WebDataSet!=null && WebDataSet.size() > 0){ 
				for (int i=0; i<WebDataSet.size(); i++) {
					Log.i(TAG,"iֵ����="+i);
					Map<String,Object> map=new HashMap<String,Object>();
					Log.i(TAG,"��ȡ����ͼƬ·��Ϊ="+WebDataSet.get(i).getVenuesImager()); 
					map.put("VenuesImager", WebServiceUtils.IMAGE_URL+WebDataSet.get(i).getVenuesImager());//����ͼƬ,������ͼƬ��Uri·����Ȼ��ListView���������н����첽���ػ�ȡ��ͼƬ	
					map.put("VenuesName", WebDataSet.get(i).getVenuesName());//��������
					map.put("Address", "��ַ��"+WebDataSet.get(i).getAddress());//��ϸ��ַ
					map.put("ReservePhone", "�绰��"+WebDataSet.get(i).getReservePhone());//Ԥ���绰
					map.put("RideRoute", "·�ߣ�"+WebDataSet.get(i).getRideRoute());//�˳�·��
					map.put("VenuesEnvironment", "����������"+WebDataSet.get(i).getVenuesEnvironment());//������������ 
					map.put("VenuesID", WebDataSet.get(i).getVenuesID());
					mListViewData.add(map);
				}   
		}else{
			Log.i(TAG,"����������������Ϊnull");
		}
		return mListViewData; 
	}
	
	/**ѡ��ʱ��ķ���*/
	private void conditionTime() {
		View view=View.inflate(ConditionQueryPage.this, R.layout.date_time_dialog, null);
		final DatePicker datePicker=(DatePicker) view.findViewById(R.id.date_pickers);
		final TimePicker timePicker=(TimePicker) view.findViewById(R.id.time_pickers);
		
		//����ʱ��ѡ��Ի���
		AlertDialog.Builder builder=new AlertDialog.Builder(ConditionQueryPage.this);
		builder.setView(view);
		
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
		
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(cal.get(Calendar.DAY_OF_MONTH));
		timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
		builder.setTitle("��ѡ�����ں�ʱ��");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
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

	/** ѡ�񳡹�����ķ���*/
	private void conditionArea() {
		/** Ŀǰֻ�漰�������������г���*/
		final String[] data={"������","������","������","������","������","������","��̨��","ʯ��ɽ��","��ͷ����","��ɽ��","ͨ����",
				"��ƽ��","������","˳����","������","ƽ����"," ������","������"}; 
		
		//��������ѡ��Ի���
		AlertDialog.Builder builder=new AlertDialog.Builder(ConditionQueryPage.this)
		.setTitle("��ѡ�񳡹�����")
		.setAdapter(new ArrayAdapter<String>(ConditionQueryPage.this, R.layout.custom_empty_textview, data), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//���EditText�ı����������
				condition_area.setText(data[which]); 
				dialog.cancel();
			}
		}); 
		builder.create().show(); 
	}
}
