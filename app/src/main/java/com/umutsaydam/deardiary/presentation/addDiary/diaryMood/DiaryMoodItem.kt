package com.umutsaydam.deardiary.presentation.addDiary.diaryMood

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.umutsaydam.deardiary.domain.entity.EmotionEntity

@Composable
fun DiaryMoodItem(
    modifier: Modifier = Modifier,
    emotionEntity: EmotionEntity,
    isSelected: Boolean
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(emotionEntity.emotionSource))
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.4f else 1f, label = "")

    LottieAnimation(
        modifier = modifier
            .size(42.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}