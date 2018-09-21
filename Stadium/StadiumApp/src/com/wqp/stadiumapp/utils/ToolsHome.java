package com.wqp.stadiumapp.utils;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.wqp.stadiumapp.R;
import com.wqp.stadiumapp.fragment.StadiumFragment;
import com.wqp.webservice.entity.GetSportBean;

public class ToolsHome {
	/** �û�����Ӧ�ú�λ�ı�ʶ*/
	public static String LOCATION_CITY=null;//��λ���ĳ�����
	public static boolean IS_LOCATION=false;//�Ƿ��ѳɹ���λ
	
	public static final int AUTO_LOCATION_FAILURE=256;//�Զ���λʧ�ܱ�ʶ
	public static final int AUTO_LOCATION_SUCCESS=512;//�Զ���λ�ɹ���ʶ
	
	public static StadiumFragment mStadiumFragment=null;//��ʶ�ڶ���ҳ������ñ���,��Ϊʹ�õ��˲໬�˵�
	
	/** �ü��ϴ�����е��˶�����,���ݱ�Ŵ洢*/
	public static List<Map<Integer,String>> SPORTTYPE_Integer=new ArrayList<Map<Integer,String>>();
	
	/** �ü��ϴ�����е��˶�����,�����˶����ƴ洢*/
	public static List<Map<String,Integer>> SPORTTYPE_STRING=new ArrayList<Map<String,Integer>>();
	
	
	/** ΪViewPager��һҳ�������*/
	public static int[] STADIUM_ITEM_IMAGE_ONE={
		R.drawable.football_normal,R.drawable.basketball_normal,R.drawable.swim_normal,
		R.drawable.tennis_normal,R.drawable.biqiu,R.drawable.pingpong
	};
	public static String[] STADIUM_ITEM_TEXT_ONE={
		"����","����","��ë��","����","����","ƹ����","̨��","������","�߶�����","����"
	}; 
	
	/** ΪViewPager�ڶ�ҳ�������*/
	public static int[] STADIUM_ITEM_MAGE_TWO={ 
		R.drawable.poor,R.drawable.bowling_normal,R.drawable.golf,R.drawable.paiqu,R.drawable.ball_more
	}; 
	public static String[] STADIUM_ITEM_TEXT_TWO={
		"̨��","������","�߶�����","����","����"
	};
	
	public static String test_content="12��22�գ�����Ժ�������ǿ�ڱ�����������̩ͬ������������л�̸����̸ǰ�����ǿ���������ñ�����Ϊ�������л�ӭ��ʽ��";
	
	
	private static final int STROKE_WIDTH = 4;
	/** ��BitmapͼƬ���ж��δ���,������Bitmap*/ 
	public static Bitmap toRoundBitmap(Bitmap bitmap){
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		float roundPx;
		float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
		if(width<height){
			roundPx=width/2;
			left=0;
			top=0; 
			right=width;
			bottom=width;
			dst_left=0;
			dst_top=0;
			dst_right=width;
			dst_bottom=width; 
		}else{
			roundPx=height/2;
			float clip=(width-height)/2;//clip:�޼�
			left=clip;
			top=0;
			right=width-clip;
			bottom=height;
			dst_left=0;
			dst_top=0;
			dst_right=height;
			dst_bottom=height; 
		}
		
		Bitmap output=Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas=new Canvas(output);
		
//		final int color=0xff424242;
		final Paint paint=new Paint();
		final Rect src=new Rect((int)left,(int)top,(int)right,(int)bottom);
		final Rect rect=new Rect((int)dst_left,(int)dst_top,(int)dst_right,(int)dst_bottom);
		final RectF rectF=new RectF(rect);
		
		paint.setAntiAlias(true);
		
		canvas.drawARGB(0, 0, 0, 0);//ARGB
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(4);
		canvas.drawRoundRect(rectF,roundPx,roundPx,paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, rect,paint);
		
		//���û���
		paint.reset();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(STROKE_WIDTH);
		paint.setAntiAlias(true);
		canvas.drawCircle(width/2, width/2, width/2-STROKE_WIDTH/2, paint);
		
		return output;
	}
	
	/** �������ݿⷵ�ص��˶�����ID����ѯ�����ض�Ӧ�˶���ͼƬ��ԴID*/
	public int queryBall(int id){
		switch (id) {
		case 1:
			return R.drawable.football_normal;//���� 
		case 2:
			return R.drawable.basketball_normal;//����
		case 3:
			return R.drawable.badminton_normal;//��ë��
		case 4:
			return R.drawable.tennis_normal;//����   
		case 5:
			return R.drawable.biqiu;//���� 
		case 6:
			return R.drawable.pingpong;//ƹ����
		case 7:
			return R.drawable.poor;//̨��
		case 8:
			return R.drawable.bowling_normal;//������
		case 9:
			return R.drawable.golf;//�߶�����
		case 10:
			return R.drawable.paiqu;//����
		}
		return 0;
	}
	
	
	//**���ݴ���Ĳ��������ݴ��뵽SPORTTYPE_Integer���ϵ���*/
	public static boolean addDataSet(List<GetSportBean> data){
		SPORTTYPE_Integer.clear();
		SPORTTYPE_STRING.clear();
		if(data!=null){
			for(int i=0;i<data.size();i++){
				 
				Map<Integer,String> data_integer=new HashMap<Integer,String>();
				data_integer.put(i, data.get(i).getSportname());
				SPORTTYPE_Integer.add(data_integer); 
				
				Map<String,Integer> data_string=new HashMap<String,Integer>();
				data_string.put(data.get(i).getSportname(), i);
				SPORTTYPE_STRING.add(data_string);
			}
			return true;
		}
		return false;
	}
	
	/**�����˶����ͱ�ţ������˶�������*/
	public static String getSportName(Integer id){
		for (Map<Integer,String> is : SPORTTYPE_Integer) {
			if(is.containsKey(id)){
				return is.get(id);
			}
		}
		return null;
	}
	
	/**�����˶����ͣ������˶��ı��*/
	public static Integer getSportSerial(String res){
		for (Map<String,Integer> is : SPORTTYPE_STRING) {
			if(is.containsKey(res)){
				return is.get(res);
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
}
