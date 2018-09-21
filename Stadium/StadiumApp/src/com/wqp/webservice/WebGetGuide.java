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
import com.wqp.webservice.entity.GetGuideBean;

/** 通过该方法可以获取到Web端方法名为:GetGuide返回的Json格式的字符串*/
public class WebGetGuide {
	
	private static final String TAG = "WebGetGuide";

	/** 从Web端获取指导信息*/
	public static List<GetGuideBean> getGetGuideData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETGUIDE;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETGUIDE);
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
				String result=soap.getProperty("GetGuideResult").toString();
				return ParseGetGuide(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/** 根据输入的JSON格式字符串进行解析成Java对象,并封装成List集合进行返回*/
	public static List<GetGuideBean> ParseGetGuide(String result){ 
		List<GetGuideBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"长度为="+len);
			if(len!=0){
				data=new ArrayList<GetGuideBean>();
				for(int i=0;i<len;i++){
					JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
					GetGuideBean gb=new GetGuideBean();
					gb.setSgid(tempJSONObject.getInt("SGID"));
					gb.setTitle(tempJSONObject.getString("Title"));
					gb.setSgtype(tempJSONObject.getString("SGType"));
					gb.setSgcontent(tempJSONObject.getString("SGContent"));
					gb.setUrl(tempJSONObject.getString("URL"));
					gb.setCreater(tempJSONObject.getInt("Creater")); 
					gb.setCreatetime(dateParse(tempJSONObject.get("CreateTime").toString()));//解析指导添加时间
					gb.setWonori(tempJSONObject.getString("WONOri"));
					gb.setWonlock(tempJSONObject.getString("WONLock"));
					gb.setCtr(tempJSONObject.getInt("CTR"));
					gb.setSort(tempJSONObject.getInt("Sort"));
					gb.setStatus(tempJSONObject.getString("Status"));
					gb.setGenre(tempJSONObject.getInt("Grenre")); 
					data.add(gb);
				}
			}  
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return data!=null ? data : null;
	}
	
	/** 输入web端的时间字符串,并转换成本时时间返回 */
	private static String dateParse(String result) { 
		if(result!=null){
			return result.replace('T', ' ');
		} 
		return null;
	}
	
}
