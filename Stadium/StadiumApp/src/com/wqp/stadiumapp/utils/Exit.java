package com.wqp.stadiumapp.utils;

import java.util.LinkedList;
import java.util.List;  

import android.app.Activity;
import android.app.Application;

public class Exit extends Application {
	
	private List<Activity> activityList = new LinkedList<Activity>();
	private static Exit instance;
	
	private Exit() { }
	
	public static Exit getInstance() {
		if (instance == null) {
			instance = new Exit();
		}
		return instance;
	}
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	
	public void destroyActivity(){
		if(activityList.size()>0){
			for (Activity element : activityList) {
				element.finish();
			}
		}
	}
}
