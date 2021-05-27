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

    fun postponeNotification(note: Note): Note {
        val currentTime = dateFormatter.parse(note.date)!!
        val calendar = Calendar.getInstance()
        calendar.time = currentTime
        calendar.add(Calendar.MINUTE, 5)
        return note.copy(date = dateFormatter.format(calendar.time))
    }

    private fun makeIntent(note: Note): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.action = NotificationReceiver.ACTION
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_ID, note.id)
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_TEXT, note.title)
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_OWNER, note.userName)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}