package com.wqp.webservice;
 
 
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE; 
import com.wqp.stadiumapp.utils.WebServiceUtils; 

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:AddUser���ص�Json��ʽ���ַ���*/
public class WebAddUser {  
	/**
	 * ���û�ע��
	 * @param str <param name="str">UserName �û��� ;password �����;Name �Ա�; Nickname �û�ID ;Phone �ǳ�;Json��ʽ</param>
	 * @return <returns>����AddUser ��ӵ����Ƿ�ɹ�  Json��ʽ</returns>; ע���Ƿ�ɹ� true:�ɹ�,false:ʧ��
	 */
	public static boolean getAddUserData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.ADDUSER;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.ADDUSER);
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
				String result=soap.getProperty("AddUserResult").toString();
				return ParseAddUser(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return false;
	}
	
	/** ����ע��֮��ķ���ֵ */
	private static boolean ParseAddUser(String result){  
		try {
			JSONObject jsonObject=new JSONObject(result);
			return jsonObject.getBoolean("AddUser");
		} catch (JSONException e) { 
			e.printStackTrace();
		} 
		return false;
	}
	
}
