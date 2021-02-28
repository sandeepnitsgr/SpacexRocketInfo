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
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

class RocketsListViewModel @Inject constructor(
    private val usecase: RocketsListUseCase
) : ViewModel() {

    val rocketListData: LiveData<Response>
        get() = _rocketListData

    private val _rocketListData = MutableLiveData<Response>()

    private fun getActiveRocketsList(): RocketListData {
        val filteredData =
            _rocketListData.value?.data?.filter { rocketInfo -> rocketInfo.active == true }
        val rocketListData = RocketListData()
        if (filteredData != null) {
            rocketListData.addAll(filteredData)
        }
        return rocketListData
    }

    private fun getInactiveRocketsList(): RocketListData {
        val filteredData =
            _rocketListData.value?.data?.filter { rocketInfo -> rocketInfo.active == false }
        val rocketListData = RocketListData()
        if (filteredData != null) {
            rocketListData.addAll(filteredData)
        }
        return rocketListData
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
        _rocketListData.value = loading()
        usecase.getRocketsList().enqueue(object : Callback<RocketListData> {
            override fun onResponse(
                call: Call<RocketListData>,
                response: retrofit2.Response<RocketListData>
            ) {
                _rocketListData.postValue(success(response.body()!!))
            }

            override fun onFailure(call: Call<RocketListData>, throwable: Throwable) {
                _rocketListData.postValue(error(throwable))
            }

        })


    }
}
