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
import com.wqp.webservice.entity.GetSportsVenuesBean; 

public class WebGetSportsVenues { 
	private static final String TAG = "WebGetSportsVenues";

	/** 根据运动类型 和 场馆区域查询出符合条件的场馆集合*/
	public static List<GetSportsVenuesBean> getGetSportsVenuesData(String str){
		//命名空间+方法名称
				String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETSPORTSVENUES;
				
				/**创建HttpTransportSE对象,参数：服务端的url*/
				HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
				
				/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
				SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETSPORTSVENUES);
				if(str!=null){
					soapObject.addProperty("str",str);//设置web端方法的参数
				} 
				
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
						String result=soap.getProperty("GetSportsVenuesResult").toString();
						return ParseGetSportsVenues(result);
//						Log.i("WEB_SERVICE",result);
					} 
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
	}
	
	private static List<GetSportsVenuesBean> ParseGetSportsVenues(String result) {
		List<GetSportsVenuesBean> data=null;
		if(result!=null){
			try {
				JSONObject object=new JSONObject(result);
				JSONArray jsonArray=object.getJSONArray("ds");
				int len=jsonArray.length();
				Log.i(TAG,"数据长度="+len);
				if(len>0){ 
					data=new ArrayList<GetSportsVenuesBean>();
					for(int i=0;i<len;i++){
						JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
						GetSportsVenuesBean sv=new GetSportsVenuesBean(); 
						sv.setVenuesID(tempJSONObject.getInt("VenuesID"));
						sv.setVenuesName(tempJSONObject.getString("VenuesName")); 
						sv.setAddress(tempJSONObject.getString("Address"));
						sv.setReservePhone(tempJSONObject.getString("ReservePhone")); 
						sv.setRideRoute(tempJSONObject.getString("RideRoute"));  
						sv.setVenuesEnvironment(tempJSONObject.getString("VenuesEnvironment"));  
						sv.setVenuesImager(tempJSONObject.getString("VenuesImager"));//这个获取到的就是图片的Uri路径，需要在适配器里面异步加载图片	 

						data.add(sv); 
					}  
				} 
			} catch (JSONException e) {  e.printStackTrace(); } 
		}
		return data!=null ? data : null;
	}
	
}
