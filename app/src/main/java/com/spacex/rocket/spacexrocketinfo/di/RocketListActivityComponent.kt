package com.spacex.rocket.spacexrocketinfo.di

import android.content.Context
import com.spacex.rocket.spacexrocketinfo.presentation.ui.rocketlist.RocketsListActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [SpaceXAppComponent::class])
interface RocketListActivityComponent {
    fun injectListActivity(rocketListActivity: RocketsListActivity?)
}