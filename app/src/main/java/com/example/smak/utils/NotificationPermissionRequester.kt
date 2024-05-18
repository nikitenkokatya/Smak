package com.example.smak.utils
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotificationPermissionRequester(private val activity: ComponentActivity) {

    private val requestPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (activity.shouldShowRequestPermissionRationale((Manifest.permission.POST_NOTIFICATIONS))) //5b explicamos al usuario
                            showNotificationPermissionRationale()
                        else
                            showSettingDialog()

                    }

            } else {

                activity.showToast("Permiso asignado")
            }
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            activity,
            R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${activity.packageName}")
                activity.startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(
            activity,
            R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    requestPermissionLauncher?.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun tryRequest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        return requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}