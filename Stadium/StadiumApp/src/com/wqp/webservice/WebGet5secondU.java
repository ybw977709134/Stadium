package com.wqp.webservice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.entity.Get5SecondUBean;

/**
 * 获得服务端回复用户的操作信息 
 *
 */
public class WebGet5secondU {
	private static final String TAG = "WebGet5secondU";

	/**
	 * 传入普通用户ID查询服务端是否有消息回复
	 * @param str {"UserID":"34"} Json格式数据
	 * @return 查询到的消息
	 */
	public static List<Get5SecondUBean> getGet5secondU(String str){
		//命名空间+方法名称
				String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GET5SECONDU;
				
				/**创建HttpTransportSE对象,参数：服务端的url*/
				HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
				
				/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
				SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GET5SECONDU);
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
						String result=soap.getProperty("Get5secondUResult").toString();
						return ParseGet5SecondU(result); 
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
	private static List<Get5SecondUBean> ParseGet5SecondU(String result) {
		List<Get5SecondUBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"长度为="+len);
			if(len!=0){
				data=new ArrayList<Get5SecondUBean>();
				for(int i=0;i<len;i++){
					JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
					Get5SecondUBean gsu=new Get5SecondUBean();
					gsu.setUserVenMesID(tempJSONObject.getInt("UserVenMesID"));
					gsu.setProID(tempJSONObject.getInt("ProID"));
					gsu.setUserID(tempJSONObject.getInt("UserID"));
					gsu.setCon(tempJSONObject.getString("Con"));
					gsu.setVenuesIDs(tempJSONObject.getInt("VenuesIDs"));
					gsu.setIsVenuesID(tempJSONObject.getInt("IsVenuesID"));	
					gsu.setCreateTime(tempJSONObject.getString("CreateTime"));
					gsu.setPrice((float)tempJSONObject.getDouble("Price"));
					gsu.setProName(tempJSONObject.getString("ProName"));
					gsu.setMakeTime(tempJSONObject.getString("MakeTime")); 
					data.add(gsu);
				}
			}  
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return data!=null ? data : null;
	}
	
	
}
