package com.samvmisti.bootcounter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samvmisti.bootcounter.data.model.TimestampDao
import com.samvmisti.bootcounter.data.model.TimestampEntity

@Database(
    entities = [TimestampEntity::class],
    version = 1
)
abstract class MainDatabase: RoomDatabase() {
    abstract fun timestampDao(): TimestampDao
}
