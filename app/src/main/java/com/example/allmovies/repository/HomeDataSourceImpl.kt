package com.example.allmovies.repository

import com.example.allmovies.AppConstants
import com.example.allmovies.network.ErrorResponse
import com.example.allmovies.network.NetworkResponse
import com.example.allmovies.network.TmdbApi
import com.example.allmovies.network.model.dto.MovieResponseDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(private val tmdbApi: TmdbApi) : HomeDataSource {
    override suspend fun getListsOfMovies(
        dispatcher: CoroutineDispatcher,
        homeResultCallback: (result: NetworkResponse<List<List<MovieResponseDTO>>, ErrorResponse>) -> Unit
    ) {
        withContext(dispatcher) {
            try {
                val trendingMoviesResponse = async { tmdbApi.getTrending(AppConstants.LANGUAGE, 0) }
                val upcomingMoviesResponse = async { tmdbApi.getUpcoming(AppConstants.LANGUAGE, 0) }
                val popularMoviesResponse = async { tmdbApi.getPopular(AppConstants.LANGUAGE, 0) }
                val topRatedMoviesResponse = async { tmdbApi.getTopRated(AppConstants.LANGUAGE, 0) }
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
