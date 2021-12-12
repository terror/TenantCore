package com.example.tenantcore.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.tenantcore.R;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.ui.TenantCoreActivity;

public class AlarmReceiver extends BroadcastReceiver {
  private static int alarmNotificationId = 0;

  @Override
  public void onReceive(Context context, Intent intent) {
    // intent back to the activity
    Intent resultIntent = new Intent(context, TenantCoreActivity.class)
      .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    // grab the new task and put it on `resultIntent`
    Request request = Request.fromBundle(intent.getExtras());
    resultIntent.putExtras(request.toBundle());

    // create a notification
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TenantCoreActivity.REQUESTS_NOTIFICATION_CHANNEL)
      .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
      .setContentText(request.getDescription())
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setGroup(TenantCoreActivity.REQUESTS_NOTIFICATION_GROUP)
      .setContentIntent(PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT));

    // notificationId is a unique int for each notification that you must define
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    notificationManager.notify(alarmNotificationId++, builder.build());
  }
}
