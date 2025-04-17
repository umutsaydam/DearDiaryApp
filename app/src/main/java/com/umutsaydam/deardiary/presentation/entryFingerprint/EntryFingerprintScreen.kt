package com.umutsaydam.deardiary.presentation.entryFingerprint

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.safeNavigate

@SuppressLint("SwitchIntDef")
@Composable
fun EntryFingerprintScreen(
    navController: NavHostController,
    entryFingerprintViewModel: EntryFingerprintViewModel = EntryFingerprintViewModel()
) {
    val context = LocalContext.current
    val toastMessage = entryFingerprintViewModel.uiMessageState.collectAsState().value

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            entryFingerprintViewModel.clearUiMessageState()
        }
    }

    LaunchedEffect(Unit) {
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                showBiometricPrompt(
                    context = context,
                    onSuccess = {
                        navController.safeNavigate(Route.Diaries.route)
                    },
                    onFailed = {}
                )
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                entryFingerprintViewModel.updateUiMessageState("There is no fingerprint sensor.")
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                entryFingerprintViewModel.updateUiMessageState("Fingerprint sensor is unavailable.")
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                entryFingerprintViewModel.updateUiMessageState("There is no any defined fingerprint.")

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
    BaseScaffold(
        topActions = {
            TextButton(
                onClick = {
                    navController.safeNavigate("EntryPin/true")
                },
            ) {
                Text("Use pin")
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_finger_print),
                contentDescription = "Fingerprint",
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Scan your fingerprint.", style = MaterialTheme.typography.titleMedium)
        }
    }
}

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
                onSuccess("Authentication Succeeded.")
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onFailed("Authentication error.")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed("Authentication failed.")
            }
        }
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Fingerprint Verification")
        .setSubtitle("Use your fingerprint for security")
        .setNegativeButtonText("Cancel")
        .build()

    biometricPrompt.authenticate(promptInfo)
}