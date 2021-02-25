package com.spacex.rocket.spacexrocketinfo.presentation.ui.rocketdetail

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spacex.rocket.spacexrocketinfo.R
import com.spacex.rocket.spacexrocketinfo.SpaceXApp
import com.spacex.rocket.spacexrocketinfo.data.model.RocketInfo
import com.spacex.rocket.spacexrocketinfo.data.model.Status
import com.spacex.rocket.spacexrocketinfo.data.model.details.Doc
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.RequestQuery
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.ApiService
import com.spacex.rocket.spacexrocketinfo.di.DaggerDetailActivityComponent
import com.spacex.rocket.spacexrocketinfo.presentation.ui.BaseActivity
import com.spacex.rocket.spacexrocketinfo.presentation.ui.SpaceItemDecoration
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketDetailsViewModel
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketDetailsViewModelFactory
import com.spacex.rocket.spacexrocketinfo.utils.Constants.INTENT_KEY
import com.spacex.rocket.spacexrocketinfo.utils.Constants.NO_DESCRIPTION_TEXT
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RocketDetailsActivity : BaseActivity() {

    private lateinit var rvRocketLaunch: RecyclerView
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var apiInterface: ApiService

    @Inject
    lateinit var rocketDetailsViewModelFactory: RocketDetailsViewModelFactory

    lateinit var viewModel: RocketDetailsViewModel
    var option: RocketInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        initUIComponents()
        fetchPassedData()
        setUpViewModelIfRequired()
    }

    private fun initUIComponents() {
        rvRocketLaunch = findViewById(R.id.rv_launch_info)
        progressBar = findViewById(R.id.detail_progress)
        setUpUIComponents()

    }

    private fun setUpUIComponents() {
        changeViewVisibilityOnDataLoad(isProgressBarVisible = true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        val adapter = RocketLaunchDetailsAdapter(ArrayList())

        rvRocketLaunch.layoutManager = llm
        rvRocketLaunch.adapter = adapter
        rvRocketLaunch.addItemDecoration(SpaceItemDecoration(LinearLayoutManager.VERTICAL))
    }

    private fun fetchPassedData() {
        option = intent.extras?.get(INTENT_KEY) as RocketInfo?
    }

    private fun setUpViewModelIfRequired() {
        option?.let { info ->
            info.id?.let {
                initViewModel()
                addObserver()
                fetchData(it)
            }
        }
    }

    private fun fetchData(id: String) {
        val request = RequestQuery(id)
        viewModel.getRocketDetailsData(request)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            rocketDetailsViewModelFactory
        ).get(RocketDetailsViewModel::class.java)
    }

    private fun addObserver() {
        viewModel.rocketDetailsData.observe(this, { response: RocketDetailsResponse? ->
            when (response?.status) {
                Status.LOADING -> {
                }
                else -> {
                    response?.let {
                        updateDataInView(response)
                    }
                }
            }
        })
    }

    override fun initInjector() {
        val applicationComponent = SpaceXApp.get(this).getApplicationComponent()
        val detailComponent = DaggerDetailActivityComponent.builder()
            .spaceXAppComponent(applicationComponent)
            .build()
        detailComponent.inject(this)
    }

    private fun updateDataInView(response: RocketDetailsResponse) {
        val sortedResponse = response.data?.docs
        sortedResponse?.sortedWith(compareBy { it.dateUtc })
        sortedResponse?.let {
            val adapter = rvRocketLaunch.adapter as RocketLaunchDetailsAdapter
            val map = sortedMapOf<String, Int>()

            prepareChartDataFromMap(it, map)
            changeViewVisibilityOnDataLoad(isProgressBarVisible = false)
            adapter.setAdapterData(
                it,
                option?.description ?: NO_DESCRIPTION_TEXT,
                map
            )
            rvRocketLaunch.scheduleLayoutAnimation()

        }
    }

    private fun changeViewVisibilityOnDataLoad(isProgressBarVisible: Boolean) {
        progressBar.visibility = if (isProgressBarVisible) View.VISIBLE else View.GONE
        rvRocketLaunch.visibility = if (isProgressBarVisible) View.GONE else View.VISIBLE
    }

    private fun prepareChartDataFromMap(
        it: List<Doc>,
        map: SortedMap<String, Int>
    ) {
        it.forEach { item ->
            run {
                val date = item.dateUtc
                val key = date.substring(0, date.indexOf("-"))
                map[key] = map.getOrDefault(key, 0) + 1
            }
        }
    }
}