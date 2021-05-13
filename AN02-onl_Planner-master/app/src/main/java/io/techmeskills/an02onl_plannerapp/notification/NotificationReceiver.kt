package io.techmeskills.an02onl_plannerapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import io.techmeskills.an02onl_plannerapp.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context, intent)
    }

    private fun showNotification(context: Context, intent: Intent) {
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, NotificationReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilderManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationBuilderManager)
        }

        val noteId = intent.getLongExtra(NOTIFICATION_KEY_NOTE_ID, -1)
        val noteOwner = intent.getStringExtra(NOTIFICATION_KEY_NOTE_OWNER)
        val noteText = intent.getStringExtra(NOTIFICATION_KEY_NOTE_TEXT)


        val notificationBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Hello, $noteOwner")
                .setContentText("May I remind of your business: $noteText")
                .setContentIntent(contentIntent)
                .addAction(deleteAction(context, noteId))
                .addAction(postponeAction(context, noteId))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)

        notificationBuilderManager.notify(0, notificationBuilder.build())
    }

    private fun deleteAction(context: Context, noteId: Long): NotificationCompat.Action {
        val deleteIntent = Intent(context.applicationContext, NotificationService::class.java)
        deleteIntent.action = ACTION_DELETE
        deleteIntent.putExtra(NOTIFICATION_KEY_NOTE_ID, noteId)

        val pendingDeleteIntent = PendingIntent.getService(
            context.applicationContext,
            9999,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_delete_x_black,
            "Delete",
            pendingDeleteIntent
        ).build()
    }

    private fun postponeAction(context: Context, noteId: Long): NotificationCompat.Action {
        val postponeIntent = Intent(context.applicationContext, NotificationService::class.java)
        postponeIntent.action = ACTION_POSTPONE
        postponeIntent.putExtra(NOTIFICATION_KEY_NOTE_ID, noteId)

        val pendingPostponeIntent = PendingIntent.getService(
            context.applicationContext,
            1111,
            postponeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_postpone_black,
            "Postpone",
            pendingPostponeIntent
        ).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationManager: NotificationManager) {
        if ((notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL) == null)) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val ACTION = "PLANNER_APP_NOTIFICATION"
        private const val NOTIFICATION_CHANNEL = "PLANNER_APP_NOTIFICATION_CHANNEL"
        const val ACTION_DELETE = "PLANNER_APP_NOTIFICATION_DELETE"
        const val ACTION_POSTPONE = "PLANNER_APP_NOTIFICATION_POSTPONE"
        const val NOTIFICATION_KEY_NOTE_ID = "PLANNER_APP_NOTIFICATION_KEY_NOTE_ID"
        const val NOTIFICATION_KEY_NOTE_TEXT = "PLANNER_APP_NOTIFICATION_KEY_NOTE_TEXT"
        const val NOTIFICATION_KEY_NOTE_OWNER = "PLANNER_APP_NOTIFICATION_KEY_NOTE_OWNER"
    }
}