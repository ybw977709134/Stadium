package com.wqp.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;

/** 通过该方法可以获取到Web端方法名为:PWDUpdate返回的Json格式的字符串,此方法属于更新密码操作*/
public class WebPWDUpdate {
	/**
	 * 输入名称进行用户密码更新操作
	 * @param str 新密码
	 */
	public static boolean getPWDUpdateData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.PWDUPDATE;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.PWDUPDATE);
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
				String result=soap.getProperty("PWDUpdateResult").toString();
				Log.i("WEB_SERVICE,更新后的结果是:",result);
				return ParseUserUpdate(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return false;
	}
	
	/** 根据输入的字符串进行解析返回的结果*/
	public static boolean ParseUserUpdate(String result){
		if(result!=null){
			try {
				JSONObject object=new JSONObject(result);
				return object.getBoolean("PWDUpdate"); 
			} catch (JSONException e) { 
				e.printStackTrace();
			} 
		}
		return false;
	}
}
