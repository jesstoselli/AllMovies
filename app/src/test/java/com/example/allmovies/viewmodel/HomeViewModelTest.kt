package com.example.allmovies.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.allmovies.AppConstants
import com.example.allmovies.network.ErrorResponse
import com.example.allmovies.network.NetworkResponse
import com.example.allmovies.network.model.dto.MovieDTO
import com.example.allmovies.repository.HomeDataSource
import com.example.allmovies.repository.HomeDataSourceImpl
import com.example.allmovies.ui.HomeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private lateinit var homeDataSourceMock: HomeDataSourceMock
    private var moviesListMock: List<MovieDTO> = listOf(MovieDTO(id = 0, "", "", ""))
    private var listsOfMoviesMock: List<List<MovieDTO>> = listOf(moviesListMock, moviesListMock, moviesListMock, moviesListMock)

    @Before
    fun init() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when LISTS request returns SUCCESSFULLY expect live data lists filled`() = runBlocking {
        // Given
        homeDataSourceMock = HomeDataSourceMock(NetworkResponse.Success(listsOfMoviesMock))

        val viewModel = HomeViewModel(homeDataSourceMock, dispatcher)

        // When
        viewModel.getListsOfMovies()

        // Then
        assertEquals(listsOfMoviesMock, viewModel.listsOfMovies.value)
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(false, viewModel.errorMessageVisibility.value)
    }
}

class HomeDataSourceMock(private val result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) : HomeDataSource {
    override suspend fun getListsOfMovies(
        dispatcher: CoroutineDispatcher,
        homeResultCallback: (result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) -> Unit
    ) {
        homeResultCallback(result)
    }

}
