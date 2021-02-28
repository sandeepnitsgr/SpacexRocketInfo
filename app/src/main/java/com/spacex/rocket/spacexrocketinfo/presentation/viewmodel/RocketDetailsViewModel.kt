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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RocketDetailsViewModel @Inject constructor(
    private val usecase: RocketDetailsUseCase
) : ViewModel() {
    var page = 1
    fun getRocketDetailsData(request: Request) {
        if(request.option == null) page = 1
        _customDetailData.value = RocketDetailsResponse.loading()
        usecase.getRocketDetailsData(request).enqueue(object : Callback<RocketDetailsData> {
            override fun onResponse(
                call: Call<RocketDetailsData>,
                response: Response<RocketDetailsData>
            ) {
                run {
                    val sortedList =
                        response.body()

                    val map = createCountMap(sortedList?.docs)
                    _customDetailData.value =
                        RocketDetailsResponse.success(
                            LaunchDetailContainer(
                                createCustomResponse(
                                    sortedList?.docs
                                ),
                                sortedList?.docs,
                                map
                            ), page
                        )
                    if(sortedList?.hasNextPage == true) {
                        request.option = PagingOption(page + 1)
                        getRocketDetailsData(request)
                        page++
                    } else {
                        _customDetailData.value = RocketDetailsResponse.dataCompleted()
                    }

                }
            }

            override fun onFailure(call: Call<RocketDetailsData>, throwable: Throwable) {
                _customDetailData.value = RocketDetailsResponse.error(throwable)
            }

        })
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

    val customDetailData: LiveData<RocketDetailsResponse>
        get() = _customDetailData

    private val _customDetailData = MutableLiveData<RocketDetailsResponse>()

}
