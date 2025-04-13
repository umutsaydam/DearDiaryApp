package com.umutsaydam.deardiary.presentation.settings.fontSettings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.presentation.common.BaseAlertDialog

@Composable
fun FontSettingsDialog(
    modifier: Modifier = Modifier,
    defaultTextStyle: Int,
    defaultFontFamily: Int,
    onSelected: (Int, Int) -> Unit,
    onDismissed: () -> Unit
) {
    val textStyleList = listOf(
        MaterialTheme.typography.headlineMedium,
        MaterialTheme.typography.headlineSmall,
        MaterialTheme.typography.bodyLarge,
        MaterialTheme.typography.bodyMedium
    )
    val fontFamilyList = listOf(
        Pair("Serif", FontFamily.Serif),
        Pair("Cursive", FontFamily.Cursive),
        Pair("Monospace", FontFamily.Monospace),
        Pair("SansSerif", FontFamily.SansSerif),
    )
    var isFontExpanded by remember { mutableStateOf(false) }

    BaseAlertDialog(
        modifier = modifier.fillMaxHeight(0.8F),
        icon = R.drawable.ic_text_filled,
        contentDesc = "Select Font Family and Size",
        title = "Select Font Family and Size",
        text = {
            Column {
                Box {
                    TextButton(
                        onClick = { isFontExpanded = true }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_dropdown_filled),
                            contentDescription = "Select a font family"
                        )

                        Text(
                            text = fontFamilyList[defaultFontFamily].first,
                            textAlign = TextAlign.Center
                        )
                    }

                    DropdownMenu(
                        expanded = isFontExpanded,
                        onDismissRequest = { isFontExpanded = false }
                    ) {
                        fontFamilyList.forEachIndexed { index, font ->
                            DropdownMenuItem(
                                text = { Text(font.first) },
                                onClick = {
                                    onSelected(defaultTextStyle, index)
                                    isFontExpanded = false
                                }
                            )
                        }
                    }

                }

                FontSizeListLazyColumn(
                    textStyleList = textStyleList,
                    fontFamily = fontFamilyList[defaultFontFamily].second,
                    selectedIndex = defaultTextStyle,
                    onSelected = { index ->
                        onSelected(index, defaultFontFamily)
                    }
                )
            }
        },
        onDismissed = { onDismissed() },
        confirmButton = {
            TextButton(onClick = { onDismissed() }) {
                Text(text = "Save", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = { onDismissed() }
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}