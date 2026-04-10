package com.example.bai5_firebase.service;

import androidx.annotation.NonNull;

import com.example.bai5_firebase.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class AppFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = "Nhắc giờ chiếu";
        String body = "Bạn có suất chiếu sắp diễn ra.";

        if (remoteMessage.getNotification() != null) {
            if (remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            }
            if (remoteMessage.getNotification().getBody() != null) {
                body = remoteMessage.getNotification().getBody();
            }
        }

        NotificationUtils.createChannel(this);
        NotificationUtils.showNotification(this, (int) System.currentTimeMillis(), title, body);
    }
}
