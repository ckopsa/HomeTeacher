package kopsabros.hometeacher.notifications;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import kopsabros.hometeacher.content.FamilyContent;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("HOMETEACH", "Broadcast Received.");
        new NotificationPublisher().sendNotification(context);
    }
}
