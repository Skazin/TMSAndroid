package io.techmeskills.an02onl_plannerapp.repositories

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.notification.NotificationReceiver
import java.text.SimpleDateFormat
import java.util.*

class NotificationRepository(private val context: Context, private val alarmManager: AlarmManager) {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun setNotification(note: Note) {

        val alarmTimeAtUTC = dateFormatter.parse(note.date)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC!!.time,
            makeIntent(note)
        )
    }

    fun unsetNotification(note: Note) {
        alarmManager.cancel(makeIntent(note))
    }

    private fun makeIntent(note: Note): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.action = "PLANNER_APP_NOTIFICATION"
        intent.putExtra("PLANNER_APP_NOTIFICATION_TEXT", note.title)
        intent.putExtra("PLANNER_APP_NOTIFICATION_USER", note.userName)
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }
}