package com.techbmt.hidecalculator.feature.main.images

import java.io.Serializable

data class AlbumModel(
    val parentFolder: String,
    var path: ArrayList<String>
): Serializable
