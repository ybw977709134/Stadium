package com.wqp.webservice;
 
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.json.JSONException;
import org.json.JSONObject; 
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.text.TextUtils;
import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils; 

public class WebGetUserOnAct {
	
	private static final String TAG = "WebGetUserOnAct";

	/** 从Web端获取用户已经参加活动ID的信息
	 * @param str 如果不需要填写就写""即可,否则可以写SQL语句
	 *@return <returns>返回DataSet 返回活动信息 Json格式</returns>;从web端解析的数据封装成JavaBean以集合的形式返回 
	 */
	public static List<Integer> getGetUserOnActData(int userid){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETUSERONACT;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETUSERONACT);
		soapObject.addProperty("UserID",userid);//设置web端方法的参数
		
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
				String result=soap.getProperty("GetUserOnActResult").toString();
				Log.i(TAG,"数据集:"+result);
				return ParseGetUserOnActInfo(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/** 根据输入的JSON格式字符串进行解析成Java对象,并封装成List集合进行返回*/
	public static List<Integer> ParseGetUserOnActInfo(String result) throws Exception{   
		try {
			JSONObject jsonObject=new JSONObject(result); 
			String res=jsonObject.getString("UserOnAct");
			 return dateParse(res);
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return null;
	}
	
	/** 输入web端的数字字符串,并转换数字数组 */
	private static List<Integer> dateParse(String result) {
		List<Integer> data=null;
		if(!TextUtils.isEmpty(result)){
			data=new ArrayList<Integer>();
			String regEx="[0-9\\.]+";
			Pattern p=Pattern.compile(regEx);
			Matcher m=p.matcher(result); 
			while(m.find()){
				System.out.println(TAG+",number="+Integer.valueOf(m.group()));
				data.add(Integer.valueOf(m.group()));  
			} 
		} 
		return data;
	}
	
}
