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
import com.wqp.webservice.entity.GetSportsVenuesBean; 

public class WebGetSportsVenues { 
	private static final String TAG = "WebGetSportsVenues";

	/** �����˶����� �� ���������ѯ�����������ĳ��ݼ���*/
	public static List<GetSportsVenuesBean> getGetSportsVenuesData(String str){
		//�����ռ�+��������
				String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETSPORTSVENUES;
				
				/**����HttpTransportSE����,����������˵�url*/
				HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
				
				/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
				SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETSPORTSVENUES);
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
						String result=soap.getProperty("GetSportsVenuesResult").toString();
						return ParseGetSportsVenues(result);
//						Log.i("WEB_SERVICE",result);
					} 
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
	}
	
	private static List<GetSportsVenuesBean> ParseGetSportsVenues(String result) {
		List<GetSportsVenuesBean> data=null;
		if(result!=null){
			try {
				JSONObject object=new JSONObject(result);
				JSONArray jsonArray=object.getJSONArray("ds");
				int len=jsonArray.length();
				Log.i(TAG,"���ݳ���="+len);
				if(len>0){ 
					data=new ArrayList<GetSportsVenuesBean>();
					for(int i=0;i<len;i++){
						JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
						GetSportsVenuesBean sv=new GetSportsVenuesBean(); 
						sv.setVenuesID(tempJSONObject.getInt("VenuesID"));
						sv.setVenuesName(tempJSONObject.getString("VenuesName")); 
						sv.setAddress(tempJSONObject.getString("Address"));
						sv.setReservePhone(tempJSONObject.getString("ReservePhone")); 
						sv.setRideRoute(tempJSONObject.getString("RideRoute"));  
						sv.setVenuesEnvironment(tempJSONObject.getString("VenuesEnvironment"));  
						sv.setVenuesImager(tempJSONObject.getString("VenuesImager"));//�����ȡ���ľ���ͼƬ��Uri·������Ҫ�������������첽����ͼƬ	 

						data.add(sv); 
					}  
				} 
			} catch (JSONException e) {  e.printStackTrace(); } 
		}
		return data!=null ? data : null;
	}
	
}
