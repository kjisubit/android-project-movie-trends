package com.js.movietrends.presentation.features.main.nowplaying

import androidx.lifecycle.ViewModel
import com.js.movietrends.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(useCases: UseCases) : ViewModel() {
    val getNowPlayingMovies = useCases.getNowPlayingMoviesUseCase()
}
