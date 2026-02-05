package com.leonardower.mymovie.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.R
import com.leonardower.mymovie.ui.theme.ErrorRed
import com.leonardower.mymovie.ui.theme.GrayButtonColor
import com.leonardower.mymovie.ui.theme.SuccessGreen
import com.leonardower.mymovie.ui.theme.Yellow

@Composable
fun RatingItem(
    rating: Int
) {
    val ratingText = rating.toString()

    when (rating) {
        in 1..4 -> ColorRateItem(ratingText, ErrorRed)
        in 5..6 -> ColorRateItem(ratingText, Yellow)
        in 7..8 -> ColorRateItem(ratingText, SuccessGreen)
        else -> GoldGradientRateItem(ratingText)
    }

}

@Composable
private fun ColorRateItem(rating: String, bgColor: Color) {
    Box(
        modifier = Modifier
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 10.dp,
                    vertical = 1.dp
                ),
            text = rating,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}


@Composable
private fun GoldGradientRateItem(rating: String) {
    Row(
        modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFE8C275),
                        Color(0xFFC9A452),
                        Color(0xFFA98532),
                        Color(0xFF8A6B1F)
                    )
                )
            )
            .padding(
                horizontal = 2.dp,
                vertical = 2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .height(14.dp)
                .graphicsLayer { rotationY = 180f },
            painter = painterResource(id = R.drawable.ico__winner_wreath),
            contentDescription = "winners wreath",
            tint = GrayButtonColor
        )
        Text(
            text = rating,
            style = MaterialTheme.typography.bodySmall,
            color = GrayButtonColor,
        )
        Icon(
            modifier = Modifier.height(14.dp),
            painter = painterResource(id = R.drawable.ico__winner_wreath),
            contentDescription = "winners wreath",
            tint = GrayButtonColor
        )
    }
}


@Preview
@Composable
private fun Preview() {
    Column {
        RatingItem(4)
        RatingItem(6)
        RatingItem(8)
        RatingItem(9)
        RatingItem(10)
    }
}