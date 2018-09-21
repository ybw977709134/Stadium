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
import com.wqp.webservice.entity.GetActInfoBean;

/** 通过该方法可以获取到Web端方法名为:GetArtInfo返回的Json格式的字符串*/
public class WebGetArtInfo {
	
	private static final String TAG = "WebGetArtInfo";

	/** 从Web端获取指导信息
	 * @param str 如果不需要填写就写""即可,否则可以写SQL语句
	 *@return <returns>返回DataSet 返回活动信息 Json格式</returns>;从web端解析的数据封装成JavaBean以集合的形式返回 
	 */
	public static List<GetActInfoBean> getGetArtInfoData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETARTINFO;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETARTINFO);
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
				String result=soap.getProperty("GetArtInfoResult").toString();
				Log.i(TAG,"数据集:"+result);
				return ParseGetArtInfo(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/** 根据输入的JSON格式字符串进行解析成Java对象,并封装成List集合进行返回*/
	public static List<GetActInfoBean> ParseGetArtInfo(String result) throws Exception{  
		List<GetActInfoBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"长度为:"+len);
			if(len>0){
				data=new ArrayList<GetActInfoBean>();
				for(int i=0;i<len;i++){
					JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
					GetActInfoBean ai=new GetActInfoBean();
					ai.setActid(tempJSONObject.getInt("ActID"));
					ai.setActtypeid(tempJSONObject.getInt("ActTypeID"));
					ai.setSporttype(tempJSONObject.getInt("SportType"));
					ai.setActtitle(tempJSONObject.getString("ActTitle"));
					ai.setVenuesname(tempJSONObject.getString("VenuesName"));
					ai.setVenuescost(tempJSONObject.getString("VenuesCost"));
					ai.setPubimg(tempJSONObject.getString("PubImg")); //解析图片地址
					ai.setStarttime(dateParse(tempJSONObject.get("StartTime").toString()));//解析开始时间	 
					ai.setEndtime(dateParse(tempJSONObject.get("EndTime").toString()));//解析结束时间
					ai.setApplyendtime(dateParse(tempJSONObject.get("ApplyEndTime").toString()));//解析报名截止日期	 
					ai.setTakecount(tempJSONObject.getInt("TakeCount"));
					ai.setRefereecount(tempJSONObject.getInt("RefereeCount"));
					ai.setGuidecount(tempJSONObject.getInt("GuideCount"));
					ai.setActcon(tempJSONObject.getString("ActCon"));
					ai.setActnotice(tempJSONObject.getString("ActNotice"));
					ai.setCreateuser(tempJSONObject.getInt("CreateUser"));
					ai.setCreatetime(dateParse(tempJSONObject.get("CreateTime").toString())); //解析发布时间 
					ai.setHotspot(tempJSONObject.getBoolean("HotSpot"));
					ai.setAdddigest(tempJSONObject.getBoolean("AddDigest"));
					ai.setBrowsecount(tempJSONObject.getInt("BrowseCount"));
					ai.setState(tempJSONObject.getString("State"));
					ai.setActusertype(tempJSONObject.getString("ActUserType"));
					Log.i(TAG,"活动的图片地址是:"+ai.getPubimg());
					data.add(ai);
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
