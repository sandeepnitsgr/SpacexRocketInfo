package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spacex.rocket.spacexrocketinfo.domain.RocketDetailsUseCase
import javax.inject.Inject

class RocketDetailsViewModelFactory @Inject constructor(
    private val useCase: RocketDetailsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RocketDetailsViewModel::class.java)) {
            return RocketDetailsViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
