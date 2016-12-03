package kopsabros.hometeacher.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kopsabros.hometeacher.Content.FamilyContent;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // Does there exist a family without appt?
        for (FamilyContent.Family family :
                FamilyContent.FAMILIES) {
            if (family.getNextAppointment() == null) {
                new NotificationPublisher().sendNotification(context);
                break;
            }
        }
    }
}
