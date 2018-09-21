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

/** 场馆预约*/
public class StadiumOrder extends Activity implements OnClickListener {
	private String TAG="StadiumOrder";
	private ImageView stadium_order_picture,stadium_order_back_arrow,stadium_order_calculator;
	private Spinner stadium_sporttypes;
	private TextView stadium_names,stadium_moneys;
	private EditText stadium_order_names,stadium_order_time;
	private Button stadium_order_pays;
	private ProgressDialog mProgressDialog;//进度对话框
	private String[] mPayType={"支付宝支付","微信支付"}; 
	private int mPayTypeID=0;//用于标识当前用户选择的支付方式编号,默认为0表示支付宝支付,1表示微信支付
	
	private List<GetAllMakeBean> DataSet;//返回的数据集
	private ArrayAdapter<String> adapter_sporttype;//运动类型Spinner适配器 
	private String mSportType=null;//用于存放用户选择的运动类型
	private int mSportTypeID=-1;//用于存放用户选择的运动类型ID
	private int mVenuesID;//场馆ID号,该ID值是从上一页面传递过来的参数
	private String mVenuesImage;//场馆图片路径,该值是从上一页面传递过来的参数
	private String mVenuesName;//场馆名称,该值是从上一页面传递过来的参数
	
	public Calendar time=Calendar.getInstance(Locale.CHINA);
	public SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DatePicker datePicker;
	private TimePicker timePicker; 
	private AlertDialog dialog;
	
	
	//支付标识
	private static final int SDK_PAY_FLAG=1;
	//检查终端设备是否存在支付宝认证账户
	private static final int SDK_CHECK_FLAG = 2;
	
	
	/** 定义消息管理器*/
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x11://从线程里面返回的数据集
				DataSet=(List<GetAllMakeBean>) msg.obj;
				initSpinner();//调用Spinner控件加载数据
				adapter_sporttype.notifyDataSetChanged(); 
				break; 
			case 0x22://数据获取为空了
				Log.i("StadiumOrder","场馆预约界面获取运动类型为空了,请注意!");
				break;
			case 0x33://接收到添加场馆预约成功的标识
				String result=(String) msg.obj;
				Log.i(TAG,"场馆预约添加成功,返回的验证码为:"+result); 
				
