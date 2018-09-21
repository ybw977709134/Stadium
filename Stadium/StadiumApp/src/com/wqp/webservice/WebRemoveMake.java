package com.wqp.webservice; 
 
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE; 
 

import com.wqp.stadiumapp.utils.WebServiceUtils; 

/**通过该方法可以取消预约订单*/
public class WebRemoveMake {  
	/**
	 * 取消预约订单
	 * @param { MakeID:1, BankCode:'6222520617777777', OpenBank:'中国工商银行', BankUserName:'张三' } 
	 * @return 退订成功返回true,失败返回false
	 */
	public static boolean getRemoveMakeData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.REMOVEMAKE;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.REMOVEMAKE);
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
				String result=soap.getProperty("RemoveMakeResult").toString();
				return ParseAddUser(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return false;
	}
	
	/** 解析取消预约之后的返回值*/
	private static boolean ParseAddUser(String result){  
		try {
			JSONObject jsonObject=new JSONObject(result);
			boolean temp= jsonObject.getBoolean("RemoveMake"); 
			return temp;
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return false;  
	}
	
}
