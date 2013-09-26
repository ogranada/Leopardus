package com.framework.leopardus.utils;

import java.util.Random;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

@SuppressWarnings("rawtypes")
public class NotificationsHelper {

	public static int getId() {
		return (new Random()).nextInt();
	}

	public static int sendNotification(Context ctx, int smallIcon,
			String titile, String content, Class clazz) {
		int id = getId();
		updateNotification(ctx, id, smallIcon, titile, content, clazz, true, null);
		return id;
	}

	public static void updateNotification(Context ctx, int id, int smallIcon,
			String titile, String content, Class clazz) {
		updateNotification(ctx, id, smallIcon, titile, content, clazz, true, null);
	}
	
	public static int sendNotification(Context ctx, int smallIcon,
			String titile, String content, Class clazz, boolean autoCancel,
			String[] multiLines) {
		int id = getId();
		updateNotification(ctx, id, smallIcon, titile, content, clazz, autoCancel, multiLines);
		return id;
	}

	public static void updateNotification(Context ctx, int id, int smallIcon,
			String titile, String content, Class clazz, boolean autoCancel,
			String[] multiLines) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				ctx).setSmallIcon(smallIcon).setContentTitle(titile)
				.setContentText(content);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
		if (clazz != null) {
			Intent resultIntent = new Intent(ctx, clazz);
			stackBuilder.addParentStack(clazz);
			stackBuilder.addNextIntent(resultIntent);
		}
		if(multiLines!=null && multiLines.length>0){
			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			inboxStyle.setBigContentTitle(content);
			for (int i = 0; i < multiLines.length; i++) {
				inboxStyle.addLine(multiLines[i]);
			}
			mBuilder.setStyle(inboxStyle);
		}
		mBuilder.setAutoCancel(autoCancel);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(id, mBuilder.build());
	}

}
