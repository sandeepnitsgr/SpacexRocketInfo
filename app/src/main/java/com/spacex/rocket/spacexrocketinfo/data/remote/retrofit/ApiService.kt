package com.spacex.rocket.spacexrocketinfo.data.remote.retrofit

import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.RequestQuery
import com.spacex.rocket.spacexrocketinfo.utils.Constants.LAUNCH_DETAIL_ENDPOINT
import com.spacex.rocket.spacexrocketinfo.utils.Constants.ROCKET_LIST_ENDPOINT
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET(ROCKET_LIST_ENDPOINT)
    fun getRocketList(): Observable<RocketListData>

    @POST(LAUNCH_DETAIL_ENDPOINT)
    fun getLaunchesDetail(@Query("query") query: RequestQuery): Observable<RocketDetailsData>
}