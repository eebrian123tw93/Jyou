package twb.brianlu.com.firebasetest.service;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;
import java.util.Random;

import twb.brianlu.com.firebasetest.R;
import twb.brianlu.com.firebasetest.chat.ChatActivity;
import twb.brianlu.com.firebasetest.core.BaseApplication;
import twb.brianlu.com.firebasetest.core.BasePresenter;
import twb.brianlu.com.firebasetest.navigation.NavigationActivity;

public class FirebaseService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseService";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        Map<String,String>datas=remoteMessage.getData();
        String title="";
        String body="";
        for(Map.Entry<String, String> entry:datas.entrySet()){
            Log.i(TAG, "key " + entry.getKey()+" value "+entry.getValue());
            if(entry.getKey().equals("title")){
                title=entry.getValue();
            }
            if(entry.getKey().equals("body")){
                body=entry.getValue();
            }
        }

//        if (remoteMessage.getNotification() != null) {
//            Log.i(TAG, "title " + remoteMessage.getNotification().getTitle());
//            Log.i(TAG, "body " + remoteMessage.getNotification().getBody());
//        }
        if (!isAppOnForeground(BaseApplication.getContext())) {
            if (BasePresenter.isLogin()) {
                notification(title, body);
                System.out.println(title+body);
            }
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.i("FirebaseService", "token " + token);
        BasePresenter.saveToken(token);
        BasePresenter.readUser();
    }

    private void notification(String title, String body) {

        // notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        RemoteViews remoteViews = new RemoteViews(getPackageName(),);
//        remoteViews.setTextViewText(
//                R.id.notification_step_textview,
//                getString(R.string.today_step_string)
//                        + ": "
//                        + dailyDataModel.getSteps()
//                        + getString(R.string.step_string));
//        remoteViews.setTextViewText(
//                R.id.notification_sleep_textview,
//                getString(R.string.yesterday_sleep_string)
//                        + ": "
//                        + dailyDataModel.getHoursOfSleep()
//                        + getString(R.string.hour_string));
//        remoteViews.setTextViewText(
//                R.id.notification_drink_textview,
//                getString(R.string.today_drink_string)
//                        + ": "
//                        + dailyDataModel.getWaterCC()
//                        + getString(R.string.cc_string));
//        remoteViews.setTextViewText(
//                R.id.notification_use_phone_textview,
//                getString(R.string.today_use_phone_string)
//                        + ": "
//                        + dailyDataModel.getHoursPhoneUse()
//                        + getString(R.string.hour_string));

        int notificationId = new Random().nextInt(1000)+1000;
        String channelId = "daily_data_channel_id";

        Intent notificationIntent = new Intent(this, ChatActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(NavigationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId);
        notificationBuilder
                .setAutoCancel(true)

//                .setCustomContentView(remoteViews)
                .setSmallIcon(R.drawable.make_friends_color)
                .setContentIntent(resultPendingIntent)
//                .setCustomBigContentView(remoteViews)
                .setContentTitle(title)
                .setContentText(body);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int important = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId, "daily_data_channel_name", important);
            notificationBuilder.setChannelId(channelId);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}