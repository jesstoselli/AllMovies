package com.example.allmovies.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.allmovies.AppConstants
import com.example.allmovies.network.ErrorResponse
import com.example.allmovies.network.NetworkResponse
import com.example.allmovies.network.TmdbApi
import com.example.allmovies.network.model.dto.MovieDTO
import com.example.allmovies.network.model.dto.MovieResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeDataSourceImplTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

//    UnconfinedTestDispatcher
//    StandardTestDispatcher

    @Mock
    private lateinit var tmdbApi: TmdbApi
//    @Mock
//    private lateinit var context: Context
//    @Mock
//    private lateinit var myListDao: MyListDao

    private val movieResponseDTO = MovieResponseDTO(listOf(MovieDTO(0, "", "", "")))

    private lateinit var homeDataSourceImpl: HomeDataSourceImpl

    @Before
    fun init() {
        homeDataSourceImpl = HomeDataSourceImpl(tmdbApi)
//        homeDataSourceImpl = HomeDataSourceImpl(context, tmdbApi, myListDao)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
//        dispatcher.cleanupTestCoroutines()
    }

    // NOT WORKING
    @Test
    fun `when ALL 4 REQUESTS returns SUCCESSFULLY expect success network response`() = runBlocking {
        // Given
        `when`(tmdbApi.getTrending(AppConstants.LANGUAGE, 1)).thenReturn(NetworkResponse.Success(movieResponseDTO))
        `when`(tmdbApi.getUpcoming(AppConstants.LANGUAGE, 1)).thenReturn(NetworkResponse.Success(movieResponseDTO))
        `when`(tmdbApi.getPopular(AppConstants.LANGUAGE, 1)).thenReturn(NetworkResponse.Success(movieResponseDTO))
        `when`(tmdbApi.getTopRated(AppConstants.LANGUAGE, 1)).thenReturn(NetworkResponse.Success(movieResponseDTO))

        var response: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>? = null
        // When
        homeDataSourceImpl.getListsOfMovies(dispatcher) {
            response = it
        }
        // Then
        assertTrue(response is NetworkResponse.Success)
        assertEquals(movieResponseDTO.results, (response as NetworkResponse.Success).body[0])
    }

    @Test
    fun `when 1 OF THE REQUESTS returns API ERROR expect api error network response`() = runBlocking {
        // Given
        `when`(tmdbApi.getTrending(AppConstants.LANGUAGE, 1)).thenReturn(NetworkResponse.ApiError(ErrorResponse(), 400))
        `when`(tmdbApi.getUpcoming(AppConstants.LANGUAGE, 1)).thenReturn(NetworkResponse.Success(movieResponseDTO))
        `when`(tmdbApi.getPopular(AppConstants.LANGUAGE, 1)).thenReturn(NetworkResponse.Success(movieResponseDTO))
        `when`(tmdbApi.getTopRated(AppConstants.LANGUAGE, 1)).thenReturn(NetworkResponse.Success(movieResponseDTO))

        var response: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>? = null
        // When
        homeDataSourceImpl.getListsOfMovies(dispatcher) {
            response = it
        }
        // Then
        assertTrue(response is NetworkResponse.ApiError)
        assertEquals(movieResponseDTO.results, (response as NetworkResponse.ApiError))
    }
}
