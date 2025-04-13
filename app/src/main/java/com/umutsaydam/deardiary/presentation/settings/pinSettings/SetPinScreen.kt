package com.umutsaydam.deardiary.presentation.settings.pinSettings

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.PinStateEnum
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.util.Constants.PIN_LENGTH
import com.umutsaydam.deardiary.util.popBackStackOrIgnore
import kotlinx.coroutines.delay

@Composable
fun SetPinScreen(navController: NavHostController) {
    val focusRequester = remember { FocusRequester() }
    var pinText by remember { mutableStateOf("") }
    var pinTextConfirm by remember { mutableStateOf("") }
    val maxLength = PIN_LENGTH
    val pin = pinText.map { it.toString().toInt() }
    val pinConfirm = pinTextConfirm.map { it.toString().toInt() }
    var pinState by remember { mutableStateOf(PinStateEnum.ENTER_FIRST) }
    var toastMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(pinState) {
        delay(300)
        focusRequester.requestFocus()
    }

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            toastMessage = ""
        }
    }

    BaseScaffold(
        navigation = {
            IconButton(
                onClick = {
                    navController.popBackStackOrIgnore()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back_filled),
                    contentDescription = "Back to the previous screen"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (pinState == PinStateEnum.ENTER_FIRST) {
                Text("Enter your pin.")
                PinPanel(
                    pin = pin,
                    maxLength = maxLength,
                    pinText = pinText,
                    focusRequester = focusRequester,
                    onValueChange = { value ->
                        pinText = value
                        Log.i("R/T", "pin: $pin")
                        if (pinText.length == maxLength) {
                            Log.i("R/T", "Switching pin confirm mode.")
                            pinState = PinStateEnum.CONFIRM_PIN
                        }
                    }
                )
            } else if (pinState == PinStateEnum.CONFIRM_PIN) {
                Text("Confirm your pin.")
                PinPanel(
                    pin = pinConfirm,
                    maxLength = maxLength,
                    pinText = pinTextConfirm,
                    focusRequester = focusRequester,
                    onValueChange = { value ->
                        pinTextConfirm = value
                        if (pinTextConfirm.length == maxLength) {
                            if (pinText == pinTextConfirm) {
                                Log.i("R/T", "System is working properly.")
                                toastMessage = "Your pin has been saved."
                                navController.popBackStackOrIgnore()
                            } else {
                                toastMessage = "Pins are not matching."
                                Log.i("R/T", "Pins do not match.")
                                pinText = ""
                                pinTextConfirm = ""
                                pinState = PinStateEnum.ENTER_FIRST
                            }
                        }
                    }
                )
            }

        }
    }
}

@Composable
fun PinPanel(
    modifier: Modifier = Modifier,
    pin: List<Int>,
    maxLength: Int,
    pinText: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit
) {
    Row {
        (0 until PIN_LENGTH).forEach {
            Icon(
                modifier = modifier
                    .padding(8.dp)
                    .size(28.dp),
                painter = painterResource(if (it < pin.size) R.drawable.ic_circle_filled else R.drawable.ic_circle_outline),
                contentDescription = "Pin",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    BasicTextField(
        value = pinText,
        onValueChange = {
            if (it.length <= maxLength && it.all { c -> c.isDigit() }) {
                onValueChange(it)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword
        ),
        modifier = Modifier
            .focusRequester(focusRequester)
            .onPreviewKeyEvent {
                if (it.type == KeyEventType.KeyDown && it.key == Key.Backspace) {
                    if (pinText.isNotEmpty()) {
                        onValueChange(pinText.dropLast(1))
                    }
                    true
                } else {
                    false
                }
            }
            .alpha(0f)
    )
}