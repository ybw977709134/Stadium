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
import com.wqp.webservice.entity.GetVenuesExpertBean;

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:GetArtInfo���ص�Json��ʽ���ַ���*/
public class WebGetVenuesExpert {
	
	private static final String TAG = "GetVenuesExpert";

	/** ��Web�˻�ȡָ����Ϣ
	 * @param str �������Ҫ��д��д""����,�������дSQL���
	 *@return <returns>����DataSet ����ר����Ϣ Json��ʽ</returns>;��web�˽��������ݷ�װ��JavaBean�Լ��ϵ���ʽ���� 
	 */
	public static List<GetVenuesExpertBean> getGetVenuesExpertData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETVENUESEXPERT;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETVENUESEXPERT);
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
				String result=soap.getProperty("GetVenuesExpertResult").toString();
				Log.i(TAG,"���ݼ�:"+result);
				return ParseGetArtInfo(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/** ���������JSON��ʽ�ַ������н�����Java����,����װ��List���Ͻ��з���*/
	public static List<GetVenuesExpertBean> ParseGetArtInfo(String result) throws Exception{  
		List<GetVenuesExpertBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"GetVenuesExpert���ݳ���Ϊ:"+len);
			if(len>0){
				data=new ArrayList<GetVenuesExpertBean>();
				for(int i=0;i<len;i++){
					JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
					GetVenuesExpertBean ve=new GetVenuesExpertBean();
					ve.setNumber(tempJSONObject.getInt("Number"));
					ve.setExpertID(tempJSONObject.getInt("ExpertID"));
					ve.setVenuesName(tempJSONObject.getString("VenuesName"));
					ve.setCreater(tempJSONObject.getInt("Creater"));
					ve.setSex(tempJSONObject.getString("Sex"));
					ve.setZhuanJiaType(tempJSONObject.getString("ZhuanJiaType"));
					ve.setNickName(tempJSONObject.getString("NickName"));
					ve.setHobby(tempJSONObject.getString("Hobby")); 
					data.add(ve);
				}
			}  
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return data!=null ? data : null;
	}
	
//	/** ����web�˵�ʱ���ַ���,��ת���ɱ�ʱʱ�䷵�� */
//	private static String dateParse(String result) {
//		if(result!=null){
//			return result.replace('T', ' ');
//		} 
//		return null;
//	}
	
}
