package com.techbmt.hidecalculator.feature.user.setting.language

data class Language (
    var type: TypeOfLanguage? = null,
    var isChosen: Boolean = false
)

enum class TypeOfLanguage {
    VIETNAM, ENGLISH
}