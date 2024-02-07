package com.caseyjbrooks.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import co.touchlab.kermit.Logger
import com.copperleaf.notifications.R

internal class AndroidNotificationService(
    private val applicationContext: Context,
    private val logger: Logger,
) : NotificationService {

    @SuppressLint("NewApi")
    override fun isPermissionGranted(): Boolean {
        val notificationEnabled = NotificationManagerCompat
            .from(applicationContext)
            .areNotificationsEnabled()
        val permissionGranted =
            (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)

        logger.i("notificationEnabled=$notificationEnabled, permissionGranted=$permissionGranted")
        return notificationEnabled && permissionGranted
    }

    override fun promptForPermission() {
    }

    override fun showNotification(
        channelId: String,
        notificationId: String,
        title: String,
        message: String,
    ) {
        logger.i("Displaying notification: channelId=$channelId, notificationId=$notificationId, title=$title, message=$message")
        createNotificationChannel(channelId)

        if (!isPermissionGranted()) {
            logger.i("Permission not granted for channelId=$channelId, notificationId=$notificationId")
            return
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat
            .from(applicationContext)
            .notify(channelId.hashCode() + notificationId.hashCode(), builder.build())
    }

    private fun createNotificationChannel(channelId: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat
                .from(applicationContext)
                .createNotificationChannel(
                    NotificationChannel(
                        channelId,
                        channelId,
                        NotificationManager.IMPORTANCE_DEFAULT,
                    ).apply {
                        description = channelId
                    }
                )
        }
    }
}
