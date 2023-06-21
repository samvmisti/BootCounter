package com.samvmisti.bootcounter.reboot

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.samvmisti.bootcounter.R
import com.samvmisti.bootcounter.data.MainDatabase
import com.samvmisti.bootcounter.data.model.TimestampEntity
import com.samvmisti.bootcounter.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class RebootTrackingService : Service() {

    private val database: MainDatabase by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Reboot Service started")
        startForeground()
        CoroutineScope(Dispatchers.IO).launch {
            val currentMillis = System.currentTimeMillis()
            val inserted =
                database.timestampDao().insert(TimestampEntity(millis = currentMillis))
            if (inserted != 0L) {
                Log.d(TAG, "inserted value with id $inserted")
                startNotificationsWorker(currentMillis)
                stopService()
            }
        }
        return START_NOT_STICKY
    }

    private fun startNotificationsWorker(millis: Long) {
        val inputData = Data.Builder()
            .putLong(NotificationWorker.ARG_LAST_MILLIS, millis)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = 15, // 15 minutes
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).setInputData(inputData).build()

        WorkManager.getInstance(this).enqueue(workRequest)
        Log.d(TAG, "NotificationWorker started")
    }

    private fun stopService() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    private fun startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startAndroidOForeground()
        else startForeground(FOREGROUND_ID, buildForegroundNotification())
    }

    private fun startAndroidOForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                CHANNEL_ID,
                NAME,
                NotificationManager.IMPORTANCE_NONE
            )
            chan.lightColor = Color.RED
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            manager.createNotificationChannel(chan)
            startForeground(2, buildForegroundNotification())
        }
    }

    private fun buildForegroundNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle(NAME)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
        return notificationBuilder.build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private companion object {
        const val CHANNEL_ID = "reboot-foreground"
        const val NAME = "reboot-service"
        const val FOREGROUND_ID = 951
        const val TAG = "RebootTrackingService:: "
    }
}
