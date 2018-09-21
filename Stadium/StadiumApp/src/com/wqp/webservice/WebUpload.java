package com.wqp.webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;

/** 通过该方法可以获取到Web端方法名为:Upload返回的Json格式的字符串*/
public class WebUpload { 
	/**
	 * 加载数据到服务器端
	 * @param fs
	 * @param fileType
	 * @param UserID
	 */
	public static void getUploadData(byte[] fs, String fileType, String UserID){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.UPLOAD;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.UPLOAD);
		soapObject.addProperty("fs",fs);//设置web端方法的参数1
		soapObject.addProperty("fileType",fileType);//设置web端方法的参数2
		soapObject.addProperty("UserID",UserID);//设置web端方法的参数3
		
		/**使用SOAP协议获得序列化的Envelope
		 * 将SoapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
		 * 参数：SoapEnvelope版本号
		 */
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut=soapObject;
		try{
			/**开始调用远程方法,参数1:命名空间+方法名称,参数2：实例化后的Envelope*/
			httpse.call(soapAction, envelope);
			if(envelope.getResponse()!=null){
				//获取服务器响应返回的SOAP消息
				SoapObject soap=(SoapObject) envelope.bodyIn;
				/**获取返回的数据,参数：服务端返回的方法名(可以在服务端查找)*/
				String result_one=soap.getProperty("UploadResult").toString(); 
				@SuppressWarnings("unused")
				byte[] result_two=(byte[]) soap.getProperty("fs");
				Log.i("WEB_SERVICE",result_one.toString());
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
}
