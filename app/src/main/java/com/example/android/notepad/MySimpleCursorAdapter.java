package com.example.android.notepad;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class MySimpleCursorAdapter extends SimpleCursorAdapter {
    private LayoutInflater mInflater;
    private OnMyAdaptertListener mListener;
    int[] ids;
    String[] str;
    int layoutId;
    Context mContext;

    @SuppressWarnings("deprecation")
    public MySimpleCursorAdapter(Context context, int layout, Cursor c,
                                 String[] string, int[] id) {
        super(context, layout, c, string, id);
        ids = id;
        str = string;
        layoutId = layout;
        mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //定时变量
        int alarm_hour;
        int alarm_minute;
        int alarm_year;
        int alarm_month;
        int alarm_day;
        int id;

        TextView titleView = (TextView) view.findViewById(ids[0]);
        TextView timeView = (TextView) view.findViewById(ids[1]);
        ImageView alarmView = (ImageView) view.findViewById(ids[2]);

        String title = cursor.getString(cursor.getColumnIndex(str[0]));
        String time = cursor.getString(cursor.getColumnIndex(str[1]));
        String alarmDate = cursor.getString(cursor.getColumnIndex(str[2]));
        id = cursor.getInt(cursor.getColumnIndex(NotePad.Notes._ID));

        titleView.setText(title);
        timeView.setText(time);
        if(alarmDate != null && alarmDate != ""){
            alarmView.setBackgroundResource(R.drawable.alarm);

/*
            //从数据库中拿到alarm_date数据进行定时操作
            int i=0, k=0;
            while(i<alarmDate.length()&&alarmDate.charAt(i)!='/') i++;
            alarm_year = Integer.parseInt(alarmDate.substring(k,i));
            k=i+1;i++;
            while(i<alarmDate.length()&&alarmDate.charAt(i)!='/') i++;
            alarm_month = Integer.parseInt(alarmDate.substring(k,i));
            k=i+1;i++;
            while(i<alarmDate.length()&&alarmDate.charAt(i)!=' ') i++;
            alarm_day = Integer.parseInt(alarmDate.substring(k,i));
            k=i+1;i++;
            while(i<alarmDate.length()&&alarmDate.charAt(i)!=':') i++;
            alarm_hour = Integer.parseInt(alarmDate.substring(k,i));
            k=i+1;i++;
            alarm_minute = Integer.parseInt(alarmDate.substring(k));


            Intent intent = new Intent(context, Alarm.class);
            //Intent intent = new Intent();
            intent.putExtra("alarmId",id);
            PendingIntent sender = PendingIntent.getBroadcast(context,id, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.add(Calendar.SECOND, 5);


            Log.e("alarm_hour",String.valueOf(alarm_hour));
            Log.e("alarm_minute",String.valueOf(alarm_minute));
            Log.e("alarm_year",String.valueOf(alarm_year));
            Log.e("alarm_month",String.valueOf(alarm_month));
            Log.e("alarm_day",String.valueOf(alarm_day));

            Calendar alarm_time = Calendar.getInstance();
            alarm_time.set(alarm_year,alarm_month-1,alarm_day,alarm_hour,alarm_minute);



            // Schedule the alarm!
            AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            //if(interval==0)
            am.set(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), sender);

            Log.e("设定时间", String.valueOf(alarm_time.getTimeInMillis()));
            Log.e("当前时间", String.valueOf(System.currentTimeMillis()));
            //测试

            *//*long triggerAtTime = System.currentTimeMillis() - 10 * 1000;
            Log.e("测试设定时间", String.valueOf(triggerAtTime));
            am.set(AlarmManager.RTC_WAKEUP, triggerAtTime, sender);*//*

            Log.e("关于闹钟", String.valueOf(id));*/


        }
//        delete.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (mListener != null) {
//                    mListener.onDelClicked(text);
//                }
//            }
//        });

    }

    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
        return mInflater.inflate(layoutId, arg2, false);
    }

    public void setMyAdapterListener(OnMyAdaptertListener listener) {
        mListener = listener;
    }

    public interface OnMyAdaptertListener {
        public void onDelClicked(String text);
    }

}