package com.wqp.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;  

import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.entity.GetLoginBean;

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:Login���ص�Json��ʽ���ַ���*/
public class WebLogin {
	/**
	 * �������ƽ��е�½����
	 * @param str
	 */
	public static GetLoginBean getLoginData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.LOGIN;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.LOGIN);
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
				String result=soap.getProperty("LoginResult").toString();
				return ParseLogin(result);  
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/** ���ݷ���˷��ص����ݽ���֮������ݷ�װ��JavaBean���󷵻�*/
	private static GetLoginBean ParseLogin(String result){
		Log.i("result=",result); 
		GetLoginBean lb=null;
		try { 
			JSONObject object=new JSONObject(result);
			if(object.getBoolean("UserExist")){
				lb=new GetLoginBean();
				lb.setUserExist(object.getBoolean("UserExist"));//�û��Ǵ���
				lb.setUserID(object.getInt("UserID")); //�û���ID
			}  
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return lb;
	}
}
