package com.wqp.webservice;
 

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
 
import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.entity.JoinActResultBean;

/** 通过该方法可以获取到Web端方法名为:JoinAct返回的Json格式的字符串*/
public class WebJoinAct {
	/**
	 * 输入名称进行登陆操作
	 * @param str <param name="str">ActID 活动ID ;UserName 用户名 多人用逗号隔开 Json格式</param>
	 * @return <returns>UserExist 用户登陆是否成功 NoName 查找无此人(UserExist为Ture是为0) Json格式</returns>	
	 */
	public static JoinActResultBean getJoinActData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.JOINACT;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.JOINACT);
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
				String result=soap.getProperty("JoinActResult").toString();
				Log.i("WEB_SERVICE",result);
				return ParseJoinAct(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;//操作失败,没有获取到数据
	}
	
	/** 解析从服务端返回的JoinAct数据
	 * @throws JSONException */
	public static JoinActResultBean ParseJoinAct(String result){ 
		if(result!=null){
			try { 
				JSONObject object=new JSONObject(result);
				JoinActResultBean jrb;
//				Iterator<?> it=object.keys();
//				while(it.hasNext()){
//					String useerexist=it.next().toString();
//					String noname=it.next().toString();
//					String onname=it.next().toString();
//					if(!TextUtils.isEmpty(useerexist)){
//						object.put("UserExist", useerexist.trim());
//					}
//					if(!TextUtils.isEmpty(noname)){
//						object.put("NoName", noname); 
//					}
//					if(!TextUtils.isEmpty(onname)){
//						object.put("OnName", onname);
//					}
//				}
				Log.i("WebJoinAct","加入应战之后从服务端返回数据解析再次处理得到的数据为:"+object.toString());
				jrb=new JoinActResultBean();
				jrb.setUserExist(object.getBoolean("UserExist"));//本来是boolean类型，却变成了String类型	
				jrb.setNoName(object.getString("NoName"));//本来是Int类型却变成了String类型
				jrb.setOnName(object.getString("OnName"));//本来是Int类型却变成了String类型 
				return jrb;  
			} catch (JSONException e) { 
				e.printStackTrace();
			} 
		}
		return null; 
	}
	
}