				//当都操作成功之后就会跳转到我的预约列表页面
//				Intent _jump=new Intent(StadiumOrder.this,MyOrder.class);
//				startActivity(_jump); 
				break;
			case 0x44://接收到添加场馆预约失败标识
				Toast.makeText(StadiumOrder.this, "预约失败,验证码:null", 0).show(); 
				break; 
			case SDK_PAY_FLAG://接收支付标识
				String param=(String) msg.obj;
				if(TextUtils.isEmpty(param)){
					Log.i(TAG, "支付不成功,请核实");
					return;
				}
				PayResult payResult=new PayResult(param); 
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				Log.i(TAG,"从支付宝服务器端返回的结果是:"+resultInfo);
				//获取到支付的状态
				String resultStatus = payResult.getResultStatus();
				Log.i(TAG,"从支付宝服务器端返回的状态是:"+resultStatus);

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(StadiumOrder.this, "支付成功",Toast.LENGTH_SHORT).show();
					//支付成功之生跳转到我的预约页面
					Intent _jump_=new Intent(StadiumOrder.this,MyOrder.class);
					startActivity(_jump_);
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(StadiumOrder.this, "支付结果确认中",Toast.LENGTH_SHORT).show(); 
						Log.i(TAG,"本次支付不成功,支付结果确认中");
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Log.i(TAG,"本次支付失败了");
						Toast.makeText(StadiumOrder.this, "支付失败",Toast.LENGTH_SHORT).show(); 
					}
				} 
				break; 
			case SDK_CHECK_FLAG://接收是否存在支付宝认证账户
				Log.i(TAG,"检测支付宝认证账户结果为:"+(Boolean)msg.obj);
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
		conditionQuery();//开启线程加载运动类型数据
		initView();  
	}
	
	/** 对页面上的所有组件进行初始化*/
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
		stadium_order_calculator.setOnClickListener(this);//预约时间
		
		//设置图片
		if(!TextUtils.isEmpty(mVenuesImage)){
			ImageLoader.getInstance().displayImage(WebServiceUtils.IMAGE_URL+mVenuesImage,stadium_order_picture, 
					AppApplication.options, new SimpleImageLoadingListener(),new ImageLoadingProgressListener(){ 
						@Override
						public void onProgressUpdate(String arg0, View view, int current, int total) {
							Log.i(TAG,"场馆详情图片异步加载的数据:total="+total+",current="+current); 
						} 
					});
		}
		//设置场馆名称
		if(!TextUtils.isEmpty(mVenuesName)){
			stadium_names.setText(mVenuesName);
		}
	}
	
	
	/** 对Spinner控件加载数据*/
	public void initSpinner(){  
		List<String> data=new ArrayList<String>(); 
		for (GetAllMakeBean amb : DataSet) {
			data.add(amb.getProjectName()); //把运动类型从数据集当中取出来存放到集合里面
		}
		adapter_sporttype=new ArrayAdapter<String>(
		StadiumOrder.this, 
		R.layout.custom_empty_textview, 
		data);
		stadium_sporttypes.setPrompt("请选择运动类型");//设置Spinner的标题
		//给运动类型的Spinner设置适配器
		stadium_sporttypes.setAdapter(adapter_sporttype);
		stadium_sporttypes.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { 
				Log.i("用户从下拉列表中选择的是:",DataSet.get(position).getProjectName());
				mSportType=DataSet.get(position).getProjectName();
				mSportTypeID=DataSet.get(position).getProjectID();
				stadium_moneys.setText(DataSet.get(position).getMakePrice()+" (RMB)");
			} 
			@Override
			public void onNothingSelected(AdapterView<?> parent) { }  
		}); 
	} 
	
	/** 生成一个组合的对话框,并返回*/
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
		
		dialog=new AlertDialog.Builder(StadiumOrder.this)
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
	
	
	/** 开启线程进行加载数据*/
	public void conditionQuery() {
		mProgressDialog=new ProgressDialog(StadiumOrder.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("页面载入中...");
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
						msg.what=0x11;//标识正常获取到数据了
						msg.obj=results;
						mHandler.sendMessage(msg); 
						Log.i(TAG,"Web端线程加载数据结束,我已经发送数据到Handler了");
					}else{
						mHandler.sendEmptyMessage(0x22);//发送标识获取到的数据为空
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
		case R.id.stadium_order_back_arrow://返回按钮
			finish();
			break; 
		case R.id.stadium_order_pays://付款按钮  
			orderOperation(); //进入到预约方法
			break;
		case R.id.stadium_order_calculator://预约时间
			dateTimePickerDialog(stadium_order_time);
			break;
		} 
	}
	
	/** 输入带有数字字符串,使用正则表达式提取出数据部分返回
	 * 例如:1233元,返回1233 
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
	
	/** 当用户点击了预约按钮之后会进入该方法*/
	public void orderOperation(){
		final String money=stadium_moneys.getText().toString().trim();
		final String names=stadium_order_names.getText().toString().trim();
		final String times=stadium_order_time.getText().toString().trim();
		
		if(TextUtils.isEmpty(mSportType) && mSportTypeID==-1){
			Toast.makeText(StadiumOrder.this, "请选择运动类型", 0).show();return;
		} 
		if(TextUtils.isEmpty(money)){
			Toast.makeText(StadiumOrder.this, "场馆费用未定,请重新选择运动类型", 0).show();return;
		}
		if(TextUtils.isEmpty(names)){
			Toast.makeText(StadiumOrder.this, "请填写姓名", 0).show();return;
		}
		if(TextUtils.isEmpty(times)){
			Toast.makeText(StadiumOrder.this, "请填写预约时间", 0).show();return;
		}
		if(mVenuesID==-1){
			Log.i("Invalid params","从上一页面传递过来的场馆ID无效,请确认");return ;
		} 
		
		mPayTypeID=0;//重新设置为默认值,这一步非常重要
		
		new AlertDialog.Builder(StadiumOrder.this)
		.setTitle("请选择支付类型")  
		.setSingleChoiceItems(mPayType, 0, new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {  
				mPayTypeID=which;
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("确定",which+",mPayTypeID="+mPayTypeID);  
				
				switch(mPayTypeID){
				case 0://跳转到支付宝支付
					//调用进行场馆预约处理的方法
					venuesOrder(money,names,times); 
					break;
				case 1://跳转到微信支付
					Toast.makeText(StadiumOrder.this, "微信支付功能暂未开发!",0).show();
					break;
				} 
				//操作完成之后再把值设置成默认状态
				stadium_order_names.setText("");
				stadium_order_time.setText(""); 
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) { 
				Log.i("取消",which+"");
			}
		}).show(); 
	}
	
	/** 当用户点击了确定按钮之后会进入此方法处理订单请求业务
	 *	金额money 需要使用 dataParse()方法进行正则表达式验证
	 */
	private void venuesOrder(final String money,final String names,final String times){ 
		Log.i("当前用户填写的信息是:","场馆ID="+mVenuesID+",运动类型ID="+mSportTypeID+
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
					//支付操作
					//第一个参数是:支付的商品名称(运动类型的ID)
					//第二个参数是:支付的商品描述
					//第三个参数是:支付的商品金额
					pay(mSportTypeID+"",ToolsHome.getSportName(mSportTypeID),String.valueOf(dataParse(money)));
 
					
					/**此步进行发送数据到服务器端对本次数据保存操作*/ 
					String result=WebAddMake.getAddMakeData(object.toString());
					if(!TextUtils.isEmpty(result)){
						Message msg=new Message();
						msg.what=0x33;//标识添加场馆预约成功
						msg.obj=result;//返回添加成功的验证码
						mHandler.sendMessage(msg);
					}else{
						mHandler.sendEmptyMessage(0x44);//发送添加场馆预约失败标识
					} 
				} catch (JSONException e) { 
					e.printStackTrace();
				} 
			};
		}.start(); 

	}
	 
	
	/** 获取到支付宝SDK版本号*/
	public String getSDKVersion(){
		PayTask payTask=new PayTask(StadiumOrder.this);
		return payTask.getVersion(); 
	}
	
	/** 查询终端设备是否存在支付宝认证账户*/
	public void checkAccount(){
		Runnable checkRun=new Runnable(){ 
			@Override
			public void run() { 
				//构造PayTask对象
				PayTask pay=new PayTask(StadiumOrder.this);
				boolean result=pay.checkAccountIfExist();
				Message msg=new Message();
				msg.what=SDK_CHECK_FLAG;
				msg.obj=result;
				mHandler.sendMessage(msg);//发送检查终端设备是否存在支付宝认证帐户标识 
			} 
		};
		new Thread(checkRun).start();
	}
	
	/** 生成商户订单号,应属于唯一编号*/
	public String getOrderCode(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());//生成年月日时分秒
		Date date=new Date();
		String key=sdf.format(date);
		
		Random r = new Random();
		key=key+r.nextInt(10);  
		return key; 
	}
	
	/** 对订单信息进行签名
	 *@param 第一个参数content:需要签名的内容
	 *@param 第二个参数商户私钥
	 */
	public String sign(String content){
		return SignUtils.sign(content, AliPayTools.RSA_PRIVATE); 
	}
	
	/** 获取签名方式*/
	public String getSign(){
		return "sign_type=\"RSA\"";
	}
	
	/** 创建订单信息
	 *@param subject 商品名称
	 *@param body 商品描述
	 *@param price 商品金额
	 */
	public String getOrderInfo(String subject,String body,String price){
		StringBuilder sb=new StringBuilder();
		// 签约合作者身份ID
		sb.append("partner=" + "\"" + AliPayTools.PARTNER + "\"");
		// 签约卖家支付宝账号
		sb.append("&seller_id=" + "\"" + AliPayTools.SELLER + "\"");
		// 商户对应此票订单唯一订单号
		sb.append("&out_trade_no=" + "\"" + getOrderCode() + "\"");
		// 商品名称
		sb.append("&subject=" + "\"" + subject + "\"");
		// 商品详情
		sb.append("&body=" + "\"" + body + "\"");
		// 商品金额
		sb.append("&total_fee=" + "\"" + price + "\"");
		// 服务接口名称， 固定值
		sb.append("&service=\"mobile.securitypay.pay\"");
		// 服务器异步通知页面路径
		sb.append("&notify_url=" + "\"" + "http://www.laidong8.com" + "\"");
		// 支付类型， 固定值
		sb.append("&payment_type=\"1\"");
		// 参数编码， 固定值
		sb.append("&_input_charset=\"utf-8\"");
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		sb.append("&it_b_pay=\"30m\"");
		
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// sb.append("&extern_token=" + "\"" + extern_token + "\"");
		
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// sb.append("&return_url=\"m.alipay.com\"");
		
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// sb.append("&paymethod=\"expressGateway\"");
		 
//		return sb.toString(); 
		Log.i(TAG,"订单信息为："+sb.toString());
		return new String(sb);
	}
	
	/** 调用支付宝SDK进行支付操作*/
	public void pay(String subject,String body,String price){
		//生成订单
		String orderInfo=getOrderInfo(subject,body,price);
		//对订单信息做RSA签名
		String sign=sign(orderInfo);
		// 仅需对sign 做URL编码
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace();
			Log.i(TAG,"支付宝在获取到签名时产生异常了!");
		}
		
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo=orderInfo + "&sign=\"" + sign + "\"&" + getSign();
		Log.i(TAG, "在支付之前生成的完整订单信息如下:"+payInfo);
		
		Runnable payRunnable=new Runnable(){ 
			@Override
			public void run() { 
				// 构造PayTask 对象
				PayTask alipay = new PayTask(StadiumOrder.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo); 
				
				Message msg=new Message();
				msg.what=SDK_PAY_FLAG;
				msg.obj=result;
				mHandler.sendMessage(msg);
			} 
		};
		// 必须异步调用
		new Thread(payRunnable).start();
	}
	

}
