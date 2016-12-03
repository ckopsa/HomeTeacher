package coljamkop.tabtest.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import coljamkop.tabtest.MainActivity;
import coljamkop.tabtest.R;

/**
 * Created by Aghbac on 6/20/16.
 */
public class NotificationPublisher {

    public void sendNotification(Context context) {
        String title = "Reminder";
        String message = "One or more families need appointments set";
        int mNotificationId = 001;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true);

        Intent openMainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent openMainActivityPendingIntent = PendingIntent.getActivity(
                context,
                0,
                openMainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mBuilder.setContentIntent(openMainActivityPendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
