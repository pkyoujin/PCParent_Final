package com.pknu.pcparent.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import com.pknu.pcparent.MyApplication;
import com.pknu.pcparent.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Hoon on 2017-06-11.
 */

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive()", "실행");

        // SMS 메시지 수신 시
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            Object[] messages = (Object[]) bundle.get("pdus");
            SmsMessage[] smsMessage = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++)
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);

            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh:mm:ss", Locale.KOREA);
            String orgDate = mDateFormat.format(curDate);
            String orgNumber = smsMessage[0].getOriginatingAddress();
            String orgText = smsMessage[0].getMessageBody().toString();
            Log.d("onReceive()", "orgNumber: " + orgNumber + ", orgDate: " + orgDate + "\norgText:\n" + orgText);

            /** 메인 액티비티 분기 **/
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(9999);

            // 메인 액티비티 확인
            for(int i=0; i<taskInfo.size(); i++) {
//                Log.d("myOnReceive()", "RunningTaskInfo[" + i + "] PackageName:"+ taskInfo.get(i).topActivity.getPackageName() + ", ClassName: " + taskInfo.get(i).topActivity.getClassName());
                // 메인 액티비티 실행 중일 때
                if(taskInfo.get(i).topActivity.getClassName().equals(MyApplication.MAIN_ACTIVITY_NAME)) {
                    Log.d("myOnReceive()", "MainActivity 실행 중");
                    List<ActivityManager.RunningAppProcessInfo> processInfo = activityManager.getRunningAppProcesses();
                    // 포그라운드 & 백그라운드 확인
                    for(int j=0; j<processInfo.size(); j++) {
//                        Log.d("myOnReceive()", "RunningAppProcessInfo[" + j + "] importance: " + processInfo.get(j).importance + ", processName: " + processInfo.get(j).processName);
                        // 포그라운드 실행 중일 때
                        if(processInfo.get(j).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                                processInfo.get(j).processName.equals(MyApplication.PROCESS_NAME))
                            Log.d("myOnReceive()", "포그라운드 실행중");
                        // 백그라운드 실행 중일 때
                        else {
                            Log.d("myOnReceive()", "백그라운드 실행중");
                            activityManager.moveTaskToFront(taskInfo.get(i).id, 0); // 포그라운드로 올리기
                        }
                        ((MainActivity) MainActivity.mContext).insertSmsData(orgNumber, orgDate, orgText);    // 컨텍스트로 액티비티 메소드, 객체 접근
                    }
                    return;
                }
            }

            // 메인 액티비티 실행 중이 아닐 때
            Log.d("myOnReceive()", "MainActivity 없음");
            Intent mainIntent = new Intent(context, MainActivity.class);
            MyApplication.IS_AUTO_STARTED = true;
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mainIntent.putExtra(MainActivity.EXTRA_ORG_NUMBER, orgNumber);
            mainIntent.putExtra(MainActivity.EXTRA_ORG_DATE, orgDate);
            mainIntent.putExtra(MainActivity.EXTRA_ORG_TEXT, orgText);
            context.startActivity(mainIntent);  // 메인 액티비티 실행
            return;
        }
    }
}
