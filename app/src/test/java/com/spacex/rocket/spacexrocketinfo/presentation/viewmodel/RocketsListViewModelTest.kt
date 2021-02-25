package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.spacex.rocket.spacexrocketinfo.data.model.RocketInfo
import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.domain.RocketsListUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RocketsListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var rocketListUseCase: RocketsListUseCase

    @MockK(relaxed = true)
    lateinit var schedulerProvider: BaseSchedulerProvider

    lateinit var viewModel: RocketsListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = RocketsListViewModel(rocketListUseCase, schedulerProvider)
    }

    @Test
    fun getRocketListData() {

    }

    @Test
    fun `test getRocketsList method for null list`() {
        val mockResponse = mockk<Observable<RocketListData>>()
        every { rocketListUseCase.getRocketsList() } returns mockResponse
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } returns Schedulers.trampoline()
        val list = viewModel.getRocketsList("All")

        assert(list.isNullOrEmpty())

    }

    @Test
    fun `test getRocketsList method for non null list`() {
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } answers { Schedulers.trampoline() }
        val dummyRocketList = RocketListData()
        val rocketInfo = mockk<RocketInfo>(relaxed = true)
        every { rocketInfo.name } returns "rocketName"

        dummyRocketList.add(rocketInfo)
        val mockResponse = Observable.just(dummyRocketList)
        every { rocketListUseCase.getRocketsList() } returns mockResponse

        viewModel.getAllRocketsListFromRepo()
        val list = viewModel.getRocketsList("All")!!

        assertEquals("rocketName", list[0].name)
        assertNotEquals("customRocketName", list[0].name)
        verify(exactly = 1) { schedulerProvider.io() }
        verify(exactly = 1) { schedulerProvider.ui() }
        assert(viewModel.disposables.size() == 1)
    }

    @Test
    fun `test whether disposable is cleared`() {
        viewModel.onCleared()
        assert(viewModel.disposables.size() == 0)
    }
}