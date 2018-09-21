package com.wqp.webservice;
   
import java.util.ArrayList; 
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
 

import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.entity.GetVenuesInfoBean;

/** 通过该方法可以获取到Web端方法名为:GetVenues返回的Json格式的字符串*/
public class WebGetVenues {
	private static final String TAG = "WebGetVenues";

	/**
	 * 输入场馆ID
	 * @param VenuesID
	 */
	public static List<GetVenuesInfoBean> getGetVenuesData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETVENUES;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETVENUES);
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
				String result=soap.getProperty("GetVenuesResult").toString();
				return ParseGetVenues(result);
//				Log.i("WEB_SERVICE",result);
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private static List<GetVenuesInfoBean> ParseGetVenues(String result) {
		List<GetVenuesInfoBean> data=null;
		if(result!=null){
			try {
				JSONObject object=new JSONObject(result);
				JSONArray jsonArray=object.getJSONArray("ds");
				int len=jsonArray.length();
				Log.i(TAG,"数据长度="+len);
				if(len>0){ 
					data=new ArrayList<GetVenuesInfoBean>();
					for(int i=0;i<len;i++){
						JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
						GetVenuesInfoBean vi=new GetVenuesInfoBean();
						vi.setVenuesid(tempJSONObject.getInt("VenuesID"));
						vi.setVenuesname(tempJSONObject.getString("VenuesName"));
						vi.setVenuesarea(tempJSONObject.getString("VenuesArea"));
						vi.setAddress(tempJSONObject.getString("Address"));
						vi.setReservephone(tempJSONObject.getString("ReservePhone"));
						vi.setVenuesemail(tempJSONObject.getString("VenuesEmail"));
						vi.setZipcode(tempJSONObject.getString("ZipCode"));
						vi.setRideroute(tempJSONObject.getString("RideRoute"));
						vi.setZhangdiarea(tempJSONObject.getDouble("zhandiArea"));
						vi.setJianzhuarea(tempJSONObject.getDouble("jianzhuArea"));
						vi.setChangdiarea(tempJSONObject.getDouble("changdiArea"));
						vi.setInternalfacilities(tempJSONObject.getString("InternalFacilities"));	
						vi.setGroundmaterial(tempJSONObject.getString("GroundMaterial"));
						vi.setWatertemperature(tempJSONObject.getDouble("WaterTemperature"));
						vi.setHeadroom(tempJSONObject.getDouble("Headroom"));
						vi.setWaterdepth(tempJSONObject.getDouble("WaterDepth"));
						vi.setOther(tempJSONObject.getString("Other"));
						vi.setVenuestype(tempJSONObject.getString("VenuesType"));
						vi.setVenuesenvironment(tempJSONObject.getString("VenuesEnvironment"));
						vi.setBridfing(tempJSONObject.getString("Briefing"));
						vi.setDirectordept(tempJSONObject.getString("DirectorDept"));
						vi.setResponsiblepeople(tempJSONObject.getString("ResponsiblePeople"));
						vi.setContact(tempJSONObject.getString("Contact"));
						vi.setContactphone(tempJSONObject.getString("ContactPhone"));
						vi.setInvestment(tempJSONObject.getDouble("Investment"));
						vi.setBuilttime(dateParse(tempJSONObject.getString("BuiltTime")));//解析时间转换成了字符串	
						vi.setUsetime(dateParse(tempJSONObject.getString("UseTime")));//解析时间转换成了字符串
						vi.setSeatnumber(tempJSONObject.getInt("SeatNumber"));
						vi.setAvgnumber(tempJSONObject.getInt("AvgNumber")); 
						vi.setVenuesimage(tempJSONObject.getString("VenuesImager"));//这个获取到的就是图片的Uri路径，需要在适配器里面异步加载图片	 
						vi.setWhetherverify(tempJSONObject.getString("WhetherVerify"));
						vi.setWhetherlock(tempJSONObject.getString("WhetherLock"));
						vi.setWhetherrecomm(tempJSONObject.getString("WhetherRecomm"));
						vi.setSort(tempJSONObject.getInt("Sort"));
						vi.setMap(htmlParse(tempJSONObject.getString("Map")));//解析网页代码 
						vi.setCreater(tempJSONObject.getString("Creater"));
						vi.setCreatetime(dateParse(tempJSONObject.getString("CreateTime")));
						data.add(vi); 
					}  
				} 
			} catch (JSONException e) {  e.printStackTrace(); } 
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
	
	/** 解析网页代码
	 *For Example:<iframe width=\"504\" height=\"797\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" src=http://[j.map.baidu.com/CEtGz]></iframe>	
	 *把src解析出来
	 */
	private static String htmlParse(String result){ 
		StringBuilder sb = null;
		Document document=Jsoup.parse(result); 
		Elements res=document.select("iframe");
		if(res.size()>0){
			sb=new StringBuilder();
			for (Element ele : res) { 
				sb.append(ele.attr("src"));
				Log.i("解析网页代码：",ele.attr("src"));
			}
		} 
		Log.i("解析网页代码：","解析完成");
		return sb!=null ? sb.toString() : "";
	}
	
}
