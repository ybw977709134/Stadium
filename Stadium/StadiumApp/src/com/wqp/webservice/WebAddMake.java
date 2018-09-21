package com.wqp.webservice;
 
 
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE; 

import android.text.TextUtils;

import com.wqp.stadiumapp.utils.WebServiceUtils; 

/** 通过该方法可以添加新预约数据*/
public class WebAddMake {  
	/**
	 * 添加新预约数据
	 * @param { VenuesID:8, ProductID:1, UserID:1, MakeUserName:'张三', MakeTime:'2015-2-19 13:30:00', PayType:'支付宝/微信'}
	 * @return 预定成功返回验证码 { AddMake:'678750' },预定失败返回空字符串 
	 */
	public static String getAddMakeData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.ADDMAKE;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.ADDMAKE);
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
				String result=soap.getProperty("AddMakeResult").toString();
				return ParseAddUser(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/** 解析注册之后的返回值为验证码 */
	private static String ParseAddUser(String result){  
		try {
			JSONObject jsonObject=new JSONObject(result);
			String temp= jsonObject.getString("AddMake");
			if(!TextUtils.isEmpty(temp)){
				return temp;
			}
		} catch (JSONException e) { 
			e.printStackTrace();
		} 
		return null;
	}
	
}
