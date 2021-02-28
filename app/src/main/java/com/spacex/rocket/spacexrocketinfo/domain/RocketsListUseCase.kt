package com.spacex.rocket.spacexrocketinfo.domain

import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.data.remote.RocketsRepository
import com.spacex.rocket.spacexrocketinfo.utils.toMappedRocketListData
import retrofit2.Call
import javax.inject.Inject

class RocketsListUseCase @Inject constructor(private val repository: RocketsRepository) {
    fun getRocketsList(): Call<RocketListData> {
        return repository.loadRocketListData()
    }
}
