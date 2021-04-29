package io.techmeskills.an02onl_plannerapp.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class Note (
        @PrimaryKey (autoGenerate = true) val id: Long = 0L,
        val title: String,
        val date: String,
        val userId: Long = -1,
        val fromCloud: Boolean = false
) : Parcelable