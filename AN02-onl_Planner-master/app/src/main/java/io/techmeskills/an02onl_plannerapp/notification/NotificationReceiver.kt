package io.techmeskills.an02onl_plannerapp.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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

        val notificationBuilder = NotificationCompat.Builder(context, context.packageName)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(intent.getStringExtra("PLANNER_APP_NOTIFICATION_USER"))
            .setContentText(intent.getStringExtra("PLANNER_APP_NOTIFICATION_TEXT"))
            .setContentIntent(contentIntent)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)
        val notificationBuilderManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilderManager.notify(1, notificationBuilder.build())
    }
}