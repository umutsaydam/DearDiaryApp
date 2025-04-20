package com.umutsaydam.deardiary.presentation.common.functions

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.umutsaydam.deardiary.R

fun showBiometricPrompt(
    context: Context,
    onSuccess: (String) -> Unit,
    onFailed: (String) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess(context.getString(R.string.auth_succeeded))
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onFailed(context.getString(R.string.auth_error))
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed(context.getString(R.string.auth_failed))
            }
        }
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(context.getString(R.string.fingerprint_verification))
        .setSubtitle(context.getString(R.string.use_your_fingerprint))
        .setNegativeButtonText(context.getString(R.string.cancel))
        .build()

    biometricPrompt.authenticate(promptInfo)
}
