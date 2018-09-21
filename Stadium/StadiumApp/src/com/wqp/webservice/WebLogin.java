package com.wqp.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;  

import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.entity.GetLoginBean;

/** 通过该方法可以获取到Web端方法名为:Login返回的Json格式的字符串*/
public class WebLogin {
	/**
	 * 输入名称进行登陆操作
	 * @param str
	 */
	public static GetLoginBean getLoginData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.LOGIN;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.LOGIN);
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
				String result=soap.getProperty("LoginResult").toString();
				return ParseLogin(result);  
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/** 根据服务端返回的数据解析之后把数据封装成JavaBean对象返回*/
	private static GetLoginBean ParseLogin(String result){
		Log.i("result=",result); 
		GetLoginBean lb=null;
		try { 
			JSONObject object=new JSONObject(result);
			if(object.getBoolean("UserExist")){
				lb=new GetLoginBean();
				lb.setUserExist(object.getBoolean("UserExist"));//用户是存在
				lb.setUserID(object.getInt("UserID")); //用户的ID
			}  
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return lb;
	}
}
