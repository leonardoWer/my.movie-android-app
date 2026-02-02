package com.leonardower.mymovie.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonardower.mymovie.R
import com.leonardower.mymovie.ui.components.common.SearchBar
import com.leonardower.mymovie.ui.components.list.GenreList
import com.leonardower.mymovie.ui.screens.search.vm.SearchVM
import com.leonardower.mymovie.ui.screens.search.vm.SearchViewModelFactory

@Composable
fun SearchScreen(
    viewModel: SearchVM = viewModel(
        factory = SearchViewModelFactory.factory
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val allGenres by viewModel.allGenres.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Поисковая строка
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            placeholder = stringResource(R.string.search_hint),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Контент в зависимости от состояния
        when {
            uiState.searchQuery.isNotEmpty() -> {
                SearchResultContent(
                    searchResults = uiState.searchResults,
                    onFilmClick = viewModel::onFilmClick,
                    onGenreClick = viewModel::onGenreClick
                )
            }

            else -> {
                GenreList(
                    modifier = Modifier.padding(16.dp),
                    allGenres = allGenres,
                    onGenreClick = viewModel::onGenreClick
                )
            }
        }
    }
}