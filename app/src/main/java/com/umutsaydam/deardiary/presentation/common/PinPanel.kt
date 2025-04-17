package com.umutsaydam.deardiary.presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.util.Constants.PIN_LENGTH

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