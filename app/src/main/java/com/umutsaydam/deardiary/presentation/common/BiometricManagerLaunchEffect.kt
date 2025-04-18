package com.umutsaydam.deardiary.presentation.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.umutsaydam.deardiary.presentation.common.functions.showBiometricPrompt

@SuppressLint("SwitchIntDef")
@Composable
fun BiometricManagerLaunchEffect(
    onSuccess: () -> Unit,
    onFailed: () -> Unit,
    onSensorError: (String) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                showBiometricPrompt(
                    context = context,
                    onSuccess = { onSuccess() },
                    onFailed = { onFailed() }
                )
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onSensorError("There is no fingerprint sensor.")
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onSensorError("Fingerprint sensor is unavailable.")
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onSensorError("There is no any defined fingerprint.")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG
                        )
                    }
                    (context as? Activity)?.startActivity(enrollIntent)
                }
            }
        }
    }
}