package io.techmeskills.an02onl_plannerapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import io.techmeskills.an02onl_plannerapp.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context, intent)
    }

    private fun showNotification(context: Context, intent: Intent) {
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, NotificationReceiver::class.java), 0
        )

        val notificationBuilderManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if ((notificationBuilderManager.getNotificationChannel("NOTIFICATION_CHANNEL${context.packageName}") == null)) {
                val notificationChannel = NotificationChannel(
                    "NOTIFICATION_CHANNEL${context.packageName}",
                    "NOTIFICATION_CHANNEL${context.packageName}",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationBuilderManager.createNotificationChannel(notificationChannel)
            }
        }

        val notificationBuilder = NotificationCompat.Builder(context, context.packageName)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(intent.getStringExtra("PLANNER_APP_NOTIFICATION_USER"))
            .setContentText(intent.getStringExtra("PLANNER_APP_NOTIFICATION_TEXT"))
            .setContentIntent(contentIntent)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)

        notificationBuilderManager.notify(1, notificationBuilder.build())
    }
}