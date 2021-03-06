package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spacex.rocket.spacexrocketinfo.domain.RocketDetailsUseCase
import com.spacex.rocket.spacexrocketinfo.utils.SchedulerProvider
import javax.inject.Inject

class RocketDetailsViewModelFactory @Inject constructor(
    private val useCase: RocketDetailsUseCase,
    private val schedulerProvider: SchedulerProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RocketDetailsViewModel::class.java)) {
            return RocketDetailsViewModel(useCase, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
