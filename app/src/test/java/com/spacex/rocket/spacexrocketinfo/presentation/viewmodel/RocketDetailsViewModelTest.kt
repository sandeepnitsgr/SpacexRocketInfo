package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.domain.RocketDetailsUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.*
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
        MockKAnnotations.init(this)
        viewModel = spyk(RocketDetailsViewModel(usecase, schedulerProvider))
    }

    @Test
    fun `check rocket detail data for non null value`() {
        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } returns Schedulers.trampoline()
        val mockRequest = mockk<Request>()
        val dummyResponse = mockk<RocketDetailsData>()
        every { dummyResponse.docs[any()].success } returns true
        val response = Observable.just(dummyResponse)
        every { usecase.getRocketDetailsData(mockRequest) } returns response

        viewModel.getRocketDetailsData(mockRequest)

        assertNotNull(viewModel.rocketDetailsData.value)
        assert(viewModel.rocketDetailsData.value!!.data == response.firstElement().blockingGet())
        assertTrue(viewModel.rocketDetailsData.value!!.data!!.docs[0].success)

    }

    @Test
    fun `check rocket detail data for erorr handling`() {

        every { schedulerProvider.io() } returns Schedulers.trampoline()
        every { schedulerProvider.ui() } answers { Schedulers.trampoline() }
        every { usecase.getRocketDetailsData(any()) } returns Observable.error(Exception("Some Error occurred"))

        viewModel.getRocketDetailsData(mockk())

        assertEquals("Some Error occurred", viewModel.rocketDetailsData.value!!.error!!.message)
    }
}