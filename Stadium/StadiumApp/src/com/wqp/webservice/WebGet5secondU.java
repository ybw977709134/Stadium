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
import com.wqp.webservice.entity.Get5SecondUBean;

/**
 * ��÷���˻ظ��û��Ĳ�����Ϣ 
 *
 */
public class WebGet5secondU {
	private static final String TAG = "WebGet5secondU";

	/**
	 * ������ͨ�û�ID��ѯ������Ƿ�����Ϣ�ظ�
	 * @param str {"UserID":"34"} Json��ʽ����
	 * @return ��ѯ������Ϣ
	 */
	public static List<Get5SecondUBean> getGet5secondU(String str){
		//�����ռ�+��������
				String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GET5SECONDU;
				
				/**����HttpTransportSE����,����������˵�url*/
				HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
				
				/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
				SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GET5SECONDU);
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
						String result=soap.getProperty("Get5secondUResult").toString();
						return ParseGet5SecondU(result); 
					} 
				}catch(Exception e){
					e.printStackTrace();
				} 
				return null;
	}
	
	/**
	 * ���ݷ���˷��ص����ݽ��н�������
	 * @param result ����
	 * @return �����Ľ��
	 */
	private static List<Get5SecondUBean> ParseGet5SecondU(String result) {
		List<Get5SecondUBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"����Ϊ="+len);
			if(len!=0){
				data=new ArrayList<Get5SecondUBean>();
				for(int i=0;i<len;i++){
					JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
					Get5SecondUBean gsu=new Get5SecondUBean();
					gsu.setUserVenMesID(tempJSONObject.getInt("UserVenMesID"));
					gsu.setProID(tempJSONObject.getInt("ProID"));
					gsu.setUserID(tempJSONObject.getInt("UserID"));
					gsu.setCon(tempJSONObject.getString("Con"));
					gsu.setVenuesIDs(tempJSONObject.getInt("VenuesIDs"));
					gsu.setIsVenuesID(tempJSONObject.getInt("IsVenuesID"));	
					gsu.setCreateTime(tempJSONObject.getString("CreateTime"));
					gsu.setPrice((float)tempJSONObject.getDouble("Price"));
					gsu.setProName(tempJSONObject.getString("ProName"));
					gsu.setMakeTime(tempJSONObject.getString("MakeTime")); 
					data.add(gsu);
				}
			}  
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return data!=null ? data : null;
	}
	
	
}
