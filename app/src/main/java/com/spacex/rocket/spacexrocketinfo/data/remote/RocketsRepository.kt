package com.spacex.rocket.spacexrocketinfo.data.remote

import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.ApiService
import retrofit2.Call
import javax.inject.Inject

class RocketsRepository @Inject constructor() {

    @Inject
    lateinit var service: ApiService

    fun loadRocketListData(): Call<RocketListData> {
        return service.getRocketList()
    }

    fun loadRocketDetailsData(request: Request): Call<RocketDetailsData> {
        return service.getLaunchesDetail(request)
    }


}
