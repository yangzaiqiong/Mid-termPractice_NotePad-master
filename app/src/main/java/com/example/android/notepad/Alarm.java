package com.example.android.notepad;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


public class Alarm extends BroadcastReceiver {


    private int alarmId;

    /**
     * 获取的是铃声的Uri
     * @param ctx
     * @param type
     * @return
     */
    public static Uri getDefaultRingtoneUri(Context ctx, int type) {

        return RingtoneManager.getActualDefaultRingtoneUri(ctx, type);

    }

    /**
     * 获取的是铃声相应的Ringtone
     * @param ctx
     * @param type
     */
    public Ringtone getDefaultRingtone(Context ctx, int type) {

        return RingtoneManager.getRingtone(ctx,
                RingtoneManager.getActualDefaultRingtoneUri(ctx, type));

    }

    /**
     * 播放铃声
     * @param ctx
     * @param type
     */

    public static void PlayRingTone(Context ctx,int type){
        MediaPlayer mMediaPlayer = MediaPlayer.create(ctx,
                getDefaultRingtoneUri(ctx,type));
        mMediaPlayer.start();
        mMediaPlayer.stop();
        mMediaPlayer.release();
        /*if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }*/
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        //showMemo(context);
        Log.e("闹钟响了","响了");
        //PlayRingTone(context, RingtoneManager.TYPE_RINGTONE);

        alarmId=intent.getIntExtra("alarmId",0);
        showNotice(context);
    }

    private void showNotice(Context context) {
        int num=alarmId;
        Log.e("Note_Home","alarmNoticeId : "+num);

        /*Intent intent=new Intent(context,Note_Taskdetail.class);

        Note_table note_table= DataSupport.find(Note_table.class,num);
        deleteTheAlarm(num);//or num
        transportInformationToEdit(intent,note_table);*/
        Intent intent=new Intent(context,NoteEditor.class);
        intent.setAction("com.android.notepad.action.EDIT_NOTE");

        PendingIntent pi=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification notification=new NotificationCompat.Builder(context)
                .setContentTitle("您的第" + num + "条备忘任务开始了～")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pi)
                .setAutoCancel(true)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(record.getMainText()))
                .setLights(Color.GREEN,1000,1000)
                .setSound(getDefaultRingtoneUri(context,RingtoneManager.TYPE_RINGTONE))
                .build();
        manager.notify(num,notification);
    }

    /*private void deleteTheAlarm(int num) {
        ContentValues temp = new ContentValues();
        temp.put("alarm_key", "");
        String where = String.valueOf(num);
        DataSupport.updateAll(Note_table.class, temp, "id = ?", where);
    }

    private void transportInformationToEdit(Intent it,Note_table note_table) {

        it.putExtra("content",note_table.getContent());
        it.putExtra("color_key",note_table.getColor_key());
        it.putExtra("time",note_table.getTime());

    }*/
}
