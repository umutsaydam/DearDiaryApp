package com.umutsaydam.deardiary.presentation.entryPin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.PinStateEnum
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.PinPanel
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.Constants.PIN_LENGTH
import com.umutsaydam.deardiary.util.popBackStackOrIgnore
import com.umutsaydam.deardiary.util.safeNavigate
import kotlinx.coroutines.delay

@Composable
fun EntryPinScreen(
    navController: NavHostController,
    isFingerprintEnable: Boolean,
    entryPinScreenViewModel: EntryPinScreenViewModel = hiltViewModel()
) {
    val pinState = entryPinScreenViewModel.pinState.collectAsState().value
    val enteredPin = entryPinScreenViewModel.enteredPin.collectAsState().value
    val pinList = enteredPin.map { it.toString().toInt() }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    BaseScaffold(
        navigation = {
            if (isFingerprintEnable) {
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
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) { paddingValues ->
        pinState?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(it.message)

                when (it) {
                    PinStateEnum.ENTER_CURRENT_PIN -> {
                        PinPanel(
                            pin = pinList,
                            maxLength = PIN_LENGTH,
                            pinText = enteredPin,
                            focusRequester = focusRequester,
                            onValueChange = { value ->
                                entryPinScreenViewModel.updateEnteredPin(value)
                            }
                        )

                    }

                    PinStateEnum.DONE -> {
                        navController.safeNavigate(Route.Diaries.route)
                    }

                    else -> {}
                }
            }
        }
    }
}