package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.spacex.rocket.spacexrocketinfo.data.model.RocketInfo
import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.domain.RocketsListUseCase
import com.spacex.rocket.spacexrocketinfo.utils.BaseSchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

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
    fun `test getRocketsList method for null list`() {
        val mockResponse = mockk<Observable<RocketListData>>(relaxed = true)
        every { rocketListUseCase.getRocketsList() } returns mockResponse
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } returns Schedulers.trampoline()

        val list = viewModel.getRocketsList("All")
        assertNull(list)
        assert(list.isNullOrEmpty())
        assert(viewModel.disposables.size() == 0)

    }

    @Test
    fun `test getRocketsList method for non null list`() {
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } answers { Schedulers.trampoline() }
        val dummyRocketList = mockk<RocketListData>()
        every { dummyRocketList[any()].name } returns "rocketName"
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
    fun `test whether there are 2 items in active and inactive list out of 4 total items`() {
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } answers { Schedulers.trampoline() }

        val mockRocketInfo = listOf<RocketInfo>(mockk(), mockk(), mockk(), mockk())

        for ((count, item) in mockRocketInfo.withIndex()) {
            if (count % 2 == 0) {
                every { item.active } returns true
                every { item.name } returns "firstType"
            } else {
                every { item.active } returns false
                every { item.name } returns "secondType"
            }
        }
        val dummyRocketList = RocketListData()

        dummyRocketList.addAll(mockRocketInfo)

//        val successSlot = slot<Consumer<RocketListData>>()
//        val errorSlot = slot<Consumer<Throwable>>()
//        every { mockResponse.subscribe(capture(successSlot), capture(errorSlot)) } returns mockk()

        val mockResponse = Observable.just(dummyRocketList)
        every { rocketListUseCase.getRocketsList() } returns mockResponse


        viewModel.getAllRocketsListFromRepo()


        val list = viewModel.getRocketsList("All")!!

        assert(list.size == 4)
        assertTrue(list[2].active!!)
        assertEquals("firstType", list[0].name)
        assertEquals("secondType", list[1].name)
        assertEquals("firstType", list[2].name)
        assertEquals("secondType", list[3].name)

        val activeList = viewModel.getRocketsList("Active")!!

        assert(activeList.size == 2)
        assertEquals("firstType", activeList[0].name)
        assertEquals("firstType", activeList[1].name)

        val inActiveList = viewModel.getRocketsList("Inactive")!!

        assert(inActiveList.size == 2)
        assertEquals("secondType", inActiveList[0].name)
        assertEquals("secondType", inActiveList[1].name)

        assert(viewModel.rocketListData.value?.data == dummyRocketList)
    }

    @Test
    fun `test whether disposable is cleared`() {
        viewModel.onCleared()
        assert(viewModel.disposables.size() == 0)
    }


    @Test
    fun `test in case some error occurs then check the updated livedata value with that exception`() {

        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } answers { Schedulers.trampoline() }
        every { rocketListUseCase.getRocketsList() } returns Observable.error(Exception("test exception"))

        viewModel.getAllRocketsListFromRepo()

        // check whether data field remains null due to error
        assertNull(viewModel.getRocketsList("All"))
        assertNull(viewModel.rocketListData.value?.data)

        // check whether the exception is handled and rightly added in error
        assertEquals( "test exception", viewModel.rocketListData.value?.error?.message)

    }
}