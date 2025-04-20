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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.entity.FontFamilySealed
import com.umutsaydam.deardiary.domain.entity.FontFamilySealed.Companion.fontFamilyList
import com.umutsaydam.deardiary.domain.entity.FontSizeSealed
import com.umutsaydam.deardiary.presentation.Dimens.PaddingSmall
import com.umutsaydam.deardiary.presentation.common.BaseAlertDialog

@Composable
fun FontSettingsDialog(
    modifier: Modifier = Modifier,
    defaultTextStyle: FontSizeSealed,
    defaultFontFamily: FontFamilySealed,
    onSave: (FontSizeSealed, FontFamilySealed) -> Unit,
    onSelected: (FontSizeSealed, FontFamilySealed) -> Unit,
    onDismissed: () -> Unit
) {
    val textStyleList = listOf(
        MaterialTheme.typography.headlineMedium,
        MaterialTheme.typography.headlineSmall,
        MaterialTheme.typography.bodyLarge,
        MaterialTheme.typography.bodyMedium
    )
    var isFontExpanded by remember { mutableStateOf(false) }

    BaseAlertDialog(
        modifier = modifier.fillMaxHeight(0.8F),
        icon = R.drawable.ic_text_filled,
        contentDesc = stringResource(R.string.select_font_family_size),
        title = stringResource(R.string.select_font_family_size),
        text = {
            Column {
                Box {
                    TextButton(
                        onClick = { isFontExpanded = true }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_dropdown_filled),
                            contentDescription = stringResource(R.string.select_font_family)
                        )

                        Text(
                            text = defaultFontFamily.fontName,
                            textAlign = TextAlign.Center
                        )
                    }

                    DropdownMenu(
                        expanded = isFontExpanded,
                        onDismissRequest = { isFontExpanded = false }
                    ) {
                        fontFamilyList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.fontName) },
                                onClick = {
                                    onSelected(defaultTextStyle, item)
                                    isFontExpanded = false
                                }
                            )
                        }
                    }

                }

                FontSizeListLazyColumn(
                    textStyleList = textStyleList,
                    fontFamily = defaultFontFamily.fontFamily,
                    selectedIndex = defaultTextStyle.id,
                    onSelected = { index ->
                        onSelected(FontSizeSealed.values[index], defaultFontFamily)
                    }
                )
            }
        },
        onDismissed = { onDismissed() },
        confirmButton = {
            TextButton(onClick = {
                onSave(defaultTextStyle, defaultFontFamily)
            }
            ) {
                Text(text = stringResource(R.string.save), color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(end = PaddingSmall),
                onClick = { onDismissed() }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}