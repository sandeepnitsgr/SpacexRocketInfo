package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spacex.rocket.spacexrocketinfo.data.model.Response
import com.spacex.rocket.spacexrocketinfo.data.model.Response.Companion.error
import com.spacex.rocket.spacexrocketinfo.data.model.Response.Companion.loading
import com.spacex.rocket.spacexrocketinfo.data.model.Response.Companion.success
import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.domain.RocketsListUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RocketsListViewModel @Inject constructor(
    private val usecase: RocketsListUseCase,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {
    var disposables = CompositeDisposable()
        private set


    val rocketListData: LiveData<Response>
        get() = _rocketListData

    private val _rocketListData = MutableLiveData<Response>()
    private val _filteredData = MutableLiveData<Response>()

    private fun getActiveRocketsList(): RocketListData? {
        val filteredData =
            _rocketListData.value?.data?.filter { rocketInfo -> rocketInfo.active == true }
        val rocketListData = RocketListData()
        if (filteredData != null) {
            rocketListData.addAll(filteredData)
        }
        _filteredData.value = success(rocketListData)
        return _filteredData.value?.data
    }

    private fun getInactiveRocketsList(): RocketListData? {
        val filteredData =
            _rocketListData.value?.data?.filter { rocketInfo -> rocketInfo.active == false }
        val rocketListData = RocketListData()
        if (filteredData != null) {
            rocketListData.addAll(filteredData)
        }
        _filteredData.value = success(rocketListData)
        return _filteredData.value?.data
    }

    fun getRocketsList(status: String): RocketListData? {
        return when (status) {
            "Active" -> getActiveRocketsList()
            "Inactive" -> getInactiveRocketsList()
            else -> getAllRocketsList()
        }
    }

    private fun getAllRocketsList() = _rocketListData.value?.data

    fun getAllRocketsListFromRepo() {

        disposables.add(usecase.getRocketsList()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { _rocketListData.setValue(loading()) }
            .subscribe(
                { rocketList: RocketListData? ->
                    run {
                        _rocketListData.setValue(success(rocketList!!))
                    }
                }
            ) { throwable: Throwable? ->
                run {
                    _rocketListData.setValue(error(throwable!!))
                }
            }
        )
    }

    public override fun onCleared() {
        disposables.clear()
    }
}
