package com.wqp.stadiumapp.utils;  
 
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebServiceUtils {
	/** Service namespace*/
	public static String NAMESPACE="http://tempuri.org/";
	public static String SERVICEURL="http://whjunt.vicp.cc:9011/LWebservice.asmx";
	public static String IMAGE_URL="http://whjunt.vicp.cc:9011";
	
	/** invoke method name*/
	public static String ADDRESERVATION="AddReservation";//��ӻ��Ϣ
	public static String ADDUSER="AddUser";//ע��
	public static String LOGIN="Login";//��½
	public static String GETUSER="GetAUser";//��ѯ������Ϣ(��ȡ������Ϣ)
	public static String USERUPDATE="UserUpdate";//�޸ĸ�����Ϣ
	public static String GETARTINFO="GetArtInfo";//��ȡ���Ϣ
	public static String GETCOMMENT="GetComment";//��ȡ������Ϣ
	public static String GETGUIDE="GetGuide";//��ȡָ����Ϣ
	public static String GETSPORTS="GetSports";//��ȡ�˶���Ϣ
	public static String GETVENUES="GetVenues";//��ȡ������Ϣ
	public static String JOINACT="JoinAct";//Ӧս����
	public static String UPLOAD="Upload ";//���� 
	public static String PWDUPDATE="PWDUpdate";//�������뷽��
	public static String GETVENUESEXPERT="GetVenuesExpert";//��ȡ˽�˽�����Ϣ
	public static String GETUSERONACT="GetUserOnAct";//�����û�ID��ѯ�û��Ѿ��μ�����Щ�
	public static String GETALLMAKE="GetAllMake";//���ݳ���ID��ȡ��������Ϣ
	public static String ADDMAKE="AddMake";//��ӳ���ԤԼ��Ϣ�������
	public static String GETUSERMAKE="GetUserMake";//�����û�ID��ȡ����ǰ�û��ҵ�ԤԼ������Ϣ
	public static String REMOVEMAKE="RemoveMake";//ȡ��ԤԼ������Ϣ
	public static String GET5SECONDU="Get5secondU";//�����û������ѯ����������Ϣ
	public static String GET5SECONDV="Get5secondV";//�����û������ѯ�ͻ����ύ������
	public static String GETSPORTSVENUES="GetSportsVenues";//�����������ƺ��˶���Ŀ���ƽ���ģ����ѯ����,���س�����Ϣ����
	public static String ADDUSERVM="AddUserVM";//����������û�ѡ��ĳ�����Ϣ
	
	/**
	 * ��������Ĳ�������Json format string
	 * @param key ��ʾjson�ַ�����ͷ��Ϣ
	 * @param value ��ʾ�����ļ�������ֵ
	 * @return Json��ʽ�ַ���
	 */
	public static String createJsonString(String key,Object value){   
		JSONObject jsonObject=new JSONObject();
		String res = null;
		try {
			jsonObject.put(key,value);
			res=jsonObject.toString();
			Log.i("����Json����֮���ֵ��",res);
		} catch (JSONException e) { 
			e.printStackTrace();
		} 
		return res;
	} 
}
