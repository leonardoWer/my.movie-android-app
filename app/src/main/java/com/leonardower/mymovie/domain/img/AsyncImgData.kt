package com.leonardower.mymovie.domain.img

data class AsyncImgData(
    val imgUrl: String,
    val contentDescription: String = "",
    val placeholderRes: Int? = null,
    val errorRes: Int? = null
)