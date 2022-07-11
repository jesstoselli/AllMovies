package com.example.allmovies.repository

import com.example.allmovies.network.ErrorResponse
import com.example.allmovies.network.NetworkResponse
import com.example.allmovies.network.model.dto.MovieResponseDTO
import kotlinx.coroutines.CoroutineDispatcher

interface HomeDataSource {
    suspend fun getListsOfMovies(
        dispatcher: CoroutineDispatcher,
        homeResultCallback: (result: NetworkResponse<List<List<MovieResponseDTO>>, ErrorResponse>) -> Unit
    )
}
