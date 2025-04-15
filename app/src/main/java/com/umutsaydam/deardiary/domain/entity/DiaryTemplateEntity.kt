package com.umutsaydam.deardiary.domain.entity

import com.umutsaydam.deardiary.R

data class DiaryTemplateEntity(
    val templateId: Int,
    val templateTitle: String,
    val templateVector: Int,
    val templateContent: String,
    val templateContentDescription: String,
    val templateDiaryContents: String
)

val templateList = listOf(
    DiaryTemplateEntity(
        templateId = 0,
        templateTitle = "My Day",
        templateVector = R.drawable.tmp_my_day_vector,
        templateContent = "Simple things to write about every day.",
        templateContentDescription = "My day template vector",
        templateDiaryContents = "How are you today?\n\nDo you need anything?\n"
    ),
    DiaryTemplateEntity(
        templateId = 1,
        templateTitle = "Gratitude",
        templateVector = R.drawable.tmp_gratitude_vector,
        templateContent = "Express gratitude and every day will be better.",
        templateContentDescription = "Gratitude template vector",
        templateDiaryContents = "How are you today?\n\nDo you need anything?\n"
    ),
    DiaryTemplateEntity(
        templateId = 1,
        templateTitle = "Feeling Journal",
        templateVector = R.drawable.tmp_feeling_vector,
        templateContent = "Express emotions to understand yourself better.",
        templateContentDescription = "Feeling Journal template vector",
        templateDiaryContents = "How are you today?\n\nDo you need anything?\n"
    )
)
