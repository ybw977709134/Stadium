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

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:GetVenues���ص�Json��ʽ���ַ���*/
public class WebGetVenues {
	private static final String TAG = "WebGetVenues";

	/**
	 * ���볡��ID
	 * @param VenuesID
	 */
	public static List<GetVenuesInfoBean> getGetVenuesData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETVENUES;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETVENUES);
		if(str!=null){
			soapObject.addProperty("str",str);//����web�˷����Ĳ���
		} 
		
		/**ʹ��SOAPЭ�������л���Envelope
		 * ��SoapObject��������ΪSoapSerializationEnvelope����Ĵ���SOAP��Ϣ
		 * ������SoapEnvelope�汾��
		 */
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut=soapObject;
		envelope.dotNet=true;
		envelope.setOutputSoapObject(soapObject);
		envelope.encodingStyle="UTF-8";
		try{
			/**��ʼ����Զ�̷���,����1:�����ռ�+��������,����2��ʵ�������Envelope*/
			httpse.call(soapAction, envelope);
			if(envelope.getResponse()!=null){
				//��ȡ��������Ӧ���ص�SOAP��Ϣ
				SoapObject soap=(SoapObject) envelope.bodyIn;
				/**��ȡ���ص�����,����������˷��صķ�����(�����ڷ���˲���)*/
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
				Log.i(TAG,"���ݳ���="+len);
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
						vi.setBuilttime(dateParse(tempJSONObject.getString("BuiltTime")));//����ʱ��ת�������ַ���	
						vi.setUsetime(dateParse(tempJSONObject.getString("UseTime")));//����ʱ��ת�������ַ���
						vi.setSeatnumber(tempJSONObject.getInt("SeatNumber"));
						vi.setAvgnumber(tempJSONObject.getInt("AvgNumber")); 
						vi.setVenuesimage(tempJSONObject.getString("VenuesImager"));//�����ȡ���ľ���ͼƬ��Uri·������Ҫ�������������첽����ͼƬ	 
						vi.setWhetherverify(tempJSONObject.getString("WhetherVerify"));
						vi.setWhetherlock(tempJSONObject.getString("WhetherLock"));
						vi.setWhetherrecomm(tempJSONObject.getString("WhetherRecomm"));
						vi.setSort(tempJSONObject.getInt("Sort"));
						vi.setMap(htmlParse(tempJSONObject.getString("Map")));//������ҳ���� 
						vi.setCreater(tempJSONObject.getString("Creater"));
						vi.setCreatetime(dateParse(tempJSONObject.getString("CreateTime")));
						data.add(vi); 
					}  
				} 
			} catch (JSONException e) {  e.printStackTrace(); } 
		}
		return data!=null ? data : null;
	}
		
	/** ����web�˵�ʱ���ַ���,��ת���ɱ�ʱʱ�䷵�� */
	private static String dateParse(String result) { 
		if(result!=null){
			return result.replace('T', ' ');
		} 
		return null;
	} 
	
	/** ������ҳ����
	 *For Example:<iframe width=\"504\" height=\"797\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" src=http://[j.map.baidu.com/CEtGz]></iframe>	
	 *��src��������
	 */
	private static String htmlParse(String result){ 
		StringBuilder sb = null;
		Document document=Jsoup.parse(result); 
		Elements res=document.select("iframe");
		if(res.size()>0){
			sb=new StringBuilder();
			for (Element ele : res) { 
				sb.append(ele.attr("src"));
				Log.i("������ҳ���룺",ele.attr("src"));
			}
		} 
		Log.i("������ҳ���룺","�������");
		return sb!=null ? sb.toString() : "";
	}
	
}
