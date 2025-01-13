package com.js.movietrends.presentation.features.main.popular

import androidx.lifecycle.ViewModel
import com.js.movietrends.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(useCases: UseCases) : ViewModel() {
    val getPopularMovies = useCases.getPopularMoviesUseCase()
}
