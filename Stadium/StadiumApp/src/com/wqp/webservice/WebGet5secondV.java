package com.wqp.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.wqp.stadiumapp.utils.WebServiceUtils;

/**
 * 获得服务端回复用户的操作信息 
 *
 */
public class WebGet5secondV {
	/**
	 * 传入普通用户ID查询服务端是否有消息回复
	 * @param str {"UserID":"34"} Json格式数据
	 * @return 查询到的消息
	 */
	public static String getGet5secondV(String str){
		//命名空间+方法名称
				String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GET5SECONDV;
				
				/**创建HttpTransportSE对象,参数：服务端的url*/
				HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
				
				/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
				SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GET5SECONDV);
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
						String result=soap.getProperty("Get5secondVResult").toString();
						return ParseGet5SecondV(result); 
					} 
				}catch(Exception e){
					e.printStackTrace();
				} 
				return null;
	}
	
	/**
	 * 根据服务端返回的数据进行解析操作
	 * @param result 数据
	 * @return 解析的结果
	 */
	private static String ParseGet5SecondV(String result) {
		StringBuffer sb = null;
		if(result!=null){
			try {
				sb=new StringBuffer();
				JSONObject object=new JSONObject(result); 
				sb.append(object.getString("MessageState")); 
			} catch (JSONException e) { 
				e.printStackTrace();
			}
		} 
		return sb.toString();
	}
	
	
}
