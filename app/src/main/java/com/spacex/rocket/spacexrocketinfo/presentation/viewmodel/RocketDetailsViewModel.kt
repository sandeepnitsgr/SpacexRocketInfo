package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.RequestQuery
import com.spacex.rocket.spacexrocketinfo.domain.RocketDetailsUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RocketDetailsViewModel @Inject constructor(
    private val usecase: RocketDetailsUseCase,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    var disposable = CompositeDisposable()
        private set

    fun getRocketDetailsData(request: RequestQuery) {
        disposable.add(usecase.getRocketDetailsData(request)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { _rocketDetailsData.setValue(RocketDetailsResponse.loading()) }
            .subscribe(
                { response: RocketDetailsData ->
                    run {
                        _rocketDetailsData.value = RocketDetailsResponse.success(response)
                    }
                }
            ) { error: Throwable ->
                run {
                    _rocketDetailsData.value = RocketDetailsResponse.error(error)
                }
            })

    }

    private val _rocketDetailsData = MutableLiveData<RocketDetailsResponse>()
    val rocketDetailsData: LiveData<RocketDetailsResponse>
        get() = _rocketDetailsData

}
