package com.techbmt.hidecalculator.db

import android.app.Application
import androidx.lifecycle.LiveData
import com.techbmt.hidecalculator.db.entities.Note
import com.techbmt.hidecalculator.feature.main.images.HiddenFile
import com.techbmt.hidecalculator.feature.main.video.VideoModel

class DBRepository(private val appDAO: AppDAO) {

    val readAllNote: LiveData<List<Note>> = appDAO.readAllNote()
    val readAllHiddenFile: LiveData<List<HiddenFile>> = appDAO.readAllHiddenFile()
    val readAllHiddenVideo: LiveData<List<VideoModel>> = appDAO.readAllHiddenVideo()

    suspend fun addNote(note: Note) {
        appDAO.addNote(note)
    }

    suspend fun addHiddenFile(hiddenFile: HiddenFile) {
        appDAO.addHiddenFile(hiddenFile)
    }

    suspend fun addHiddenVideo(video: VideoModel) {
        appDAO.addHiddenVideo(video)
    }

    suspend fun updateNote(note: Note) {
        appDAO.updateNote(note)
    }

    suspend fun deleteNote(id: Int) {
        appDAO.deleteNote(id)
    }

    suspend fun deleteHiddenImage(id: Int) {
        appDAO.deleteHiddenImage(id)
    }

    suspend fun deleteHiddenVideo(id: Int) {
        appDAO.deleteVideo(id)
    }

}