package com.umutsaydam.deardiary.presentation.addDiary

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.sealedStates.UiMessage
import com.umutsaydam.deardiary.domain.sealedStates.UiState
import com.umutsaydam.deardiary.presentation.Dimens.PaddingXSmall
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.BottomXRMenuWithGesture
import com.umutsaydam.deardiary.presentation.common.LoadingCircular
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.popBackStackOrIgnore
import com.umutsaydam.deardiary.util.safeNavigateWithClearingBackStack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDiaryScreen(
    navController: NavController,
    addDiaryViewModel: AddDiaryViewModel = hiltViewModel()
) {
    val uiMessageState by addDiaryViewModel.uiMessageState.collectAsState()
    val addDiaryUiState by addDiaryViewModel.addDiaryUiState.collectAsState()
    val diaryText by addDiaryViewModel.diaryContent.collectAsState()
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )
    var isDatePickerOpen by remember { mutableStateOf(false) }
    val selectedIndex by addDiaryViewModel.diaryEmotion.collectAsState()
    val context = LocalContext.current

    val selectedDate by addDiaryViewModel.selectedDateMillis.collectAsState()

    LaunchedEffect(uiMessageState) {
        when (val state = uiMessageState) {
            is UiMessage.Success -> {
                Toast.makeText(context, context.getString(state.message), Toast.LENGTH_SHORT).show()
                addDiaryViewModel.clearUiMessageState()
            }

            is UiMessage.Error -> {
                Toast.makeText(context, context.getString(state.message), Toast.LENGTH_SHORT).show()
                if (state.statusCode != null && state.statusCode == 401) {
                    navController.safeNavigateWithClearingBackStack(Route.Auth.route)
                }
                addDiaryViewModel.clearUiMessageState()
            }

            else -> {}
        }
    }

    BaseScaffold(
        title = { Text(stringResource(R.string.write_diary)) },
        topActions = {
            IconButton(
                onClick = {
                    addDiaryViewModel.saveDiaryServer()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check_filled),
                    contentDescription = stringResource(R.string.save_diary)
                )
            }
        },
        navigation = {
            IconButton(
                onClick = {
                    navController.popBackStackOrIgnore()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back_filled),
                    contentDescription = stringResource(R.string.back_prev_screen)
                )
            }
        }
    ) { paddingValues ->
        if (addDiaryUiState is UiState.Loading) {
            LoadingCircular()
        }

        if (isDatePickerOpen) {
            DairyDatePickerDialog(
                datePickerState = datePickerState,
                onSelectedDate = { selectedDate ->
                    addDiaryViewModel.updateSelectedDate(selectedDate)
                },
                onDismissed = {
                    isDatePickerOpen = false
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TextButton(
                    onClick = { isDatePickerOpen = true }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_date_filled),
                        contentDescription = stringResource(R.string.select_a_date)
                    )

                    Text(
                        text = addDiaryViewModel.formatForSelectedDate(selectedDate),
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                BasicTextField(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(PaddingXSmall),
                    value = diaryText,
                    onValueChange = { addDiaryViewModel.updateDiaryContent(it) }
                )
            }
        }

        BottomXRMenuWithGesture(
            selectedIndex = selectedIndex,
            paddingValues = paddingValues,
            onMoodSelected = { mood ->
                addDiaryViewModel.updateDiaryEmotion(mood)
            },
            onTemplateSelected = { templateDiaryContents ->
                addDiaryViewModel.updateDiaryContent(
                    templateDiaryContents + "\n" + diaryText
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DairyDatePickerDialog(
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState,
    onSelectedDate: (Long) -> Unit,
    onDismissed: () -> Unit,
) {
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = { onDismissed() },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onSelectedDate(it)
                    }
                    onDismissed()
                }
            ) {
                Text(stringResource(R.string.select))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissed() }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}