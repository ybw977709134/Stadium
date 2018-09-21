package com.wqp.stadiumapp;
 
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;   
import com.wqp.alipay.PayResult;
import com.wqp.alipay.SignUtils;
import com.wqp.stadiumapp.utils.AliPayTools;
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils; 
import com.wqp.webservice.WebAddMake;
import com.wqp.webservice.WebGetAllMake;
import com.wqp.webservice.entity.GetAllMakeBean;

/** ����ԤԼ*/
public class StadiumOrder extends Activity implements OnClickListener {
	private String TAG="StadiumOrder";
	private ImageView stadium_order_picture,stadium_order_back_arrow,stadium_order_calculator;
	private Spinner stadium_sporttypes;
	private TextView stadium_names,stadium_moneys;
	private EditText stadium_order_names,stadium_order_time;
	private Button stadium_order_pays;
	private ProgressDialog mProgressDialog;//���ȶԻ���
	private String[] mPayType={"֧����֧��","΢��֧��"}; 
	private int mPayTypeID=0;//���ڱ�ʶ��ǰ�û�ѡ���֧����ʽ���,Ĭ��Ϊ0��ʾ֧����֧��,1��ʾ΢��֧��
	
	private List<GetAllMakeBean> DataSet;//���ص����ݼ�
	private ArrayAdapter<String> adapter_sporttype;//�˶�����Spinner������ 
	private String mSportType=null;//���ڴ���û�ѡ����˶�����
	private int mSportTypeID=-1;//���ڴ���û�ѡ����˶�����ID
	private int mVenuesID;//����ID��,��IDֵ�Ǵ���һҳ�洫�ݹ����Ĳ���
	private String mVenuesImage;//����ͼƬ·��,��ֵ�Ǵ���һҳ�洫�ݹ����Ĳ���
	private String mVenuesName;//��������,��ֵ�Ǵ���һҳ�洫�ݹ����Ĳ���
	
	public Calendar time=Calendar.getInstance(Locale.CHINA);
	public SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DatePicker datePicker;
	private TimePicker timePicker; 
	private AlertDialog dialog;
	
	
	//֧����ʶ
	private static final int SDK_PAY_FLAG=1;
	//����ն��豸�Ƿ����֧������֤�˻�
	private static final int SDK_CHECK_FLAG = 2;
	
	
	/** ������Ϣ������*/
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x11://���߳����淵�ص����ݼ�
				DataSet=(List<GetAllMakeBean>) msg.obj;
				initSpinner();//����Spinner�ؼ���������
				adapter_sporttype.notifyDataSetChanged(); 
				break; 
			case 0x22://���ݻ�ȡΪ����
				Log.i("StadiumOrder","����ԤԼ�����ȡ�˶�����Ϊ����,��ע��!");
				break;
			case 0x33://���յ���ӳ���ԤԼ�ɹ��ı�ʶ
				String result=(String) msg.obj;
				Log.i(TAG,"����ԤԼ��ӳɹ�,���ص���֤��Ϊ:"+result); 
				
