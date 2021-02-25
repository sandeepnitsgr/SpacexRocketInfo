package com.spacex.rocket.spacexrocketinfo.presentation.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.spacex.rocket.spacexrocketinfo.R
import com.spacex.rocket.spacexrocketinfo.SpaceXApp
import com.spacex.rocket.spacexrocketinfo.data.model.RocketInfo
import com.spacex.rocket.spacexrocketinfo.data.model.Status
import com.spacex.rocket.spacexrocketinfo.data.model.details.RocketDetailsResponse
import com.spacex.rocket.spacexrocketinfo.data.model.details.request.RequestQuery
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.ApiService
import com.spacex.rocket.spacexrocketinfo.di.DaggerDetailActivityComponent
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketDetailsViewModel
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketDetailsViewModelFactory
import javax.inject.Inject

class RocketDetailsActivity : BaseActivity() {

    lateinit var entries: ArrayList<Entry>
    lateinit var lineChart: LineChart

    @Inject
    lateinit var apiInterface: ApiService

    @Inject
    lateinit var rocketDetailsViewModelFactory: RocketDetailsViewModelFactory

    lateinit var viewModel: RocketDetailsViewModel
    var option: RocketInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        val tv = findViewById<TextView>(R.id.rocket_description)
        option = intent.extras?.get("response") as RocketInfo?
        tv.text = option?.description
        lineChart = findViewById(R.id.lineChart)
        entries = ArrayList()
        initLineChart()
        setUpViewModelIfRequired()
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
                    updateDataInView(response)
                }
            }
        })
    }

    private fun updateDataInView(response: RocketDetailsResponse?) {
        Log.e("sandeep", response.toString())
    }

    override fun initInjector() {
        val applicationComponent = SpaceXApp.get(this).getApplicationComponent()
        val detailComponent = DaggerDetailActivityComponent.builder()
            .spaceXAppComponent(applicationComponent)
            .build()
        detailComponent.inject(this)
    }

    private fun initLineChart() {
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
