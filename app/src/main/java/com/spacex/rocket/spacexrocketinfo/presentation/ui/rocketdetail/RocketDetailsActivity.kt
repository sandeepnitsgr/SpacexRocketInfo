package com.spacex.rocket.spacexrocketinfo.presentation.ui.rocketdetail

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spacex.rocket.spacexrocketinfo.R
import com.spacex.rocket.spacexrocketinfo.SpaceXApp
import com.spacex.rocket.spacexrocketinfo.data.model.LaunchDetailContainer
import com.spacex.rocket.spacexrocketinfo.data.model.RocketInfo
import com.spacex.rocket.spacexrocketinfo.data.model.Status
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.PagingOption
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.Request
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.RequestQuery
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.ApiService
import com.spacex.rocket.spacexrocketinfo.di.DaggerDetailActivityComponent
import com.spacex.rocket.spacexrocketinfo.presentation.ui.BaseActivity
import com.spacex.rocket.spacexrocketinfo.presentation.ui.SpaceItemDecoration
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketDetailsViewModel
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketDetailsViewModelFactory
import com.spacex.rocket.spacexrocketinfo.utils.Constants.INTENT_KEY
import com.spacex.rocket.spacexrocketinfo.utils.Constants.NO_DESCRIPTION_TEXT
import javax.inject.Inject

class RocketDetailsActivity : BaseActivity() {

    private lateinit var rvRocketLaunch: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    @Inject
    lateinit var apiInterface: ApiService

    @Inject
    lateinit var rocketDetailsViewModelFactory: RocketDetailsViewModelFactory

    lateinit var viewModel: RocketDetailsViewModel
    var option: RocketInfo? = null

    lateinit var request: Request
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        fetchPassedData()
        initUIComponents()
        setUpViewModelIfRequired()
    }

    private fun initUIComponents() {
        rvRocketLaunch = findViewById(R.id.rv_launch_info)
        progressBar = findViewById(R.id.detail_progress)
        errorTextView = findViewById(R.id.tv_error_message)
        setUpUIComponents()

    }

    private fun setUpUIComponents() {
        changeViewVisibilityOnDataLoad(
            progressBarVisibility = View.VISIBLE,
            rvVisibility = View.GONE
        )
        errorTextView.visibility = View.GONE
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
        val request = Request(RequestQuery(id))
        request.option = null
        viewModel.getRocketDetailsData(request)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            rocketDetailsViewModelFactory
        ).get(RocketDetailsViewModel::class.java)
    }

    private fun addObserver() {
        viewModel.shouldShowProgressBarLiveData.observe(this, { shouldShow: Boolean ->
            when (shouldShow) {
                true -> changeViewVisibilityOnDataLoad(
                    progressBarVisibility = View.VISIBLE,
                    rvVisibility = View.GONE
                )
                false -> {
                    changeViewVisibilityOnDataLoad(
                        progressBarVisibility = View.GONE,
                        rvVisibility = View.VISIBLE
                    )
                }
            }
        }

        )
        viewModel.customDetailData.observe(this, { response: RocketDetailsResponse? ->
            when (response?.status) {
                Status.SUCCESS -> {
                    response.let { res ->
                        res.data?.let {
                            updateDataInView(it, res.page)
                        }
                    }

                }
                else -> {
                    errorTextView.visibility = View.VISIBLE
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

    private fun updateDataInView(launchDetailContainer: LaunchDetailContainer, page: Int) {
        launchDetailContainer.let {
            val adapter = rvRocketLaunch.adapter as RocketLaunchDetailsAdapter

            adapter.setAdapterData(
                launchDetailContainer.docWithYearList,
                option?.description ?: NO_DESCRIPTION_TEXT,
                launchDetailContainer.countMap,
                page
            )
            rvRocketLaunch.scheduleLayoutAnimation()

        }
    }

    private fun changeViewVisibilityOnDataLoad(
        progressBarVisibility: Int,
        rvVisibility: Int
    ) {
        progressBar.visibility = progressBarVisibility
        rvRocketLaunch.visibility = rvVisibility

    }

}