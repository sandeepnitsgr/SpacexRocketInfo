package com.spacex.rocket.spacexrocketinfo.domain

import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.data.remote.RocketsRepository
import io.reactivex.Observable
import javax.inject.Inject

class RocketsListUseCase @Inject constructor(private val repository: RocketsRepository) {
    fun getRocketsList(): Observable<RocketListData> {
        return repository.loadRocketListData()
    }
}
