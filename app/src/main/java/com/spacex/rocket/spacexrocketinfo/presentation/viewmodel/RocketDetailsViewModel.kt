package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.domain.RocketDetailsUseCase
import javax.inject.Inject

class RocketDetailsViewModel @Inject constructor(
    val usecase: RocketDetailsUseCase,
    val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {
    fun getRocketDetailsData(request: Request) {
        usecase.getRocketDetailsData(request)
    }

    private val _rocketDetailsData = MutableLiveData<RocketDetailsResponse>()
    val rocketDetailsData: LiveData<RocketDetailsResponse>
        get() = _rocketDetailsData

}
