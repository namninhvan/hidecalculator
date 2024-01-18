package com.techbmt.hidecalculator.feature.main.images

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class HiddenFile(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val typeId: Int,
    val originPath: String,
    val newPath: String
)