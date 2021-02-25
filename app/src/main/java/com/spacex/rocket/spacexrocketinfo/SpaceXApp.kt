package com.spacex.rocket.spacexrocketinfo

import android.app.Activity
import android.app.Application
import com.spacex.rocket.spacexrocketinfo.di.ContextModule
import com.spacex.rocket.spacexrocketinfo.di.DaggerSpaceXAppComponent
import com.spacex.rocket.spacexrocketinfo.di.RetrofitModule
import com.spacex.rocket.spacexrocketinfo.di.SpaceXAppComponent


class SpaceXApp : Application() {
    lateinit var appComponent:SpaceXAppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerSpaceXAppComponent.builder()
            .contextModule(ContextModule(this))
            .retrofitModule(RetrofitModule(this))
            .build()
        appComponent.inject(this)
    }

    fun getApplicationComponent(): SpaceXAppComponent {
        return appComponent
    }
    companion object {
        fun get(activity: Activity): SpaceXApp {
            return activity.application as SpaceXApp
        }
    }
}
