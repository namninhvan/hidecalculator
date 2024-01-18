package com.techbmt.hidecalculator.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.techbmt.hidecalculator.db.entities.Note
import com.techbmt.hidecalculator.feature.main.images.HiddenFile
import com.techbmt.hidecalculator.feature.main.video.VideoModel

@Dao
interface AppDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Query("select * from Note")
    fun readAllNote(): LiveData<List<Note>>

    @Update()
    suspend fun updateNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHiddenFile(hiddenFile: HiddenFile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHiddenVideo(video: VideoModel)

    @Query("select * from HiddenFile")
    fun readAllHiddenFile(): LiveData<List<HiddenFile>>

    @Query("select * from VideoModel")
    fun readAllHiddenVideo(): LiveData<List<VideoModel>>

    @Query("delete from HiddenFile where id=:id")
    suspend fun deleteHiddenImage(id: Int)

    @Query("delete from Note where id=:id")
    suspend fun deleteNote(id: Int)

    @Query("delete from VideoModel where id=:id")
    suspend fun deleteVideo(id: Int)

}