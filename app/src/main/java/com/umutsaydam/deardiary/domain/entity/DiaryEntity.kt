package com.umutsaydam.deardiary.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.umutsaydam.deardiary.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = "user_diary")
data class DiaryEntity(
    @PrimaryKey
    @Serializable(with = UUIDSerializer::class)
    @ColumnInfo(name = "diary_id")
    val diaryId: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "diary_content")
    var diaryContent: String,
    @ColumnInfo(name = "diary_date")
    val diaryDate: String? = null,
    @ColumnInfo(name = "diary_emotion")
    var diaryEmotion: Int? = null
)