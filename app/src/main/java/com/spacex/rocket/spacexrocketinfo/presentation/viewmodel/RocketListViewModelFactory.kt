package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spacex.rocket.spacexrocketinfo.domain.RocketsListUseCase
import com.spacex.rocket.spacexrocketinfo.utils.SchedulerProvider
import javax.inject.Inject

class RocketListViewModelFactory @Inject constructor(
    private val useCase: RocketsListUseCase,
    private val schedulerProvider: SchedulerProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RocketsListViewModel::class.java)) {
            return RocketsListViewModel(useCase, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
