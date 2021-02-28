package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.spacex.rocket.spacexrocketinfo.data.model.Status
import com.spacex.rocket.spacexrocketinfo.data.model.details.Doc
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.domain.RocketDetailsUseCase
import com.spacex.rocket.spacexrocketinfo.utils.BaseSchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RocketDetailsViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var usecase: RocketDetailsUseCase

    @MockK
    lateinit var schedulerProvider: BaseSchedulerProvider

    lateinit var viewModel: RocketDetailsViewModel

    @MockK
    lateinit var observer: Observer<RocketDetailsResponse>

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = RocketDetailsViewModel(usecase, schedulerProvider)
    }

    @Test
    fun `check rocket detail data for non null value`() {
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } returns Schedulers.trampoline()
        val mockRequest = mockk<Request>(relaxed = true)
        val dummyResponseList = ArrayList<Doc>()
        for (i in 1..4)
            dummyResponseList.add(mockk())
        val count = 2000
        var sign = -1
        var i = 1
        val copyOfDummyResponseList = mutableListOf<Doc>()
        for (item in dummyResponseList) {
            val year = count + i * sign
            sign *= -1
            i += 1
            every { item.dateUtc } returns "$year-"
            copyOfDummyResponseList.add(item)
        }

        val response = mockk<RocketDetailsData>()
        every { response.hasNextPage } returns false
        every { response.docs } returns dummyResponseList
        val dummyObservable = Observable.just(response)
        every { usecase.getRocketDetailsData(mockRequest) } returns dummyObservable

        viewModel.getRocketDetailsData(mockRequest)

        assertNotNull(viewModel.customDetailData.value)

        assert(viewModel.customDetailData.value!!.status == Status.SUCCESS)


        // check whether the list is correctly updated in livedata or not
        assertEquals("1999-", viewModel.customDetailData.value!!.data!!.docList!![0].dateUtc)
        assertEquals("2002-", viewModel.customDetailData.value!!.data!!.docList!![1].dateUtc)
        assertEquals("1997-", viewModel.customDetailData.value!!.data!!.docList!![2].dateUtc)
        assertEquals("2004-", viewModel.customDetailData.value!!.data!!.docList!![3].dateUtc)

//         All years are different so different entries in docWithYearList
        assertEquals(8, viewModel.customDetailData.value!!.data!!.docWithYearList.size)

    }

    @Test
    fun `check rocket detail data for erorr handling`() {

        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } answers { Schedulers.trampoline() }
        every { usecase.getRocketDetailsData(any()) } returns Observable.error(Exception("Some Error occurred"))

        viewModel.getRocketDetailsData(mockk(relaxed = true))

        assertEquals("Some Error occurred", viewModel.customDetailData.value!!.error!!.message)
    }

    @Test
    fun `check detail response container entries`() {

        val mockRequest = mockk<Request>(relaxed = true)
        val dummyResponseList = ArrayList<Doc>()
        dummyResponseList.add(mockk())
        dummyResponseList.add(mockk())
        dummyResponseList.add(mockk())
        dummyResponseList.add(mockk())


        val response = mockk<RocketDetailsData>(relaxed = true)
        every { response.docs } returns dummyResponseList
        every { response.hasNextPage } returns false
        val dummyObservable = Observable.just(response)

        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } returns Schedulers.trampoline()
        every { usecase.getRocketDetailsData(mockRequest) } returns dummyObservable

        for (item in dummyResponseList) {
            every { item.dateUtc } returns "2000-"
        }

        viewModel.getRocketDetailsData(mockRequest)

        assert(viewModel.disposable.size() == 1)
        // all years are same so 1 extra entry for year will be there in docWithYearList
        assertEquals(5, viewModel.customDetailData.value!!.data!!.docWithYearList.size)

    }

    @Test
    fun `test whether disposable is cleared`() {

        val mockRequest = mockk<Request>(relaxed = true)
        val dummyObservable = mockk<Observable<RocketDetailsData>>(relaxed = true)
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } returns Schedulers.trampoline()
        every { usecase.getRocketDetailsData(mockRequest) } returns dummyObservable

        viewModel.getRocketDetailsData(mockRequest)


        assertEquals(1, viewModel.disposable.size())
        viewModel.onCleared()
        assertEquals(0, viewModel.disposable.size())
    }
}
