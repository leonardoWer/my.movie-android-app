package com.leonardower.mymovie.ui.components.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.R
import com.leonardower.mymovie.ui.components.common.GrayButton
import com.leonardower.mymovie.ui.theme.GrayBg
import com.leonardower.mymovie.ui.theme.LightGray

@Composable
fun AddFilmEmptyState(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = GrayBg
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(backgroundColor)
            .padding(vertical = 16.dp, horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Icon(
            modifier = Modifier
                .size(110.dp),
            imageVector = Icons.Default.AddCircle,
            contentDescription = stringResource(R.string.add_film),
            tint = LightGray.copy(alpha = 0.1f)
        )
        Text(
            text = "Добавьте просмотренные фильмы,\nи они будут здесь",
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        GrayButton(
            text = "Добавить фильм",
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 32.dp)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AddFilmEmptyState(
        onClick = {}
    )
}