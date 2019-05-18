package com.example.android.notepad;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddAlarm extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String EDIT_TITLE_ACTION = "com.android.notepad.action.ADD_ALARM";

    // Creates a projection that returns the note ID and the note background.
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID,               // Projection position 0, the note's id
            NotePad.Notes.COLUMN_NAME_ALARM_DATE //闹钟时间
    };

    // The position of the title column in a Cursor returned by the provider.
    private static final int COLUMN_INDEX_ALARM_DATE = 8;

    // A Cursor object that will contain the results of querying the provider for a note.
    private Cursor mCursor;

    // A URI object for the note whose title is being edited.
    private Uri mUri;


    //定时变量
    int alarm_hour;
    int alarm_minute;
    int alarm_year;
    int alarm_month;
    int alarm_day;
    String alarmDate;

    //addAlarm——表示添加闹钟或者修改闹钟， cancelAlarm——表示取消闹钟
    private String point;
    //当前所进入的id
    int id;

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        alarm_year=year;
        alarm_month=monthOfYear+1;
        alarm_day=dayOfMonth;

        Log.e("onDateSet——alarm_hour",String.valueOf(alarm_hour));
        Log.e("onDateSet——alarm_minute",String.valueOf(alarm_minute));
        Log.e("onDateSet——alarm_year",String.valueOf(alarm_year));
        Log.e("onDateSet——alarm_month",String.valueOf(alarm_month));
        Log.e("onDateSet——alarm_day",String.valueOf(alarm_day));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        alarm_hour=hourOfDay;
        alarm_minute=minute;


        Log.e("onTimeSet——alarm_hour",String.valueOf(alarm_hour));
        Log.e("onTimeSet——alarm_minute",String.valueOf(alarm_minute));
        Log.e("onTimeSet——alarm_year",String.valueOf(alarm_year));
        Log.e("onTimeSet——alarm_month",String.valueOf(alarm_month));
        Log.e("onTimeSet——alarm_day",String.valueOf(alarm_day));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        mUri = getIntent().getData();
        mCursor = managedQuery(
                mUri,        // The URI for the note that is to be retrieved.
                PROJECTION,  // The columns to retrieve
                null,        // No selection criteria are used, so no where columns are needed.
                null,        // No where columns are used, so no where values are needed.
                null         // No sort order is needed.
        );

        showAlarm();
    }

    public void onClickOk(View v) {
        point = "addAlarm";
        finish();
    }
    public void onClickCancel(View v) {
        point = "cancelAlarm";
        finish();
    }


    //显示界面
    private final void showAlarm(){

        Log.e("提醒", "进去setOrEditAlarm");

        Log.e("alarm_hour",String.valueOf(alarm_hour));
        Log.e("alarm_minute",String.valueOf(alarm_minute));
        Log.e("alarm_year",String.valueOf(alarm_year));
        Log.e("alarm_month",String.valueOf(alarm_month));
        Log.e("alarm_day",String.valueOf(alarm_day));

        if(mCursor.moveToFirst() == true){
            int alarmDateIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_ALARM_DATE);
            alarmDate = mCursor.getString(alarmDateIndex);
            id = mCursor.getInt(mCursor.getColumnIndex(NotePad.Notes._ID));
        }
        else {
            alarmDate = null;
        }

        //如果还没有设定闹钟,默认显示当前时间
        if(alarmDate == null){

            Calendar c = Calendar.getInstance();
            alarm_hour = c.get(Calendar.HOUR_OF_DAY);
            alarm_minute = c.get(Calendar.MINUTE);
            alarm_year = c.get(Calendar.YEAR);
            alarm_month = c.get(Calendar.MONTH)+1;
            alarm_day = c.get(Calendar.DAY_OF_MONTH);

            Log.e("月份",String.valueOf(alarm_month));

            alarmDate=alarm_year+"/"+alarm_month+"/"+alarm_day+" "+alarm_hour+":"+alarm_minute;

        }
        //如果已经设定，显示已经设定的时间
        else {
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
        }

        Log.e("设定时间",alarmDate);
        new TimePickerDialog(this,this,alarm_hour,alarm_minute,true).show();
        new DatePickerDialog(this,this,alarm_year,alarm_month-1,alarm_day).show();

        Log.e("提醒", "setOrEditAlarm出去");
    }

    //设置或者修改闹钟
    private void setOrEditAlarm(){
        alarmDate=alarm_year+"/"+alarm_month+"/"+alarm_day+" "+alarm_hour+":"+alarm_minute;
        Log.e("定好的闹钟时间",alarmDate);
    }


    @Override
    protected void onPause() {
        super.onPause();

        // Verifies that the query made in onCreate() actually worked. If it worked, then the
        // Cursor object is not null. If it is *empty*, then mCursor.getCount() == 0.

        if (mCursor != null) {

            //判断是要添加闹钟还是取消闹钟
            //如果添加闹钟或者修改闹钟
            if("addAlarm".equals(point)){
                setOrEditAlarm();

                // Creates a values map for updating the provider.
                ContentValues values = new ContentValues();

                if(alarmDate == null){
                    if(mCursor.moveToFirst() == true){
                        int alarmDateIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_ALARM_DATE);
                        alarmDate = mCursor.getString(alarmDateIndex);
                    }
                    else {
                        alarmDate = "";
                    }
                }

                // In the values map, sets the title to the current contents of the edit box.
                values.put(NotePad.Notes.COLUMN_NAME_ALARM_DATE, alarmDate);

                /*
                 * Updates the provider with the note's new title.
                 *
                 * Note: This is being done on the UI thread. It will block the thread until the
                 * update completes. In a sample app, going against a simple provider based on a
                 * local database, the block will be momentary, but in a real app you should use
                 * android.content.AsyncQueryHandler or android.os.AsyncTask.
                 */
                getContentResolver().update(
                        mUri,    // The URI for the note to update.
                        values,  // The values map containing the columns to update and the values to use.
                        null,    // No selection criteria is used, so no "where" columns are needed.
                        null     // No "where" columns are used, so no "where" values are needed.
                );

                new Thread(new MyRunnable(this)).start();
            }
        }
    }

    class MyRunnable implements Runnable {

        /**
         * 成员变量Context
         */
        Context context;

        /**
         * 构造函数 将程序所需Context传入
         */
        public MyRunnable(Context context) {
            //为成员变量赋值
            this.context = context;
        }

        @Override
        public void run() {

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
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            //if(interval==0)
            am.set(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), sender);

            Log.e("设定时间", String.valueOf(alarm_time.getTimeInMillis()));
            Log.e("当前时间", String.valueOf(System.currentTimeMillis()));
            //测试

                        /*long triggerAtTime = System.currentTimeMillis() - 10 * 1000;
                        Log.e("测试设定时间", String.valueOf(triggerAtTime));
                        am.set(AlarmManager.RTC_WAKEUP, triggerAtTime, sender);*/

            Log.e("关于闹钟", String.valueOf(id));
        }
    }

}
