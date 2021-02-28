package com.spacex.rocket.spacexrocketinfo.domain

import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.data.remote.RocketsRepository
import retrofit2.Call
import javax.inject.Inject

class RocketDetailsUseCase @Inject constructor(private val repository: RocketsRepository) {
    fun getRocketDetailsData(request: Request): Call<RocketDetailsData> {
        return repository.loadRocketDetailsData(request)
    }

}
