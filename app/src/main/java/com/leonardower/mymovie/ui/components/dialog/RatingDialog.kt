package com.leonardower.mymovie.ui.components.dialog

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.leonardower.mymovie.ui.components.common.GrayButton
import com.leonardower.mymovie.ui.theme.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun RatingDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    filmTitle: String? = null,
    filmPosterUrl: String? = null,
    currentRatingInt: Int? = 0,
) {
    val currentRating = currentRatingInt ?: 0

    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            FullScreenRatingContent(
                onDismiss = onDismiss,
                onConfirm = onConfirm,
                filmTitle = filmTitle,
                filmPosterUrl = filmPosterUrl,
                currentRating = currentRating
            )
        }
    }
}

@Composable
private fun FullScreenRatingContent(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    filmTitle: String?,
    filmPosterUrl: String?,
    currentRating: Int
) {
    var selectedRating by remember { mutableIntStateOf(currentRating) }
    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (currentRating in 0..10) currentRating else 0
    )

    val centerColumnWidth = 150.dp

    // Цвета в зависимости от оценки
    val color = when (selectedRating) {
        in 1..4 -> ErrorRed
        in 5..6 -> Color.Yellow
        in 7..10 -> SuccessGreen
        else -> Color.White
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBg)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Хедер с заголовком и кнопкой закрытия
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Оценить",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Закрыть",
                    tint = LightGray
                )
            }
        }

        // Картинка
        if (!filmPosterUrl.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .height(220.dp)
                    .width(centerColumnWidth)
                    .align(Alignment.CenterHorizontally)
            ) {
                AsyncImage(
                    model = filmPosterUrl,
                    contentDescription = "Постер $filmTitle ",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Название фильма
        if (filmTitle != null) {
            Text(
                text = filmTitle,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                textAlign = TextAlign.Center,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        RatingSelector(
            selectedRating = selectedRating,
            onRatingSelected = { rating -> selectedRating = rating },
            scrollState = scrollState,
            textColor = color,
            itemSize = centerColumnWidth
        )

        Spacer(modifier = Modifier.height(32.dp))

        GrayButton(
            text = when (selectedRating) {
                0 -> "Не оценивать"
                else -> "Поставить оценку"
            },
            onClick = {
                if (selectedRating in 0..10) {
                    onConfirm(selectedRating)
                    onDismiss()
                } else {
                    onDismiss()
                }
            },
            modifier = Modifier.widthIn(min = centerColumnWidth),
            alignTextCenter = true
        )
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun RatingSelector(
    selectedRating: Int,
    itemSize: Dp,
    onRatingSelected: (Int) -> Unit,
    scrollState: LazyListState,
    textColor: Color,
) {
    val ratings = listOf("−", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val containerWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp
    val contentPadding = remember {
        PaddingValues(horizontal = (containerWidth - itemSize) / 2)
    }

    fun centerItem(index: Int) {
        coroutineScope.launch {
            scrollState.animateScrollToItem(index = index)
        }
    }

    // При инициализации центрируем выбранный элемент
    LaunchedEffect(Unit) {
        if (selectedRating in ratings.indices) {
            centerItem(selectedRating)
        }
    }

    // Отслеживаем скролл для автоматического выбора элемента
    LaunchedEffect(scrollState) {
        snapshotFlow {
            scrollState.firstVisibleItemScrollOffset to scrollState.firstVisibleItemIndex
        }
            .debounce(50)
            .collect { (scrollOffset, firstVIIndex) ->
                if (firstVIIndex >= 0 && firstVIIndex < ratings.size) {
                    val itemWidthPx = with(density) { itemSize.toPx() }
                    val visibleIW = itemWidthPx - scrollOffset

                    val visibleThreshold = 2.6
                    val halfThreshold = itemWidthPx / visibleThreshold

                    // Определяем, какой элемент ближе к центру
                    val nextRating: Int
                    var targetIndex = firstVIIndex

                    // Проверяем, нужно ли переключиться на другой элемент
                    if (firstVIIndex < ratings.size - 1 && visibleIW < halfThreshold) {
                        // Прокрутка вправо - переходим к следующему элементу
                        targetIndex = firstVIIndex + 1
                    } else if (firstVIIndex > 0 && scrollOffset > halfThreshold) {
                        // Прокрутка влево - переходим к предыдущему элементу
                        targetIndex = firstVIIndex - 1
                    } else if (scrollOffset <= halfThreshold && visibleIW >= halfThreshold) {
                        // Остаемся на текущем элементе
                        targetIndex = firstVIIndex
                    }

                    // Конвертируем индекс в рейтинг (0 для индекса 0, иначе индекс)
                    nextRating = if (targetIndex == 0) 0 else targetIndex

                    onRatingSelected(nextRating)
                    centerItem(targetIndex)
                }
            }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(DarkBg)
                .size(itemSize)
                .align(Alignment.Center)
        )
        LazyRow(
            state = scrollState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            contentPadding = contentPadding
        ) {
            items(ratings.size) { index ->
                val rating = if (index == 0) 0 else index // "−" это 0
                val isSelected = selectedRating == rating

                RatingItem(
                    text = ratings[index],
                    isSelected = isSelected,
                    itemSize = itemSize,
                    onClick = {
                        onRatingSelected(rating)
                        centerItem(index)
                    },
                    textColor = textColor,
                )
            }
        }
    }
}

@Composable
private fun RatingItem(
    text: String,
    isSelected: Boolean,
    itemSize: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    textColor: Color
) {
    val fontSize by animateFloatAsState  (
        targetValue = if (isSelected) 48f else 36f,
        animationSpec = tween(durationMillis = 200)
    )
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) textColor else LightGray,
        animationSpec = tween(durationMillis = 200)
    )
    Box(
        modifier = modifier
            .size(itemSize)
            .clickable{ onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = fontSize.sp,
            ),
            color = animatedColor,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    RatingDialog(
        true,
        {}, {},
        "Примерное название",
    )
}