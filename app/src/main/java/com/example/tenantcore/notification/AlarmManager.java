package com.example.tenantcore.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.ui.TenantCoreActivity;
import java.util.Calendar;
import java.util.HashMap;

public class AlarmManager {
  private static final HashMap<Request, PendingIntent> alarms = new HashMap<>();

  public static void set(TenantCoreActivity activity, Request request) {
    // Don't set alarms in the past and don't process tasks without a due date
    if (request.isOverdue() || request.getDueDate() == null)
      return;

    // Grab the alarm manager
    android.app.AlarmManager alarmManager = (android.app.AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

    // Passing our intent data
    Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
    alarmIntent.putExtras(request.toBundle());

    // Craft the pending intent
    PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    // Save request -> pendingIntent
    alarms.put(request, pendingIntent);

    // Use the request date, but set the time to 12:00PM
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(request.getDueDate());
    calendar.set(Calendar.HOUR_OF_DAY, 12);

    // Set the alarm
    alarmManager.set(
      android.app.AlarmManager.RTC_WAKEUP,
      calendar.getTimeInMillis(),
      pendingIntent
    );
  }

  public static void cancel(TenantCoreActivity activity, Request task) {
    // If there's no currently stored intent with `task`, return
    if (!alarms.containsKey(task))
      return;

    // Grab the alarm manager
    android.app.AlarmManager alarmManager = (android.app.AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

    // Cancel the alarm
    alarmManager.cancel(alarms.get(task));

    // Delete the task from alarms
    alarms.remove(task);
  }

  public static void update(TenantCoreActivity activity, Request request) {
    // Cancel the current set alarm for this request
    cancel(activity, request);

    // Set a new alarm for this request
    set(activity, request);
  }
}
