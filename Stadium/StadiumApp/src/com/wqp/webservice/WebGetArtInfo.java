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

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:GetArtInfo���ص�Json��ʽ���ַ���*/
public class WebGetArtInfo {
	
	private static final String TAG = "WebGetArtInfo";

	/** ��Web�˻�ȡָ����Ϣ
	 * @param str �������Ҫ��д��д""����,�������дSQL���
	 *@return <returns>����DataSet ���ػ��Ϣ Json��ʽ</returns>;��web�˽��������ݷ�װ��JavaBean�Լ��ϵ���ʽ���� 
	 */
	public static List<GetActInfoBean> getGetArtInfoData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETARTINFO;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETARTINFO);
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
				String result=soap.getProperty("GetArtInfoResult").toString();
				Log.i(TAG,"���ݼ�:"+result);
				return ParseGetArtInfo(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/** ���������JSON��ʽ�ַ������н�����Java����,����װ��List���Ͻ��з���*/
	public static List<GetActInfoBean> ParseGetArtInfo(String result) throws Exception{  
		List<GetActInfoBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"����Ϊ:"+len);
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
					ai.setPubimg(tempJSONObject.getString("PubImg")); //����ͼƬ��ַ
					ai.setStarttime(dateParse(tempJSONObject.get("StartTime").toString()));//������ʼʱ��	 
					ai.setEndtime(dateParse(tempJSONObject.get("EndTime").toString()));//��������ʱ��
					ai.setApplyendtime(dateParse(tempJSONObject.get("ApplyEndTime").toString()));//����������ֹ����	 
					ai.setTakecount(tempJSONObject.getInt("TakeCount"));
					ai.setRefereecount(tempJSONObject.getInt("RefereeCount"));
					ai.setGuidecount(tempJSONObject.getInt("GuideCount"));
					ai.setActcon(tempJSONObject.getString("ActCon"));
					ai.setActnotice(tempJSONObject.getString("ActNotice"));
					ai.setCreateuser(tempJSONObject.getInt("CreateUser"));
					ai.setCreatetime(dateParse(tempJSONObject.get("CreateTime").toString())); //��������ʱ�� 
					ai.setHotspot(tempJSONObject.getBoolean("HotSpot"));
					ai.setAdddigest(tempJSONObject.getBoolean("AddDigest"));
					ai.setBrowsecount(tempJSONObject.getInt("BrowseCount"));
					ai.setState(tempJSONObject.getString("State"));
					ai.setActusertype(tempJSONObject.getString("ActUserType"));
					Log.i(TAG,"���ͼƬ��ַ��:"+ai.getPubimg());
					data.add(ai);
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
