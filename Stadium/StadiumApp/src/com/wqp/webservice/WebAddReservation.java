package com.wqp.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
 

import com.wqp.stadiumapp.utils.WebServiceUtils;

/** 通过该方法可以获取到Web端方法名为:AddReservation返回的Json格式的字符串*/
public class WebAddReservation {
	
	/**
	 * 根据输入的参数从Web端返回Json格式的字符串数据
	 * @param str <param name="str">SportType 运动类型;ActTitle 活动标题;VenuesName 使用场馆;
	 * VenuesCost 场馆费用;StartTime 开始时间;EndTime 结束时间;ApplyEndTime 报名截止日期;
	 * TakeCount 参与人数活动上限;RefereeCount 裁判人数;GuideCount 指导员人数;
	 * ActCon 活动具体内容;ActNotice 活动公告; CreateUser 创建人;Json格式</param> 
	 * @return <returns>返回AddReservation 添加活动信息是否成功  Json格式</returns>
	 */
	public static boolean getAddReservationData(String str){
		
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.ADDRESERVATION;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.ADDRESERVATION);
		soapObject.addProperty("str",str);//设置参数1 
		
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
				String result=soap.getProperty("AddReservationResult").toString(); 
				return ParseAddUser(result);
//				Log.i("给服务端添加完活动数据后返回的结果是:",result);
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/** 解析添加活动之后的返回值 */
	private static boolean ParseAddUser(String result){  
		try {
			JSONObject jsonObject=new JSONObject(result);
			return jsonObject.getBoolean("AddReservation");
		} catch (JSONException e) { 
			e.printStackTrace();
		} 
		return false;
	}
}
