package com.esri.lcdx.online;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.Calendar;

public class SelectTIme extends PopupWindow {
    private View view;
    private DatePicker datePicker;
    int year; //今年
    int month; //当前月份
    int day; //今天
    private String data;

    public SelectTIme(Context context){
        view=LayoutInflater.from(context).inflate(R.layout.activity_date,null);
        this.setAnimationStyle(R.style.anim_menu_bottombar);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable colorDrawable=new ColorDrawable(context.getResources().getColor(R.color.white));
        this.setBackgroundDrawable(colorDrawable);
        this.setFocusable(true);
        this.setContentView(view);
        datePicker=view.findViewById(R.id.DatePicker_01);
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.rilikongjian).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
        Calendar calendar= Calendar.getInstance(); //获取日历的实例
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);//实际月份需要加1
        day=calendar.get(Calendar.DATE);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                System.out.println(i);
                System.out.println(i1);
                System.out.println(i2);
                int i3=i1+1;
                String leaveData=i+"年"+i3+"月"+i2+"日";
                setData(leaveData);
                System.out.println(data);
                if(LeaveActivity.i==0){
                    LeaveActivity.finishTime.setText(data);
                    SelectTIme.this.dismiss();
                }else {
                    LeaveActivity.leaveTime.setText(data);
                    SelectTIme.this.dismiss();
                }
            }
        });

    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
