package com.wqp.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
 

import com.wqp.stadiumapp.utils.WebServiceUtils;

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:AddReservation���ص�Json��ʽ���ַ���*/
public class WebAddReservation {
	
	/**
	 * ��������Ĳ�����Web�˷���Json��ʽ���ַ�������
	 * @param str <param name="str">SportType �˶�����;ActTitle �����;VenuesName ʹ�ó���;
	 * VenuesCost ���ݷ���;StartTime ��ʼʱ��;EndTime ����ʱ��;ApplyEndTime ������ֹ����;
	 * TakeCount �������������;RefereeCount ��������;GuideCount ָ��Ա����;
	 * ActCon ���������;ActNotice �����; CreateUser ������;Json��ʽ</param> 
	 * @return <returns>����AddReservation ��ӻ��Ϣ�Ƿ�ɹ�  Json��ʽ</returns>
	 */
	public static boolean getAddReservationData(String str){
		
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.ADDRESERVATION;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.ADDRESERVATION);
		soapObject.addProperty("str",str);//���ò���1 
		
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
				String result=soap.getProperty("AddReservationResult").toString(); 
				return ParseAddUser(result);
//				Log.i("���������������ݺ󷵻صĽ����:",result);
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/** ������ӻ֮��ķ���ֵ */
	private static boolean ParseAddUser(String result){  
		try {
			JSONObject jsonObject=new JSONObject(result);
			return jsonObject.getBoolean("AddReservation");
		} catch (JSONException e) { 
			e.printStackTrace();
		} 
		return false;
	}
}
