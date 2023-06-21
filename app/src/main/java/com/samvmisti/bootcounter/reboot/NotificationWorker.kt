package com.samvmisti.bootcounter.reboot

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.samvmisti.bootcounter.time.Time
import com.samvmisti.bootcounter.R

class NotificationWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        createNotification()
        return Result.success()
    }

    private fun createNotification() {
        val lastTimeRebooted = inputData.getLong(ARG_LAST_MILLIS, 0L)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = context.getString(R.string.channel_description)
            notificationManager.createNotificationChannel(channel)
        }
        val vibrationPattern = longArrayOf(0, 500, 200, 500)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.device_was_rebooted))
            .setContentText(Time.convertMillisToString(lastTimeRebooted))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVibrate(vibrationPattern)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_SOUND)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    companion object {
        private const val CHANNEL_ID = "notification_channel_id"
        private const val NOTIFICATION_ID = 1
        const val ARG_LAST_MILLIS = "com.samvmisti.bootcounter.reboot.NotificationWorker.ARG_LAST_MILLIS"
    }
}
