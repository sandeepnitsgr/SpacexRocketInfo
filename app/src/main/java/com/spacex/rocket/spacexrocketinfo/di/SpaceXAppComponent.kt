package com.spacex.rocket.spacexrocketinfo.di

import com.spacex.rocket.spacexrocketinfo.SpaceXApp
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.ApiService
import dagger.Component


@ApplicationScope
@Component(modules = [ContextModule::class, RetrofitModule::class, SpaceXModule::class])
interface SpaceXAppComponent {
    var apiService: ApiService
    fun inject(spaceXApplication: SpaceXApp)
}