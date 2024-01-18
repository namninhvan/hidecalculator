package com.techbmt.hidecalculator.feature.main.video

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class VideoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val originPath: String = "",
    val hiddenPath: String = "",
    val thumb: String = "",
    val duration: Long = 0L
): Serializable
