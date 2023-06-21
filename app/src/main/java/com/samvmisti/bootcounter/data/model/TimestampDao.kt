package com.samvmisti.bootcounter.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimestampDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TimestampEntity): Long

    @Query("SELECT * FROM timestampentity ORDER BY millis DESC")
    suspend fun getAll(): List<TimestampEntity>
}
