package com.spacex.rocket.spacexrocketinfo.data.remote.retrofit

import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.utils.Constants.LAUNCH_DETAIL_ENDPOINT
import com.spacex.rocket.spacexrocketinfo.utils.Constants.ROCKET_LIST_ENDPOINT
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET(ROCKET_LIST_ENDPOINT)
    fun getRocketList(): Call<RocketListData>

    @POST(LAUNCH_DETAIL_ENDPOINT)
    fun getLaunchesDetail(@Body query: Request): Call<RocketDetailsData>
}