package com.leonardower.mymovie.domain.genre

data class GenreData(
    val genreName: String,
    private val _previewImgFileNameList: List<String>,
) {
    val previewImgFileNamePair: Pair<String?, String?>
        get() = when {
            _previewImgFileNameList.isEmpty() -> null to null
            _previewImgFileNameList.size == 1 -> _previewImgFileNameList[0] to null
            else -> _previewImgFileNameList[0] to _previewImgFileNameList[1]
        }
}