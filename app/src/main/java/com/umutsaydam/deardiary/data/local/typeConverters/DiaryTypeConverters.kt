package com.umutsaydam.deardiary.data.local.typeConverters

import androidx.room.TypeConverter
import java.util.UUID


class DiaryTypeConverters {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return uuid?.let { UUID.fromString(it) }
    }
}