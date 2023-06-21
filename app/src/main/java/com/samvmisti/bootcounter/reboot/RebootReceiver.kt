package com.samvmisti.bootcounter.reboot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d(this::class.java.simpleName, "Reboot action received")
            val serviceIntent = Intent(context, RebootTrackingService::class.java)
            context.startService(serviceIntent)
        }
    }
}
