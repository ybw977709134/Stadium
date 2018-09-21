package com.wqp.webservice;
 
 
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.text.TextUtils;
import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;

/** 通过该方法可以添加新预约数据*/
public class WebAddUserVM {  
	/**
	 * 添加用户选择的场馆信息，提交至服务器端
	 * @param { ProName:空手道, MakeTime:2015-03-12 08:00:00, UserID:1, VenuesIDs:8} 
	 * @return 如果添加成功就返回true,否则false
	 */
	public static boolean getAddUserVMData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.ADDUSERVM;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.ADDUSERVM);
		soapObject.addProperty("str",str);//设置web端方法的参数
		
		/**使用SOAP协议获得序列化的Envelope
		 * 将SoapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
		 * 参数：SoapEnvelope版本号
		 */
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut=soapObject;
		envelope.dotNet=true;
		envelope.setOutputSoapObject(soapObject);
		envelope.encodingStyle="UTF-8";
		try{
			/**开始调用远程方法,参数1:命名空间+方法名称,参数2：实例化后的Envelope*/
			httpse.call(soapAction, envelope);
			if(envelope.getResponse()!=null){
				//获取服务器响应返回的SOAP消息
				SoapObject soap=(SoapObject) envelope.bodyIn;
				/**获取返回的数据,参数：服务端返回的方法名(可以在服务端查找)*/
				String result=soap.getProperty("AddUserVMResult").toString();
				return ParseAddUserVM(result); 
//				Log.i("WebAddUserVM","向该接口添加数据返回的结果是:"+result);
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return false;
	}
	
	/** 解析注册之后的返回值为验证码 */
	private static boolean ParseAddUserVM(String result){
		boolean temp=false;
		try {
			JSONObject jsonObject=new JSONObject(result);
			temp= jsonObject.getBoolean("AddUserVM"); 
		} catch (JSONException e) { 
			e.printStackTrace();
		} 
		return temp;
	}
	
}
