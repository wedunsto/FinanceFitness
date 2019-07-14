package com.example.financefitness;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotifyMe extends MainActivity{
    private NotificationCompat.Builder notify;//Creates notification
    private NotificationManagerCompat notifyMC;
    private NotificationChannel channel;//Creates channel to push notification
    private NotificationManager notifyM;
    private Context mContext;//Used to hold Context

    public NotifyMe(Context mContext){
        this.mContext = mContext;
    }

    public void setUpNotification(){//build notification
        notify = new NotificationCompat.Builder(mContext,"1");
        notifyMC = NotificationManagerCompat.from(mContext);
        channel = new NotificationChannel("1","Exercise",NotificationManager.IMPORTANCE_DEFAULT);
        notifyM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notify.setSmallIcon(R.drawable.cast_ic_notification_on);
        notify.setContentTitle("Title");
        notify.setContentText("Contents");
        notify.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        channel.setDescription("Description");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpNotification();
    }

    public void creatNotification(){
        notifyM.createNotificationChannel(channel);
        notifyMC.notify(1,notify.build());
    }
}
