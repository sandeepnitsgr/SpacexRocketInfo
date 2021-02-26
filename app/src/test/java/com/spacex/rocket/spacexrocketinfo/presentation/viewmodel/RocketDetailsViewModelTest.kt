package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.spacex.rocket.spacexrocketinfo.data.model.details.Doc
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.domain.RocketDetailsUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Observable
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

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = spyk(RocketDetailsViewModel(usecase, schedulerProvider))
    }

    @Test
    fun `check rocket detail data for non null value`() {
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } returns Schedulers.trampoline()
        val mockRequest = mockk<Request>()
        val dummyResponseList = listOf<Doc>(mockk(), mockk(), mockk(), mockk())
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
        every { response.docs } returns dummyResponseList
        val dummyObservable = Observable.just(response)
        every { usecase.getRocketDetailsData(mockRequest) } returns dummyObservable

        viewModel.getRocketDetailsData(mockRequest)

        assertNotNull(viewModel.customDetailData.value)
        val sortedResponse = copyOfDummyResponseList.sortedWith(compareBy { it.dateUtc })

        assert(viewModel.customDetailData.value!!.data!!.docList == sortedResponse)

        // check whether the sorted list is correctly created or not
        assertEquals("1997-", viewModel.customDetailData.value!!.data!!.docList!![0].dateUtc)
        assertEquals("1999-", viewModel.customDetailData.value!!.data!!.docList!![1].dateUtc)
        assertEquals("2002-", viewModel.customDetailData.value!!.data!!.docList!![2].dateUtc)
        assertEquals("2004-", viewModel.customDetailData.value!!.data!!.docList!![3].dateUtc)

        // All years are different so different entries in docWithYearList
        assert(viewModel.customDetailData.value!!.data!!.docWithYearList.size == 8)

    }

    @Test
    fun `check rocket detail data for erorr handling`() {

        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } answers { Schedulers.trampoline() }
        every { usecase.getRocketDetailsData(any()) } returns Observable.error(Exception("Some Error occurred"))

        viewModel.getRocketDetailsData(mockk())

        assertEquals("Some Error occurred", viewModel.customDetailData.value!!.error!!.message)
    }

    @Test
    fun `check detail response container entries`() {
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } answers { Schedulers.trampoline() }

        val mockRequest = mockk<Request>()
        val dummyResponseList = listOf<Doc>(mockk(), mockk(), mockk(), mockk())
        val response = mockk<RocketDetailsData>()
        every { response.docs } returns dummyResponseList
        val dummyObservable = Observable.just(response)
        every { usecase.getRocketDetailsData(mockRequest) } returns dummyObservable

        for (item in dummyResponseList) {
            every { item.dateUtc } returns "2000-"
        }

        viewModel.getRocketDetailsData(mockRequest)

        assert(viewModel.disposable.size() == 1)
        // all years are same so 1 extra entry for year will be there in docWithYearList
        assert(viewModel.customDetailData.value!!.data!!.docWithYearList.size == 5)

    }

    @Test
    fun `test whether disposable is cleared`() {

        val mockRequest = mockk<Request>()
        val dummyObservable = mockk<Observable<RocketDetailsData>>(relaxed = true)
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } answers { Schedulers.trampoline() }
        every { usecase.getRocketDetailsData(mockRequest) } returns dummyObservable

        viewModel.getRocketDetailsData(mockRequest)


        assertEquals(1, viewModel.disposable.size())
        viewModel.onCleared()
        assertEquals(0, viewModel.disposable.size())
    }
}