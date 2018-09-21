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
import com.wqp.webservice.entity.GetCommentBean; 

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:GetComment���ص�Json��ʽ���ַ���*/
public class WebGetComment {
	
	private static final String TAG = "WebGetComment";

	/** ��Web�˻�ȡָ����Ϣ
	 * 
	 * @param str �������ȫ��������д ""����,
	 * �ַ����ı�׼��ʽΪ��KeyType='����' and KeyID=1�� ����KeyTypeΪ��ѯ�ֶ� �����ݡ��ֶ�ֵ andΪ��������
	 * @return <returns>����DataSet ���ػ��Ϣ Json��ʽ</returns>
	 */
	public static List<GetCommentBean> getGetCommentData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETCOMMENT;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETCOMMENT);
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
				String result=soap.getProperty("GetCommentResult").toString();
				return ParseGetComment(result);
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/** ���������JSON��ʽ�ַ������н�����Java����,����װ��List���Ͻ��з���*/
	public static List<GetCommentBean> ParseGetComment(String result){ 
		List<GetCommentBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"����Ϊ="+len);
			if(len!=0){
				data=new ArrayList<GetCommentBean>();
				for(int i=0;i<len;i++){
					JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
					GetCommentBean cb=new GetCommentBean();
					cb.setCommentid(tempJSONObject.getInt("CommentID"));
					cb.setUserid(tempJSONObject.getInt("UserID"));
					cb.setCon(tempJSONObject.getString("Con"));
					cb.setKeyid(tempJSONObject.getInt("KeyID"));
					cb.setKeytype(tempJSONObject.getString("KeyType"));
					cb.setCreatetime(dateParse(tempJSONObject.get("CreateTime").toString()));//��������ʱ��	
					data.add(cb);
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
