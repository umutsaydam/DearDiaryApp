package com.umutsaydam.deardiary.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDiary(diaryEntity: DiaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDiaries(diaries: List<DiaryEntity>)

    @Delete
    suspend fun deleteDiary(diaryEntity: DiaryEntity)

    @Query("SELECT * FROM user_diary ORDER BY diary_date DESC")
    fun getDiaries(): Flow<List<DiaryEntity>>

    @Query(
        """
            SELECT * FROM user_diary 
            WHERE diary_content LIKE '%' || :query || '%' 
               OR diary_date LIKE '%' || :query || '%' 
            ORDER BY diary_date DESC
        """
    )
    fun searchDiaries(query: String): Flow<List<DiaryEntity>>
}