package io.techmeskills.an02onl_plannerapp.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
open class Note (
        @PrimaryKey (autoGenerate = true) val id: Long = 0L,
        val title: String,
        val date: String? = null) : Parcelable