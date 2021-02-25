package com.spacex.rocket.spacexrocketinfo.di

import android.content.Context
import com.spacex.rocket.spacexrocketinfo.presentation.ui.rocketlist.RocketsListActivity
import dagger.Module
import dagger.Provides

@Module
class RocketListActivityContextModule(private val rocketsListActivity: RocketsListActivity) {
    var context: Context = rocketsListActivity

    @Provides
    @ActivityScope
    fun providesRocketListActivity(): RocketsListActivity {
        return rocketsListActivity
    }

    @Provides
    @ActivityScope
    @ActivityContext
    fun provideContext(): Context {
        return context
    }

}