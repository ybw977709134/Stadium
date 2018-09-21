package com.wqp.alipay;
 

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message; 
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

public class PayDemoActivity extends FragmentActivity {

	//锟教伙拷PID
	public static final String PARTNER = "";
	//锟教伙拷锟秸匡拷锟剿猴拷
	public static final String SELLER = "";
	//锟教伙拷私钥锟斤拷pkcs8锟斤拷式
	public static final String RSA_PRIVATE = "";
	//支锟斤拷锟斤拷锟斤拷钥
	public static final String RSA_PUBLIC = "";


	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支锟斤拷锟斤拷锟斤拷锟截此达拷支锟斤拷锟斤拷锟斤拷签锟斤拷锟斤拷锟斤拷锟街э拷锟斤拷锟角╋拷锟斤拷锟较拷锟角┰际敝э拷锟斤拷锟斤拷峁╋拷墓锟皆匡拷锟斤拷锟角�
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 锟叫讹拷resultStatus 为锟斤拷9000锟斤拷锟斤拷锟斤拷支锟斤拷锟缴癸拷锟斤拷锟斤拷锟斤拷状态锟斤拷锟�锟斤拷刹慰锟斤拷涌锟斤拷牡锟�
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayDemoActivity.this, "支锟斤拷锟缴癸拷",
							Toast.LENGTH_SHORT).show();
				} else {
					// 锟叫讹拷resultStatus 为锟角★拷9000锟斤拷锟斤拷锟斤拷锟斤拷锟街э拷锟绞э拷锟�
					// 锟斤拷8000锟斤拷锟斤拷锟街э拷锟斤拷锟斤拷锟斤拷为支锟斤拷锟斤拷锟斤拷原锟斤拷锟斤拷锟较低吃拷锟斤拷诘却锟街э拷锟斤拷锟斤拷确锟较ｏ拷锟斤拷锟秸斤拷锟斤拷锟角凤拷晒锟斤拷苑锟斤拷锟斤拷锟届步通知为准锟斤拷小锟斤拷锟斤拷状态锟斤拷
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayDemoActivity.this, "支锟斤拷锟斤拷锟饺凤拷锟斤拷锟",
								Toast.LENGTH_SHORT).show();

					} else {
						// 锟斤拷锟斤拷值锟酵匡拷锟斤拷锟叫讹拷为支锟斤拷失锟杰ｏ拷锟斤拷锟斤拷锟矫伙拷锟斤拷锟斤拷取锟斤拷支锟斤拷锟斤拷锟斤拷锟斤拷系统锟斤拷锟截的达拷锟斤拷
						Toast.makeText(PayDemoActivity.this, "支锟斤拷失锟斤拷",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayDemoActivity.this, "锟斤拷锟斤拷锟轿拷锟" + msg.obj,Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.pay_main);
	}

	/**
	 * call alipay sdk pay. 锟斤拷锟斤拷SDK支锟斤拷
	 * 
	 */
	public void pay(View v) {
		// 锟斤拷锟斤拷
		String orderInfo = getOrderInfo("锟斤拷锟皆碉拷锟斤拷品", "锟矫诧拷锟斤拷锟斤拷品锟斤拷锟斤拷细锟斤拷锟斤拷", "0.01");

		// 锟皆讹拷锟斤拷锟斤拷RSA 签锟斤拷
		String sign = sign(orderInfo);
		try {
			// 锟斤拷锟斤拷锟絪ign 锟斤拷URL锟斤拷锟斤拷
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 锟斤拷锟斤拷姆锟斤拷支锟斤拷锟斤拷锟斤拷锟斤拷娣讹拷亩锟斤拷锟斤拷锟较�
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 锟斤拷锟斤拷PayTask 锟斤拷锟斤拷
				PayTask alipay = new PayTask(PayDemoActivity.this);
				// 锟斤拷锟斤拷支锟斤拷锟接口ｏ拷锟斤拷取支锟斤拷锟斤拷锟�
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 锟斤拷锟斤拷锟届步锟斤拷锟斤拷
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 锟斤拷询锟秸讹拷锟借备锟角凤拷锟斤拷锟街э拷锟斤拷锟斤拷锟街わ拷嘶锟�
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 锟斤拷锟斤拷PayTask 锟斤拷锟斤拷
				PayTask payTask = new PayTask(PayDemoActivity.this);
				// 锟斤拷锟矫诧拷询锟接口ｏ拷锟斤拷取锟斤拷询锟斤拷锟�
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 锟斤拷取SDK锟芥本锟斤拷
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷息
	 * @param subject:锟斤拷品锟斤拷锟�
	 * @param body:锟斤拷品锟斤拷锟斤拷
	 * @param price:锟斤拷品锟斤拷锟�
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约锟斤拷锟斤拷锟斤拷锟斤拷锟絀D
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约锟斤拷锟斤拷支锟斤拷锟斤拷锟剿猴拷
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 锟教伙拷锟斤拷站唯一锟斤拷锟斤拷锟斤拷
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 锟斤拷品锟斤拷锟�
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 锟斤拷品锟斤拷锟斤拷
		orderInfo += "&body=" + "\"" + body + "\"";

		// 锟斤拷品锟斤拷锟�
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 锟斤拷锟斤拷锟斤拷锟届步通知页锟斤拷路锟斤拷
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

		// 锟斤拷锟斤拷涌锟斤拷锟狡ｏ拷 锟教讹拷值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支锟斤拷锟斤拷锟酵ｏ拷 锟教讹拷值
		orderInfo += "&payment_type=\"1\"";

		// 锟斤拷锟斤拷锟斤拷耄�锟教讹拷值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 锟斤拷锟斤拷未锟斤拷锟筋交锟阶的筹拷时时锟斤拷
		// 默锟斤拷30锟斤拷锟接ｏ拷一锟斤拷锟斤拷时锟斤拷锟矫笔斤拷锟阶就伙拷锟皆讹拷锟斤拷锟截闭★拷
		// 取值锟斤拷围锟斤拷1m锟斤拷15d锟斤拷
		// m-锟斤拷锟接ｏ拷h-小时锟斤拷d-锟届，1c-锟斤拷锟届（锟斤拷锟桔斤拷锟阶猴拷时锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷0锟斤拷乇眨锟斤拷锟�
		// 锟矫诧拷锟斤拷锟斤拷值锟斤拷锟斤拷锟斤拷小锟斤拷悖拷锟�.5h锟斤拷锟斤拷转锟斤拷为90m锟斤拷
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为锟斤拷锟斤拷锟斤拷锟斤拷权锟斤拷取锟斤拷锟斤拷alipay_open_id,锟斤拷锟较此诧拷锟斤拷锟矫伙拷锟斤拷使锟斤拷锟斤拷权锟斤拷锟剿伙拷锟斤拷锟斤拷支锟斤拷
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷螅锟角耙筹拷锟斤拷锟阶拷锟斤拷袒锟街革拷锟揭筹拷锟斤拷路锟斤拷锟斤拷锟缴匡拷
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 锟斤拷锟斤拷锟斤拷锟叫匡拷支锟斤拷锟斤拷锟斤拷锟斤拷锟矫此诧拷锟斤拷锟斤拷锟角╋拷锟�锟教讹拷值 锟斤拷锟斤拷要签约锟斤拷锟斤拷锟斤拷锟斤拷锟叫匡拷锟斤拷锟街э拷锟斤拷锟斤拷锟斤拷锟绞癸拷茫锟�
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 锟斤拷锟斤拷袒锟斤拷锟斤拷锟斤拷牛锟斤拷锟街碉拷锟斤拷袒锟斤拷锟接︼拷锟斤拷锟轿ㄒ伙拷锟斤拷锟斤拷远锟斤拷锟斤拷式锟芥范锟斤拷
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 锟皆讹拷锟斤拷锟斤拷息锟斤拷锟斤拷签锟斤拷
	 * 
	 * @param content
	 *            锟斤拷签锟斤拷锟斤拷息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 锟斤拷取签锟斤拷式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}


