package com.wqp.stadiumapp;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.graphics.Bitmap;

public class AppApplication extends Application {
	
	public static DisplayImageOptions options=new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.user_test)
	.showImageOnFail(R.drawable.user_test)
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	.build();
	
	@Override
	public void onCreate() { 
		super.onCreate();
		//创建ImageLoader默认的配置参数
//		ImageLoaderConfiguration configuration=ImageLoaderConfiguration.createDefault(getApplicationContext());	
		
//		自定义ImageLoaderConfiguration配置参数
		ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(getApplicationContext())
		.threadPriority(Thread.NORM_PRIORITY - 2)//开启线程的优先级
		.denyCacheImageMultipleSizesInMemory() 
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() // Remove for release app  
		.build();
		
		//初始化ImageLoader的配置参数
		ImageLoader.getInstance().init(config); 
	}
	
	
}
