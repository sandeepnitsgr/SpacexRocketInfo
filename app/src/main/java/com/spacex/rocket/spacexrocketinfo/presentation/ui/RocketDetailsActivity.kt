package com.spacex.rocket.spacexrocketinfo.presentation.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.spacex.rocket.spacexrocketinfo.R
import com.spacex.rocket.spacexrocketinfo.SpaceXApp
import com.spacex.rocket.spacexrocketinfo.data.model.RocketInfo
import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.data.model.Status
import com.spacex.rocket.spacexrocketinfo.data.model.details.Doc
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.RequestQuery
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.ApiService
import com.spacex.rocket.spacexrocketinfo.di.DaggerDetailActivityComponent
import com.spacex.rocket.spacexrocketinfo.presentation.ui.rocketlist.RocketListAdapter
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketDetailsViewModel
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketDetailsViewModelFactory
import javax.inject.Inject

class RocketDetailsActivity : BaseActivity() {

    lateinit var entries: ArrayList<Entry>
    lateinit var lineChart: LineChart
    lateinit var tvRocketDescription: TextView
    lateinit var rvRocketLaunch: RecyclerView

    @Inject
    lateinit var apiInterface: ApiService

    @Inject
    lateinit var rocketDetailsViewModelFactory: RocketDetailsViewModelFactory

    lateinit var viewModel: RocketDetailsViewModel
    var option: RocketInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        fetchPassedData()
        initUIComponents()
        setUpViewModelIfRequired()
    }

    private fun initUIComponents() {
        lineChart = findViewById(R.id.lineChart)
        tvRocketDescription = findViewById(R.id.rocket_description)
        rvRocketLaunch = findViewById(R.id.rv_launch_info)
        setDataInUIComponents()

    }

    private fun setDataInUIComponents() {
        tvRocketDescription.text = option?.description ?: "No Description Found"
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvRocketLaunch.layoutManager = llm
        rvRocketLaunch.adapter = RocketLaunchDetailsAdapter(ArrayList())
        rvRocketLaunch.addItemDecoration(SpaceItemDecoration(LinearLayoutManager.VERTICAL))
        rvRocketLaunch.isNestedScrollingEnabled = false
    }

    private fun fetchPassedData() {
        option = intent.extras?.get("response") as RocketInfo?
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

    private fun updateDataInView(response: RocketDetailsResponse) {
        Log.e("sandeep", response.data?.docs.toString())
        initLineChart(response)
    }

    override fun initInjector() {
        val applicationComponent = SpaceXApp.get(this).getApplicationComponent()
        val detailComponent = DaggerDetailActivityComponent.builder()
            .spaceXAppComponent(applicationComponent)
            .build()
        detailComponent.inject(this)
    }

    private fun initLineChart(response: RocketDetailsResponse) {
        val sortedResponse = response.data?.docs
        Log.e("sandeep", sortedResponse.toString())
        sortedResponse?.sortedWith(compareBy { it.dateUtc })
        sortedResponse?.let {
            (rvRocketLaunch.adapter as RocketLaunchDetailsAdapter).setAdapterData(it)
        }
        entries = ArrayList()
        val revenueComp1 = arrayListOf(10000f, 20000f, 30000f, 40000f)
        val revenueComp2 = arrayListOf(22000f, 23000f, 5000f, 48000f)

        val entries1 = revenueComp1.mapIndexed { index, arr ->
            Entry(index.toFloat(), arr)
        }

        val entries2 = revenueComp2.mapIndexed { index, arrayList ->
            Entry(index.toFloat(), arrayList)
        }

        val lineDataSet1 = LineDataSet(entries1, "Company 1")
        lineDataSet1.color = Color.RED
        lineDataSet1.setDrawValues(false)
        lineDataSet1.axisDependency = YAxis.AxisDependency.LEFT

        val lineDataSet2 = LineDataSet(entries2, "Company 2")
        lineDataSet2.color = Color.BLUE
        lineDataSet1.setDrawValues(false)
        lineDataSet2.axisDependency = YAxis.AxisDependency.LEFT

        val lineDataSets = LineData(lineDataSet1, lineDataSet2)

        lineChart.data = lineDataSets

        lineChart.axisLeft.mAxisMaximum = 1f
        lineChart.axisLeft.mAxisMinimum = -1f
        lineChart.axisLeft.mAxisRange = 2f
    }

}
