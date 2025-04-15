package com.umutsaydam.deardiary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.umutsaydam.deardiary.data.local.typeConverters.DiaryTypeConverters
import com.umutsaydam.deardiary.domain.entity.DiaryEntity

@Database(entities = [DiaryEntity::class], version = 1, exportSchema = false)
@TypeConverters(DiaryTypeConverters::class)
abstract class DearDiaryDB : RoomDatabase() {
    abstract val diaryDao: DiaryDao
}