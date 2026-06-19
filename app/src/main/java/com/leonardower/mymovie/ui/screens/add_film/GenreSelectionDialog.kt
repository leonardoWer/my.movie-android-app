package com.leonardower.mymovie.ui.screens.add_film

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.domain.genre.GenreData
import com.leonardower.mymovie.domain.genre.getGenrePreviewImages
import com.leonardower.mymovie.ui.components.tiles.genre.GenreCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GenreSelectionBottomSheet(
    allGenres: List<Genre>,
    selectedGenres: List<Genre>,
    onDismiss: () -> Unit,
    onConfirm: (List<Genre>) -> Unit
) {
    // Внутреннее состояние выбранных жанров
    val tempSelectedGenres = remember(selectedGenres) {
        selectedGenres.toMutableStateList()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Выберите жанры",
                    style = MaterialTheme.typography.titleSmall,
                )
                TextButton(
                    onClick = {
                        onConfirm(tempSelectedGenres.toList())
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "Выбрать ${tempSelectedGenres.size}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 4,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                allGenres.forEach { genre ->
                    val isSelected = tempSelectedGenres.any { it.id == genre.id }

                    GenreSelectionItem(
                        genre = genre,
                        isSelected = isSelected,
                        onToggle = {
                            if (isSelected) {
                                tempSelectedGenres.removeAll { it.id == genre.id }
                            } else {
                                tempSelectedGenres.add(genre)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GenreSelectionItem(
    genre: Genre,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .clickable { onToggle() }
    ) {
        GenreCard(
            genreData = GenreData(
                genreName = genre.name
            ),
            onClick = onToggle
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    color = if (isSelected) 
                        MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.primary
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Default.Clear else Icons.Default.Add,
                contentDescription = if (isSelected) "Remove genre" else "Add genre",
                tint = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}