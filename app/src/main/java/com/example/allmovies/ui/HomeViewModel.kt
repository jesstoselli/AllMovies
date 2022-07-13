package com.example.allmovies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allmovies.AppConstants
import com.example.allmovies.di.IoDispatcher
import com.example.allmovies.network.NetworkResponse
import com.example.allmovies.network.TmdbApi
import com.example.allmovies.network.model.dto.MovieDTO
import com.example.allmovies.repository.HomeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeDataSource: HomeDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _listsOfMovies = MutableLiveData<List<List<MovieDTO>>>()
    val listsOfMovies: LiveData<List<List<MovieDTO>>>
        get() = _listsOfMovies

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _errorMessageVisibility = MutableLiveData<Boolean>(false)
    val errorMessageVisibility: LiveData<Boolean>
        get() = _errorMessageVisibility

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        getListsOfMovies()
    }

    fun getListsOfMovies() {
        // App starts loading
        showErrorMessage(false)

        try {
            viewModelScope.launch {
                homeDataSource.getListsOfMovies(ioDispatcher) { result ->
                    when (result) {
                        is NetworkResponse.Success -> {
                            _listsOfMovies.postValue(result.body)
                            _isLoading.postValue(false)
                            _errorMessageVisibility.postValue(false)
                        }
                        is NetworkResponse.NetworkError -> {
                            showErrorMessage(true, AppConstants.NETWORK_ERROR_MESSAGE)
                        }
                        is NetworkResponse.ApiError -> {
                            showErrorMessage(true, AppConstants.API_ERROR_MESSAGE)
                        }
                        is NetworkResponse.UnknownError -> {
                            showErrorMessage(true, AppConstants.UNKNOWN_ERROR_MESSAGE)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }

    }

    private fun showErrorMessage(show: Boolean, message: String? = null) {
        _isLoading.postValue(!show)
        _errorMessageVisibility.postValue(show)
        _errorMessage.postValue(message)
    }
}
