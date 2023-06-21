package com.samvmisti.bootcounter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table to save a timestamp after each device reboot
 */
@Entity
data class TimestampEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val millis: Long
)
