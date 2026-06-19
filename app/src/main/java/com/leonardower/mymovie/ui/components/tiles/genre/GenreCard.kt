package com.leonardower.mymovie.ui.components.tiles.genre

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.domain.genre.GenreData
import com.leonardower.mymovie.ui.components.img.ImgFromAssets

@Composable
fun GenreCard(
    genreData: GenreData,
    onClick: () -> Unit = {}
) {
    val pathFromAssets = "genre/"
    val firstPhotoFileName = genreData.previewImgFileNamePair.first
    val secondPhotoFileName = genreData.previewImgFileNamePair.second

    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(
                width = 1.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            text = genreData.genreName,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(vertical = 2.dp),
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium
        )

        ImgFromAssets(
            fileName = firstPhotoFileName.orEmpty(),
            pathFromAssets = pathFromAssets,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(70.dp)
                .offset(y = 5.dp, x = -(10).dp)
                .rotate(-7f)
                .clip(MaterialTheme.shapes.small)
        )

        ImgFromAssets(
            fileName = secondPhotoFileName.orEmpty(),
            pathFromAssets = pathFromAssets,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(70.dp)
                .offset(y = 12.dp, x = 10.dp)
                .rotate(-10f)
                .clip(MaterialTheme.shapes.small)
        )
    }
}

@Preview
@Composable
private fun PreviewGenreCard() {
    GenreCard(
        genreData = GenreData(
            genreName = "Драма",
            _previewImgFileNameList = listOf(
                "drama__1.png", "drama__2.png"
            )
        )
    )
}