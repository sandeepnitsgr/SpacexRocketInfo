package com.spacex.rocket.spacexrocketinfo.data.remote.retrofit

import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.RequestQuery
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("v4/rockets")
    fun getRocketList(): Observable<RocketListData>

    @POST("v4/launches/query")
    fun getLaunchesDetail(@Query("query") query: RequestQuery): Observable<RocketDetailsData>
}