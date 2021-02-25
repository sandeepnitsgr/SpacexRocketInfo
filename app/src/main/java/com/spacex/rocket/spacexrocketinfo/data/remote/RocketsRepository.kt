package com.spacex.rocket.spacexrocketinfo.data.remote

import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.RequestQuery
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class RocketsRepository @Inject constructor() {

    @Inject
    lateinit var service: ApiService

    fun loadRocketListData(): Observable<RocketListData> {
        return service.getRocketList()
    }

    fun loadRocketDetailsData(request: RequestQuery): Observable<RocketDetailsData> {
        return service.getLaunchesDetail(request)
    }


}
