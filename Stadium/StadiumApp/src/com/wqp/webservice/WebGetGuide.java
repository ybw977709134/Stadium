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

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:GetGuide���ص�Json��ʽ���ַ���*/
public class WebGetGuide {
	
	private static final String TAG = "WebGetGuide";

	/** ��Web�˻�ȡָ����Ϣ*/
	public static List<GetGuideBean> getGetGuideData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETGUIDE;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETGUIDE);
		soapObject.addProperty("str",str);//����web�˷����Ĳ���
		
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
				String result=soap.getProperty("GetGuideResult").toString();
				return ParseGetGuide(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/** ���������JSON��ʽ�ַ������н�����Java����,����װ��List���Ͻ��з���*/
	public static List<GetGuideBean> ParseGetGuide(String result){ 
		List<GetGuideBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"����Ϊ="+len);
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
					gb.setCreatetime(dateParse(tempJSONObject.get("CreateTime").toString()));//����ָ�����ʱ��
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
	
	/** ����web�˵�ʱ���ַ���,��ת���ɱ�ʱʱ�䷵�� */
	private static String dateParse(String result) { 
		if(result!=null){
			return result.replace('T', ' ');
		} 
		return null;
	}
	
}
