package com.umutsaydam.deardiary.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.entity.EmotionEntity
import com.umutsaydam.deardiary.domain.entity.emotionList
import com.umutsaydam.deardiary.domain.entity.templateList
import com.umutsaydam.deardiary.presentation.addDiary.diaryMood.DiaryMoodItem
import com.umutsaydam.deardiary.presentation.addDiary.diaryTemplate.DiaryTemplateDialog
import kotlinx.coroutines.launch


@Composable
fun BottomXRMenuWithGesture(
    selectedIndex: Int,
    paddingValues: PaddingValues,
    onMoodSelected: (Int) -> Unit,
    onTemplateSelected: (String) -> Unit
) {
    var isMoodDialogOpen by remember { mutableStateOf(false) }
    var isLongPressingMoodButton by remember { mutableStateOf(false) }
    var isTemplateDialogOpen by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }

    if (isTemplateDialogOpen) {
        DiaryTemplateDialog(
            templateList = templateList,
            onTemplateSelected = { selectedTemplate ->
                onTemplateSelected(selectedTemplate.templateDiaryContents)
                isTemplateDialogOpen = false
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(paddingValues)
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
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

                                onMoodSelected(index)

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
            if (selectedIndex != -1) {
                ShowEmotion(selectedIndex)
            }

            BottomXRMenu(
                onTemplateDialogOpen = { isTemplateDialogOpen = true },
                onLongPress = {
                    isMoodDialogOpen = true
                    isLongPressingMoodButton = true
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.BottomCenter
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
}

@Composable
fun ShowEmotion(selectedIndex: Int) {
    val scope = rememberCoroutineScope()
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(emotionList[selectedIndex].emotionSource)
    )
    val animationState = rememberLottieAnimatable()
    Box(
        modifier = Modifier
            .offset(x = 0.dp, y = (-80).dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                scope.launch {
                    composition?.let {
                        animationState.animate(
                            composition = it,
                            iterations = 1
                        )
                    }
                }
            }
    ) {
        LottieAnimation(
            composition = composition,
            progress = animationState.progress,
            modifier = Modifier.size(42.dp)
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