package com.leonardower.mymovie.ui.components.img

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ImgFromUrl(
    imgUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "Img by url",
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        model = imgUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}