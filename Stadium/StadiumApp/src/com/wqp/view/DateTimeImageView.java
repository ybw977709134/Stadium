package com.wqp.view;
 
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.wqp.stadiumapp.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface; 
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View; 
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimeImageView extends ImageView {
	public Calendar time=Calendar.getInstance(Locale.CHINA);
	public SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DatePicker datePicker;
	private TimePicker timePicker;
	private EditText dataView;
	private AlertDialog dialog; 
	
    public DateTimeImageView(Context context) {
        super(context);
        init();
    }
    public DateTimeImageView(Context context, AttributeSet attrs) {
    	super(context, attrs);
    	init();
    }
	public DateTimeImageView(Context context,EditText dataView){
		super(context);
		this.dataView=dataView;
	}
	
	private void init(){ 
		this.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) { 
			//生成一个DatePickerDialog对象并显示，显示的DatePickerDialog控件可以选择年月日，并设置
				dateTimePickerDialog();
			}
		});
		updateLabel();
	}
	
	/** 更新标签*/
	public void updateLabel(){
		if(dataView!=null){
			dataView.setText(format.format(time.getTime()));
		} 
	} 
	
	/** 生成一个组合的对话框,并返回*/
	public AlertDialog dateTimePickerDialog(){
		LinearLayout dateTimeLayout=(LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_date_time_selected, null);	
		datePicker=(DatePicker) dateTimeLayout.findViewById(R.id.selected_datepicker);
		timePicker=(TimePicker) dateTimeLayout.findViewById(R.id.selected_timepicker);
		if(dataView==null){ init(); }
		timePicker.setIs24HourView(true); 
		OnTimeChangedListener timeListener=new OnTimeChangedListener(){ 
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				time.set(Calendar.HOUR_OF_DAY, hourOfDay);
				time.set(Calendar.MINUTE, minute);
			} 
		};
		//为时间选择器设置监听
		timePicker.setOnTimeChangedListener(timeListener);
		
		OnDateChangedListener dateListener=new OnDateChangedListener(){ 
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				time.set(Calendar.YEAR, year);
				time.set(Calendar.MONTH, monthOfYear);
				time.set(Calendar.DAY_OF_MONTH, dayOfMonth); 
			} 
		};
		//为日期选择器设置监听
		datePicker.init(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), dateListener);
 		
		timePicker.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(time.get(Calendar.MINUTE));
		
		dialog=new AlertDialog.Builder(getContext())
		.setTitle("请选择日期和时间")
		.setView(dateTimeLayout)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				datePicker.clearFocus();//清除日期选择器的焦点
				timePicker.clearFocus();//清除时间选择器的焦点
				time.set(Calendar.YEAR, datePicker.getYear());  
                time.set(Calendar.MONTH, datePicker.getMonth());  
                time.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());  
                time.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());  
                time.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                updateLabel();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) { 
				if(dataView!=null){
					dataView.setText("");
				}
			}
		}).show();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	
	
}
