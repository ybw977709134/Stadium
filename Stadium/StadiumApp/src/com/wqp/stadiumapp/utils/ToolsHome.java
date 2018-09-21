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
	/** 用户进入应用后定位的标识*/
	public static String LOCATION_CITY=null;//定位到的城市名
	public static boolean IS_LOCATION=false;//是否已成功定位
	
	public static final int AUTO_LOCATION_FAILURE=256;//自动定位失败标识
	public static final int AUTO_LOCATION_SUCCESS=512;//自动定位成功标识
	
	public static StadiumFragment mStadiumFragment=null;//标识第二个页面的引用变量,因为使用到了侧滑菜单
	
	/** 该集合存放所有的运动类型,根据编号存储*/
	public static List<Map<Integer,String>> SPORTTYPE_Integer=new ArrayList<Map<Integer,String>>();
	
	/** 该集合存放所有的运动类型,根据运动名称存储*/
	public static List<Map<String,Integer>> SPORTTYPE_STRING=new ArrayList<Map<String,Integer>>();
	
	
	/** 为ViewPager第一页填充数据*/
	public static int[] STADIUM_ITEM_IMAGE_ONE={
		R.drawable.football_normal,R.drawable.basketball_normal,R.drawable.swim_normal,
		R.drawable.tennis_normal,R.drawable.biqiu,R.drawable.pingpong
	};
	public static String[] STADIUM_ITEM_TEXT_ONE={
		"足球","篮球","羽毛球","网球","壁球","乒乓球","台球","保龄球","高尔夫球","排球"
	}; 
	
	/** 为ViewPager第二页填充数据*/
	public static int[] STADIUM_ITEM_MAGE_TWO={ 
		R.drawable.poor,R.drawable.bowling_normal,R.drawable.golf,R.drawable.paiqu,R.drawable.ball_more
	}; 
	public static String[] STADIUM_ITEM_TEXT_TWO={
		"台球","保龄球","高尔夫球","排球","更多"
	};
	
	public static String test_content="12月22日，国务院总理李克强在北京人民大会堂同泰国总理巴育举行会谈。会谈前，李克强在人民大会堂北大厅为巴育举行欢迎仪式。";
	
	
	private static final int STROKE_WIDTH = 4;
	/** 对Bitmap图片进行二次处理,并返回Bitmap*/ 
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
			float clip=(width-height)/2;//clip:修剪
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
		
		//重置画笔
		paint.reset();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(STROKE_WIDTH);
		paint.setAntiAlias(true);
		canvas.drawCircle(width/2, width/2, width/2-STROKE_WIDTH/2, paint);
		
		return output;
	}
	
	/** 根据数据库返回的运动类型ID来查询，返回对应运动的图片资源ID*/
	public int queryBall(int id){
		switch (id) {
		case 1:
			return R.drawable.football_normal;//足球 
		case 2:
			return R.drawable.basketball_normal;//篮球
		case 3:
			return R.drawable.badminton_normal;//羽毛球
		case 4:
			return R.drawable.tennis_normal;//网球   
		case 5:
			return R.drawable.biqiu;//壁球 
		case 6:
			return R.drawable.pingpong;//乒乓球
		case 7:
			return R.drawable.poor;//台球
		case 8:
			return R.drawable.bowling_normal;//保龄球
		case 9:
			return R.drawable.golf;//高尔夫球
		case 10:
			return R.drawable.paiqu;//排球
		}
		return 0;
	}
	
	
	//**根据传入的参数把数据存入到SPORTTYPE_Integer集合当中*/
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
	
	/**输入运动类型编号，返回运动的名称*/
	public static String getSportName(Integer id){
		for (Map<Integer,String> is : SPORTTYPE_Integer) {
			if(is.containsKey(id)){
				return is.get(id);
			}
		}
		return null;
	}
	
	/**输入运动类型，返回运动的编号*/
	public static Integer getSportSerial(String res){
		for (Map<String,Integer> is : SPORTTYPE_STRING) {
			if(is.containsKey(res)){
				return is.get(res);
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
}
