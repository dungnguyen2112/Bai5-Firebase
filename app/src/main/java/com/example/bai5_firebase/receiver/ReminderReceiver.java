package com.example.bai5_firebase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bai5_firebase.util.NotificationUtils;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String movieTitle = intent.getStringExtra("movieTitle");
        String theaterName = intent.getStringExtra("theaterName");
        String body = "Sắp tới giờ chiếu " + movieTitle + " tại " + theaterName;

        NotificationUtils.createChannel(context);
        NotificationUtils.showNotification(context, (int) System.currentTimeMillis(), "Nhắc giờ chiếu", body);
    }
}
