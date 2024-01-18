package com.techbmt.hidecalculator.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.techbmt.hidecalculator.db.entities.Note
import com.techbmt.hidecalculator.feature.main.images.HiddenFile
import com.techbmt.hidecalculator.feature.main.video.VideoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DBViewModel(application: Application) : AndroidViewModel(application) {

    private val appDAO = AppDatabase.getDatabase(application).getAppDAO()
    private val repository = DBRepository(appDAO)
    val readAllNote : LiveData<List<Note>> = repository.readAllNote
    val readAllHiddenFile: LiveData<List<HiddenFile>> = repository.readAllHiddenFile
    val readAllHiddenVideo: LiveData<List<VideoModel>> = repository.readAllHiddenVideo

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note)
        }
    }

    fun addHiddenVideo(video: VideoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addHiddenVideo(video)
        }
    }

    fun addHiddenFile(file: HiddenFile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addHiddenFile(file)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.deleteNote(id)
        }
    }

    fun deleteHiddenImage(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHiddenImage(id)
        }
    }

    fun deleteHiddenVideo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHiddenVideo(id)
        }
    }

}