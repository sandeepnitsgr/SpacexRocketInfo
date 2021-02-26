package com.spacex.rocket.spacexrocketinfo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spacex.rocket.spacexrocketinfo.data.model.DocWithYear
import com.spacex.rocket.spacexrocketinfo.data.model.LaunchDetailContainer
import com.spacex.rocket.spacexrocketinfo.data.model.details.Doc
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsData
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.domain.RocketDetailsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class RocketDetailsViewModel @Inject constructor(
    private val usecase: RocketDetailsUseCase,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    var disposable = CompositeDisposable()
        private set

    fun getRocketDetailsData(request: Request) {
        disposable.add(usecase.getRocketDetailsData(request)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { _customDetailData.setValue(RocketDetailsResponse.loading()) }
            .subscribe(
                { response: RocketDetailsData ->
                    run {
                        val sortedList =
                            response.docs.sortedWith(compareBy { it.dateUtc })

                        val map = createCountMap(sortedList)
                        _customDetailData.value =
                            RocketDetailsResponse.success(
                                LaunchDetailContainer(
                                    createCustomResponse(
                                        sortedList
                                    ), sortedList, map
                                )
                            )

                    }
                }
            ) { error: Throwable ->
                run {
                    _customDetailData.value = RocketDetailsResponse.error(error)
                }
            })

    }

    private fun createCountMap(sortedList: List<Doc>?): SortedMap<String, Int> {
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

    private fun createCustomResponse(list: List<Doc>?): MutableList<DocWithYear> {
        val customResponse = mutableListOf<DocWithYear>()
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
    val customDetailData: LiveData<RocketDetailsResponse>
        get() = _customDetailData

    private val _customDetailData = MutableLiveData<RocketDetailsResponse>()

}
