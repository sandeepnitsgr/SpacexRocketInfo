package com.spacex.rocket.spacexrocketinfo.di

import com.spacex.rocket.spacexrocketinfo.presentation.ui.RocketDetailsActivity
import dagger.Component

@Component(dependencies = [SpaceXAppComponent::class])
@ActivityScope
interface DetailActivityComponent {
    fun inject(detailActivity: RocketDetailsActivity?)
}