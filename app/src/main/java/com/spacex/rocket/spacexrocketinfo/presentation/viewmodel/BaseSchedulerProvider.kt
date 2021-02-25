package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import io.reactivex.Scheduler

interface BaseSchedulerProvider {
    fun io() : Scheduler
    fun computation() : Scheduler
    fun ui() : Scheduler
}
