package com.spacex.rocket.spacexrocketinfo.domain

import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.RequestQuery
import com.spacex.rocket.spacexrocketinfo.data.remote.RocketsRepository
import io.reactivex.Observable
import javax.inject.Inject

class RocketDetailsUseCase @Inject constructor(private val repository: RocketsRepository) {
    fun getRocketDetailsData(request: RequestQuery): Observable<RocketDetailsData> {
        return repository.loadRocketDetailsData(request)
    }

}