				//���������ɹ�֮��ͻ���ת���ҵ�ԤԼ�б�ҳ��
//				Intent _jump=new Intent(StadiumOrder.this,MyOrder.class);
//				startActivity(_jump); 
				break;
			case 0x44://���յ���ӳ���ԤԼʧ�ܱ�ʶ
				Toast.makeText(StadiumOrder.this, "ԤԼʧ��,��֤��:null", 0).show(); 
				break; 
			case SDK_PAY_FLAG://����֧����ʶ
				String param=(String) msg.obj;
				if(TextUtils.isEmpty(param)){
					Log.i(TAG, "֧�����ɹ�,���ʵ");
					return;
				}
				PayResult payResult=new PayResult(param); 
				// ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
				String resultInfo = payResult.getResult();
				Log.i(TAG,"��֧�����������˷��صĽ����:"+resultInfo);
				//��ȡ��֧����״̬
				String resultStatus = payResult.getResultStatus();
				Log.i(TAG,"��֧�����������˷��ص�״̬��:"+resultStatus);

				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(StadiumOrder.this, "֧���ɹ�",Toast.LENGTH_SHORT).show();
					//֧���ɹ�֮����ת���ҵ�ԤԼҳ��
					Intent _jump_=new Intent(StadiumOrder.this,MyOrder.class);
					startActivity(_jump_);
					finish();
				} else {
					// �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
					// ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(StadiumOrder.this, "֧�����ȷ����",Toast.LENGTH_SHORT).show(); 
						Log.i(TAG,"����֧�����ɹ�,֧�����ȷ����");
					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						Log.i(TAG,"����֧��ʧ����");
						Toast.makeText(StadiumOrder.this, "֧��ʧ��",Toast.LENGTH_SHORT).show(); 
					}
				} 
				break; 
			case SDK_CHECK_FLAG://�����Ƿ����֧������֤�˻�
				Log.i(TAG,"���֧������֤�˻����Ϊ:"+(Boolean)msg.obj);
				break; 
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		mVenuesID=intent.getIntExtra("VenuesID", -1);
		mVenuesImage=intent.getStringExtra("VenuesImage");
		mVenuesName=intent.getStringExtra("VenuesName");
		
		setContentView(R.layout.stadium_order);
		conditionQuery();//�����̼߳����˶���������
		initView();  
	}
	
	/** ��ҳ���ϵ�����������г�ʼ��*/
	public void initView(){
		stadium_order_picture=(ImageView) findViewById(R.id.stadium_order_pictures);
		stadium_order_back_arrow=(ImageView) findViewById(R.id.stadium_order_back_arrow);
		stadium_names=(TextView) findViewById(R.id.stadium_names);
		stadium_sporttypes=(Spinner) findViewById(R.id.stadium_sporttypes);
		stadium_moneys=(TextView) findViewById(R.id.stadium_moneys);
		stadium_order_names=(EditText) findViewById(R.id.stadium_order_names);
		stadium_order_time=(EditText) findViewById(R.id.stadium_order_time);
		stadium_order_pays=(Button) findViewById(R.id.stadium_order_pays);
		stadium_order_calculator=(ImageView) findViewById(R.id.stadium_order_calculator);
		
		stadium_order_back_arrow.setOnClickListener(this);
		stadium_order_pays.setOnClickListener(this);  
		stadium_order_calculator.setOnClickListener(this);//ԤԼʱ��
		
		//����ͼƬ
		if(!TextUtils.isEmpty(mVenuesImage)){
			ImageLoader.getInstance().displayImage(WebServiceUtils.IMAGE_URL+mVenuesImage,stadium_order_picture, 
					AppApplication.options, new SimpleImageLoadingListener(),new ImageLoadingProgressListener(){ 
						@Override
						public void onProgressUpdate(String arg0, View view, int current, int total) {
							Log.i(TAG,"��������ͼƬ�첽���ص�����:total="+total+",current="+current); 
						} 
					});
		}
		//���ó�������
		if(!TextUtils.isEmpty(mVenuesName)){
			stadium_names.setText(mVenuesName);
		}
	}
	
	
	/** ��Spinner�ؼ���������*/
	public void initSpinner(){  
		List<String> data=new ArrayList<String>(); 
		for (GetAllMakeBean amb : DataSet) {
			data.add(amb.getProjectName()); //���˶����ʹ����ݼ�����ȡ������ŵ���������
		}
		adapter_sporttype=new ArrayAdapter<String>(
		StadiumOrder.this, 
		R.layout.custom_empty_textview, 
		data);
		stadium_sporttypes.setPrompt("��ѡ���˶�����");//����Spinner�ı���
		//���˶����͵�Spinner����������
		stadium_sporttypes.setAdapter(adapter_sporttype);
		stadium_sporttypes.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { 
				Log.i("�û��������б���ѡ�����:",DataSet.get(position).getProjectName());
				mSportType=DataSet.get(position).getProjectName();
				mSportTypeID=DataSet.get(position).getProjectID();
				stadium_moneys.setText(DataSet.get(position).getMakePrice()+" (RMB)");
			} 
			@Override
			public void onNothingSelected(AdapterView<?> parent) { }  
		}); 
	} 
	
	/** ����һ����ϵĶԻ���,������*/
	public AlertDialog dateTimePickerDialog(final EditText dataView){
		LinearLayout dateTimeLayout=(LinearLayout) LayoutInflater.from(StadiumOrder.this).inflate(R.layout.custom_date_time_selected, null);	
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
		
		dialog=new AlertDialog.Builder(StadiumOrder.this)
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
	
	
	/** �����߳̽��м�������*/
	public void conditionQuery() {
		mProgressDialog=new ProgressDialog(StadiumOrder.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("ҳ��������...");
		mProgressDialog.setIndeterminate(true); 
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
		new Thread(){    
			public void run() {
				try { 
					JSONObject object=new JSONObject();
					object.put("VenuesID", mVenuesID);
					List<GetAllMakeBean> results=WebGetAllMake.getGetGetAllMakeData(object.toString());
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
				} finally{
					mProgressDialog.dismiss();
				}
			};
		}.start();
	} 

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stadium_order_back_arrow://���ذ�ť
			finish();
			break; 
		case R.id.stadium_order_pays://���ť  
			orderOperation(); //���뵽ԤԼ����
			break;
		case R.id.stadium_order_calculator://ԤԼʱ��
			dateTimePickerDialog(stadium_order_time);
			break;
		} 
	}
	
	/** ������������ַ���,ʹ��������ʽ��ȡ�����ݲ��ַ���
	 * ����:1233Ԫ,����1233 
	 */
	private int dataParse(String result) {
		int number=0;
		if(!TextUtils.isEmpty(result)){ 
			String regEx="[0-9\\.]+";
			Pattern p=Pattern.compile(regEx);
			Matcher m=p.matcher(result); 
			while(m.find()){
				System.out.println(TAG+",number="+Integer.valueOf(m.group()));
				number=Integer.valueOf(m.group()).intValue();  
			} 
		} 
		return number;
	}
	
	/** ���û������ԤԼ��ť֮������÷���*/
	public void orderOperation(){
		final String money=stadium_moneys.getText().toString().trim();
		final String names=stadium_order_names.getText().toString().trim();
		final String times=stadium_order_time.getText().toString().trim();
		
		if(TextUtils.isEmpty(mSportType) && mSportTypeID==-1){
			Toast.makeText(StadiumOrder.this, "��ѡ���˶�����", 0).show();return;
		} 
		if(TextUtils.isEmpty(money)){
			Toast.makeText(StadiumOrder.this, "���ݷ���δ��,������ѡ���˶�����", 0).show();return;
		}
		if(TextUtils.isEmpty(names)){
			Toast.makeText(StadiumOrder.this, "����д����", 0).show();return;
		}
		if(TextUtils.isEmpty(times)){
			Toast.makeText(StadiumOrder.this, "����дԤԼʱ��", 0).show();return;
		}
		if(mVenuesID==-1){
			Log.i("Invalid params","����һҳ�洫�ݹ����ĳ���ID��Ч,��ȷ��");return ;
		} 
		
		mPayTypeID=0;//��������ΪĬ��ֵ,��һ���ǳ���Ҫ
		
		new AlertDialog.Builder(StadiumOrder.this)
		.setTitle("��ѡ��֧������")  
		.setSingleChoiceItems(mPayType, 0, new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {  
				mPayTypeID=which;
			}
		})
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("ȷ��",which+",mPayTypeID="+mPayTypeID);  
				
				switch(mPayTypeID){
				case 0://��ת��֧����֧��
					//���ý��г���ԤԼ����ķ���
					venuesOrder(money,names,times); 
					break;
				case 1://��ת��΢��֧��
					Toast.makeText(StadiumOrder.this, "΢��֧��������δ����!",0).show();
					break;
				} 
				//�������֮���ٰ�ֵ���ó�Ĭ��״̬
				stadium_order_names.setText("");
				stadium_order_time.setText(""); 
			}
		})
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) { 
				Log.i("ȡ��",which+"");
			}
		}).show(); 
	}
	
	/** ���û������ȷ����ť֮������˷�������������ҵ��
	 *	���money ��Ҫʹ�� dataParse()��������������ʽ��֤
	 */
	private void venuesOrder(final String money,final String names,final String times){ 
		Log.i("��ǰ�û���д����Ϣ��:","����ID="+mVenuesID+",�˶�����ID="+mSportTypeID+
				",UserID="+UserGlobal.UserID+",money="+dataParse(money)+
				",MakeUserName="+names+",MakeTime="+times+",PayType="+mPayType[mPayTypeID]); 
		new Thread(){
			public void run() {
				JSONObject object=new JSONObject(); 
				try {
					object.put("VenuesID", mVenuesID);
					object.put("ProductID", mSportTypeID);
					object.put("UserID", UserGlobal.UserID);
					object.put("MakeUserName", names);
					object.put("MakeTime", times);
					object.put("PayType", mPayType[mPayTypeID]); 
					//֧������
					//��һ��������:֧������Ʒ����(�˶����͵�ID)
					//�ڶ���������:֧������Ʒ����
					//������������:֧������Ʒ���
					pay(mSportTypeID+"",ToolsHome.getSportName(mSportTypeID),String.valueOf(dataParse(money)));
 
					
					/**�˲����з������ݵ��������˶Ա������ݱ������*/ 
					String result=WebAddMake.getAddMakeData(object.toString());
					if(!TextUtils.isEmpty(result)){
						Message msg=new Message();
						msg.what=0x33;//��ʶ��ӳ���ԤԼ�ɹ�
						msg.obj=result;//������ӳɹ�����֤��
						mHandler.sendMessage(msg);
					}else{
						mHandler.sendEmptyMessage(0x44);//������ӳ���ԤԼʧ�ܱ�ʶ
					} 
				} catch (JSONException e) { 
					e.printStackTrace();
				} 
			};
		}.start(); 

	}
	 
	
	/** ��ȡ��֧����SDK�汾��*/
	public String getSDKVersion(){
		PayTask payTask=new PayTask(StadiumOrder.this);
		return payTask.getVersion(); 
	}
	
	/** ��ѯ�ն��豸�Ƿ����֧������֤�˻�*/
	public void checkAccount(){
		Runnable checkRun=new Runnable(){ 
			@Override
			public void run() { 
				//����PayTask����
				PayTask pay=new PayTask(StadiumOrder.this);
				boolean result=pay.checkAccountIfExist();
				Message msg=new Message();
				msg.what=SDK_CHECK_FLAG;
				msg.obj=result;
				mHandler.sendMessage(msg);//���ͼ���ն��豸�Ƿ����֧������֤�ʻ���ʶ 
			} 
		};
		new Thread(checkRun).start();
	}
	
	/** �����̻�������,Ӧ����Ψһ���*/
	public String getOrderCode(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());//����������ʱ����
		Date date=new Date();
		String key=sdf.format(date);
		
		Random r = new Random();
		key=key+r.nextInt(10);  
		return key; 
	}
	
	/** �Զ�����Ϣ����ǩ��
	 *@param ��һ������content:��Ҫǩ��������
	 *@param �ڶ��������̻�˽Կ
	 */
	public String sign(String content){
		return SignUtils.sign(content, AliPayTools.RSA_PRIVATE); 
	}
	
	/** ��ȡǩ����ʽ*/
	public String getSign(){
		return "sign_type=\"RSA\"";
	}
	
	/** ����������Ϣ
	 *@param subject ��Ʒ����
	 *@param body ��Ʒ����
	 *@param price ��Ʒ���
	 */
	public String getOrderInfo(String subject,String body,String price){
		StringBuilder sb=new StringBuilder();
		// ǩԼ���������ID
		sb.append("partner=" + "\"" + AliPayTools.PARTNER + "\"");
		// ǩԼ����֧�����˺�
		sb.append("&seller_id=" + "\"" + AliPayTools.SELLER + "\"");
		// �̻���Ӧ��Ʊ����Ψһ������
		sb.append("&out_trade_no=" + "\"" + getOrderCode() + "\"");
		// ��Ʒ����
		sb.append("&subject=" + "\"" + subject + "\"");
		// ��Ʒ����
		sb.append("&body=" + "\"" + body + "\"");
		// ��Ʒ���
		sb.append("&total_fee=" + "\"" + price + "\"");
		// ����ӿ����ƣ� �̶�ֵ
		sb.append("&service=\"mobile.securitypay.pay\"");
		// �������첽֪ͨҳ��·��
		sb.append("&notify_url=" + "\"" + "http://www.laidong8.com" + "\"");
		// ֧�����ͣ� �̶�ֵ
		sb.append("&payment_type=\"1\"");
		// �������룬 �̶�ֵ
		sb.append("&_input_charset=\"utf-8\"");
		// ����δ����׵ĳ�ʱʱ��
		// Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
		// ȡֵ��Χ��1m��15d��
		// m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
		// �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
		sb.append("&it_b_pay=\"30m\"");
		
		// extern_tokenΪ���������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
		// sb.append("&extern_token=" + "\"" + extern_token + "\"");
		
		// ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
		// sb.append("&return_url=\"m.alipay.com\"");
		
		// �������п�֧���������ô˲���������ǩ���� �̶�ֵ ����ҪǩԼ���������п����֧��������ʹ�ã�
		// sb.append("&paymethod=\"expressGateway\"");
		 
//		return sb.toString(); 
		Log.i(TAG,"������ϢΪ��"+sb.toString());
		return new String(sb);
	}
	
	/** ����֧����SDK����֧������*/
	public void pay(String subject,String body,String price){
		//���ɶ���
		String orderInfo=getOrderInfo(subject,body,price);
		//�Զ�����Ϣ��RSAǩ��
		String sign=sign(orderInfo);
		// �����sign ��URL����
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace();
			Log.i(TAG,"֧�����ڻ�ȡ��ǩ��ʱ�����쳣��!");
		}
		
		// �����ķ���֧���������淶�Ķ�����Ϣ
		final String payInfo=orderInfo + "&sign=\"" + sign + "\"&" + getSign();
		Log.i(TAG, "��֧��֮ǰ���ɵ�����������Ϣ����:"+payInfo);
		
		Runnable payRunnable=new Runnable(){ 
			@Override
			public void run() { 
				// ����PayTask ����
				PayTask alipay = new PayTask(StadiumOrder.this);
				// ����֧���ӿڣ���ȡ֧�����
				String result = alipay.pay(payInfo); 
				
				Message msg=new Message();
				msg.what=SDK_PAY_FLAG;
				msg.obj=result;
				mHandler.sendMessage(msg);
			} 
		};
		// �����첽����
		new Thread(payRunnable).start();
	}
	

}
