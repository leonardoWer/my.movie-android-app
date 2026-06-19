package com.leonardower.mymovie.ui.components.img

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.leonardower.mymovie.domain.img.AssetsImgData

@Composable
fun ImgFromAssets(
    pathFromAssets: String,
    fileName: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    tint: Color? = null,
    placeholder: Painter? = null,
) {
    if (fileName.isEmpty()) return

    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data("file:///android_asset/$pathFromAssets/$fileName")
            .build(),
        contentDescription = fileName,
        modifier = modifier,
        contentScale = contentScale,
        colorFilter = tint?.let { ColorFilter.tint(it) },
        placeholder = placeholder,
    )
}

@Composable
fun ImgFromAssets(
    imgData: AssetsImgData,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    tint: Color? = null,
    placeholder: Painter? = null,
) {
    ImgFromAssets(
        pathFromAssets = imgData.pathFromAssets,
        fileName = imgData.fileName,
        modifier = modifier,
        contentScale = contentScale,
        tint = tint,
        placeholder = placeholder
    )
}
