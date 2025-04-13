package com.umutsaydam.deardiary.presentation.addDiary

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.EmotionEntity
import com.umutsaydam.deardiary.domain.emotionList
import com.umutsaydam.deardiary.domain.templateList
import com.umutsaydam.deardiary.presentation.addDiary.diaryMood.DiaryMoodItem
import com.umutsaydam.deardiary.presentation.addDiary.diaryTemplate.DiaryTemplateDialog
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.util.popBackStackOrIgnore
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDiaryScreen(navController: NavController) {
    var diaryText by remember { mutableStateOf("") }
    var isTemplateDialogOpen by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )
    var isDatePickerOpen by remember { mutableStateOf(false) }
    var isMoodDialogOpen by remember { mutableStateOf(false) }
    var isLongPressingMoodButton by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }

    BaseScaffold(
        title = "Write a diary",
        topActions = {
            IconButton(
                onClick = {

                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check_filled),
                    contentDescription = "Save the dairy"
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
                    contentDescription = "Back to the previous screen"
                )
            }
        }
    ) { paddingValues ->

        if (isDatePickerOpen) {
            DairyDatePickerDialog(
                datePickerState = datePickerState,
                onSelectedDate = { selectedDate ->

                },
                onDismissed = {
                    isDatePickerOpen = false
                }
            )
        }

        if (isTemplateDialogOpen) {
            DiaryTemplateDialog(
                templateList = templateList,
                onTemplateSelected = { selectedTemplate ->

                },
                onDismissed = { isTemplateDialogOpen = false }
            )
        }

        if (isMoodDialogOpen && isLongPressingMoodButton) {
            DairyMoodPopup(
                emotionList = emotionList,
                selectedIndex = selectedIndex,
                onDismissed = {
                    isMoodDialogOpen = false
                    isLongPressingMoodButton = false
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
                        contentDescription = "Select a date"
                    )

                    Text(
                        text = "10 April 2025",
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                BasicTextField(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    value = diaryText,
                    onValueChange = { diaryText = it }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .imePadding()
                .pointerInput(isMoodDialogOpen) {
                    if (isMoodDialogOpen) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val pos = event.changes.first().position
                                val popupWidthPx = 300.dp.toPx()
                                val popupStartX = (screenWidthPx - popupWidthPx) / 2f
                                val localX = pos.x - popupStartX
                                val spacing = 4.dp.toPx()
                                val totalSpacing = spacing * (emotionList.size - 1)
                                val availableWidth = 300.dp.toPx() - totalSpacing
                                val itemWidth = availableWidth / emotionList.size
                                val fullItemWidth = itemWidth + spacing

                                val index = (localX / fullItemWidth)
                                    .toInt()
                                    .coerceIn(0, emotionList.lastIndex)


                                selectedIndex = index

                                if (event.changes
                                        .first()
                                        .changedToUp()
                                ) {
                                    emotionList[index]
                                    isMoodDialogOpen = false
                                    isLongPressingMoodButton = false
                                    break
                                }
                            }
                        }
                    }
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomXRMenu(
                onTemplateDialogOpen = { isTemplateDialogOpen = true },
                onLongPress = {
                    isMoodDialogOpen = true
                    isLongPressingMoodButton = true
                    Log.i("R/T", "long pressed $isMoodDialogOpen && $isLongPressingMoodButton")
                },
                onDismissed = {
                    isMoodDialogOpen = false
                    isLongPressingMoodButton = false
                }
            )
        }
    }
}

@Composable
fun DairyMoodPopup(
    emotionList: List<EmotionEntity>,
    onDismissed: () -> Unit,
    selectedIndex: Int
) {
    Popup(
        alignment = Alignment.BottomCenter,
        onDismissRequest = { onDismissed() },
        offset = IntOffset(0, -235)
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 4.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                items(count = emotionList.size, key = { it }) { index ->
                    DiaryMoodItem(
                        emotionEntity = emotionList[index],
                        isSelected = selectedIndex == index
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DairyDatePickerDialog(
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState,
    onSelectedDate: (String) -> Unit,
    onDismissed: () -> Unit,
) {
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = { onDismissed() },
        confirmButton = {
            TextButton(
                onClick = {
                    val dateFormat = SimpleDateFormat("dd-MM-yyy", Locale.getDefault())
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        dateFormat.format(it)
                    } ?: ""
                    onSelectedDate(selectedDate)
                    onDismissed()
                }
            ) {
                Text("Select")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissed() }
            ) {
                Text("Dismiss")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Composable
fun BottomXRMenu(
    modifier: Modifier = Modifier,
    onTemplateDialogOpen: () -> Unit,
    onLongPress: () -> Unit,
    onDismissed: () -> Unit,
) {
    Row(
        modifier = modifier
            .width(300.dp)
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { onTemplateDialogOpen() },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_sticky_note_outline),
                    contentDescription = "Choose a diary template",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(25.dp)
                )
            },
            label = { Text("Templates") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown()
                                onLongPress()

                                val upOrCancel = waitForUpOrCancellation()
                                if (upOrCancel != null) {
                                    onDismissed()
                                }
                            }
                        },
                    painter = painterResource(R.drawable.ic_emoji_outline),
                    contentDescription = "Choose a mood",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            label = { Text("Choose a mood") }
        )
    }
}