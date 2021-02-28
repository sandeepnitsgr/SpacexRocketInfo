package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spacex.rocket.spacexrocketinfo.data.model.DocWithYear
import com.spacex.rocket.spacexrocketinfo.data.model.LaunchDetailContainer
import com.spacex.rocket.spacexrocketinfo.data.model.details.Doc
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.PagingOption
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.domain.RocketDetailsUseCase
import com.spacex.rocket.spacexrocketinfo.utils.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RocketDetailsViewModel @Inject constructor(
    private val usecase: RocketDetailsUseCase,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {


    val shouldShowProgressBarLiveData: LiveData<Boolean>
    get() = _shouldShowProgressBarLiveData

    private val _shouldShowProgressBarLiveData = MutableLiveData<Boolean>()
    val customDetailData: LiveData<RocketDetailsResponse>
        get() = _customDetailData

    private val _customDetailData =
        MutableLiveData<RocketDetailsResponse>()


    var page = 1
    var disposable = CompositeDisposable()
        private set

    fun getRocketDetailsData(request: Request) {
        if (request.option == null) page = 1

        disposable.add(usecase.getRocketDetailsData(request)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { _shouldShowProgressBarLiveData.value = true }
            .subscribe(
                { response: RocketDetailsData ->
                    run {
                        processResponse(request, response)
                    }
                }) { error: Throwable ->
                run {
                    _customDetailData.value = RocketDetailsResponse.error(error)
                    _shouldShowProgressBarLiveData.value = false
                }
            }
        )
    }

    private fun processResponse(
        request: Request,
        body: RocketDetailsData?
    ) {

        val map = createCountMap(body?.docs)
        _customDetailData.value =
            RocketDetailsResponse.success(
                LaunchDetailContainer(
                    createCustomResponse(
                        body?.docs
                    ),
                    body?.docs,
                    map
                ), page
            )
        if(body?.hasNextPage == true) {
            request.option = PagingOption(page + 1)
            getRocketDetailsData(request)
            page++
        } else {
            _shouldShowProgressBarLiveData.value = false
        }

    }

    private fun createCountMap(sortedList: ArrayList<Doc>?): SortedMap<String, Int> {
        val map = sortedMapOf<String, Int>()
        sortedList?.forEach { item ->
            run {
                val date = item.dateUtc
                val key = date.substring(0, date.indexOf("-"))
                map[key] = map.getOrDefault(key, 0) + 1
            }
        }
        return map
    }

    private fun createCustomResponse(list: ArrayList<Doc>?): ArrayList<DocWithYear> {
        val customResponse = ArrayList<DocWithYear>()
        var prevYear = ""
        list?.let {
            for (item in list) {
                var year = item.dateUtc.substring(0, item.dateUtc.indexOf("-"))
                when {

                    prevYear.isBlank() -> {
                        val docWithYear = DocWithYear(year, null)
                        customResponse.add(docWithYear)
                        customResponse.add(DocWithYear(null, item))
                        prevYear = year
                    }
                    prevYear == year -> {
                        customResponse.add(DocWithYear(null, item))
                    }
                    else -> {
                        prevYear = year
                        customResponse.add(DocWithYear(year, null))
                        customResponse.add(DocWithYear(null, item))
                    }
                }
            }
        }
        return customResponse
    }

    public override fun onCleared() {
        disposable.clear()
    }

}
