package com.caseyjbrooks.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import co.touchlab.kermit.Logger
import com.copperleaf.notifications.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AndroidNotificationService(
    private val applicationContext: Context,
    private val activityContext: ComponentActivity?,
    private val logger: Logger,
) : NotificationService {

    @SuppressLint("NewApi")
    override fun isPermissionGranted(): Boolean {
        val cxt = activityContext ?: applicationContext

        val notificationEnabled = NotificationManagerCompat
            .from(cxt)
            .areNotificationsEnabled()

        val permissionGranted = (ActivityCompat.checkSelfPermission(
            cxt,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED)

        logger.i("notificationEnabled=$notificationEnabled, permissionGranted=$permissionGranted")
        return notificationEnabled && permissionGranted
    }

    override suspend fun promptForPermission() = withContext(Dispatchers.Main) {
        checkNotNull(activityContext)
        when {
            hasPermission() -> {
                // You can use the API that requires the permission.
            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                prompt()
            }
        }
    }

// Display Notifications
// ---------------------------------------------------------------------------------------------------------------------

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

// Check and request Notification permissions
// ---------------------------------------------------------------------------------------------------------------------

    @SuppressLint("InlinedApi")
    private fun hasPermission(): Boolean {
        val cxt = activityContext ?: applicationContext
        return ContextCompat
            .checkSelfPermission(cxt, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("InlinedApi")
    private fun shouldShowRationaleUi(): Boolean {
        checkNotNull(activityContext)
        return ActivityCompat
            .shouldShowRequestPermissionRationale(activityContext, Manifest.permission.POST_NOTIFICATIONS)
    }

    @SuppressLint("InlinedApi")
    private suspend fun prompt() {
        checkNotNull(activityContext)

        ActivityCompat.requestPermissions(
            activityContext,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            PermissionsDomainEvents.PermissionType.Notifications.ordinal,
        )
    }
}
