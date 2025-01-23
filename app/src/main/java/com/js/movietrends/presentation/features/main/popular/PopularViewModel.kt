package com.js.movietrends.presentation.features.main.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {
    private val _popularMovies = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val popularMovies: StateFlow<PagingData<Movie>> = _popularMovies

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            useCases.getPopularMoviesUseCase()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _popularMovies.value = it
                }
        }
    }
}
