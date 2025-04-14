package com.umutsaydam.deardiary.presentation.addDiary.diaryTemplate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.entity.DiaryTemplateEntity

@Composable
fun DiaryTemplateListItem(
    modifier: Modifier = Modifier,
    index: Int,
    diaryTemplateEntity: DiaryTemplateEntity,
    onTemplateSelected: (Int) -> Unit,
    onInfoSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(top = 8.dp, bottom = 8.dp, start = 8.dp)
    ) {
        Column(
            modifier = Modifier.weight(0.4f)
        ) {
            Text(
                text = diaryTemplateEntity.templateTitle,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = diaryTemplateEntity.templateContent,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        onTemplateSelected(index)
                    }
                ) {
                    Text(
                        "Add",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        onInfoSelected(index)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info_outline),
                        contentDescription = "Info about template"
                    )
                }
            }
        }

        Image(
            modifier = Modifier.weight(0.6f),
            painter = painterResource(diaryTemplateEntity.templateVector),
            contentDescription = "My day template vector",
            contentScale = ContentScale.Fit,
        )
    }
}