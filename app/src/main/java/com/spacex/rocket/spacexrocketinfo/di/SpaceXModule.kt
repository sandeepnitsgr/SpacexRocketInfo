package com.spacex.rocket.spacexrocketinfo.di

import com.spacex.rocket.spacexrocketinfo.data.remote.RocketsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class SpaceXModule {
    @Singleton
    @Provides
    fun provideRemoteRepoInstance(): RocketsRepository {
        return RocketsRepository()
    }

}