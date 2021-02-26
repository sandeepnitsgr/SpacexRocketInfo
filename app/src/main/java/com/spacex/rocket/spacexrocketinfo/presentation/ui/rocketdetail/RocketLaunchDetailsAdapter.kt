package com.spacex.rocket.spacexrocketinfo.presentation.ui.rocketdetail

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.spacex.rocket.spacexrocketinfo.R
import com.spacex.rocket.spacexrocketinfo.data.model.DocWithYear
import com.spacex.rocket.spacexrocketinfo.presentation.glide.GlideApp
import com.spacex.rocket.spacexrocketinfo.utils.Constants.FAILED_MESSAGE
import com.spacex.rocket.spacexrocketinfo.utils.Constants.INPUT_DATE_FORMAT
import com.spacex.rocket.spacexrocketinfo.utils.Constants.NUM_OF_LAUNCHES
import com.spacex.rocket.spacexrocketinfo.utils.Constants.OUTPUT_DATE_FORMAT
import com.spacex.rocket.spacexrocketinfo.utils.Constants.SUCCESSFUL_MESSAGE
import com.spacex.rocket.spacexrocketinfo.utils.Constants.TYPE_HEADER
import com.spacex.rocket.spacexrocketinfo.utils.Constants.TYPE_ITEM
import com.spacex.rocket.spacexrocketinfo.utils.Constants.TYPE_YEAR_HEADER
import java.text.SimpleDateFormat
import java.util.*

class RocketLaunchDetailsAdapter(var response: List<DocWithYear>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var descriptionLaunch: String = ""
    lateinit var pairMap: SortedMap<String, Int>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.rocket_launch_details, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_YEAR_HEADER -> {
                val view = inflater.inflate(R.layout.year_header_layout, parent, false)
                HeaderYearViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_rocket_launch_detail, parent, false)
                LaunchDetailsViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LaunchDetailsViewHolder -> {
                setDataInLaunchDetailViewHolder(holder, position)
            }
            is HeaderYearViewHolder -> {
                setYearInYearViewHolder(holder, position)
            }
            is HeaderViewHolder -> {
                setDataInHeaderViewHolder(holder)
            }
        }
    }

    private fun setYearInYearViewHolder(
        holder: HeaderYearViewHolder,
        position: Int
    ) {
        holder.yearHeaderTextView.text = response[position - 1].year
    }

    private fun setDataInHeaderViewHolder(holder: HeaderViewHolder) {
        holder.tvRocketDescription.text = descriptionLaunch
        if (::pairMap.isInitialized)
            updateLineChartData(holder.lineChart)
        else {
            holder.lineChart.visibility = View.GONE
        }
    }

    private fun setDataInLaunchDetailViewHolder(
        holder: LaunchDetailsViewHolder,
        position: Int
    ) {
        val url = response[position - 1].doc!!.links.patch.small
        val glide = GlideApp.with(holder.itemView.context)
        url?.let {
            glide.load(url)
                .placeholder(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.icon)
        } ?: run {
            glide.load(R.drawable.no_image).into(holder.icon)
        }


        holder.missionName.text = response[position - 1].doc!!.name
        holder.launchDate.text = convertToFormattedDate(response[position - 1].doc!!.dateUtc)
        holder.launchSuccessFul.text = when (response[position - 1].doc!!.success) {
            true -> SUCCESSFUL_MESSAGE
            else -> FAILED_MESSAGE
        }
    }

    private fun updateLineChartData(lineChart: LineChart) {
        val countList = mutableListOf<Entry>()
        if (pairMap.isNullOrEmpty()) {
            lineChart.visibility = View.GONE
            return
        }
        lineChart.visibility = View.VISIBLE
        pairMap.forEach { entry ->
            run {
                countList.add(Entry(Integer.parseInt(entry.key).toFloat(), entry.value.toFloat()))
            }
        }
        val xAxisData = LineDataSet(countList, NUM_OF_LAUNCHES)
        xAxisData.color = Color.RED
        xAxisData.setDrawValues(false)
        xAxisData.axisDependency = YAxis.AxisDependency.LEFT

        val lineDataSets = LineData(xAxisData)

        setLineChartProperties(lineChart)
        lineChart.data = lineDataSets

        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
    }

    private fun setLineChartProperties(lineChart: LineChart) {
        val maxRange = pairMap.values.maxOrNull()
        lineChart.xAxis.mAxisMaximum = maxRange?.toFloat() ?: 1f
        lineChart.xAxis.mAxisMinimum = 0f
        lineChart.xAxis.mAxisRange = 1f
        lineChart.description.isEnabled = false
    }

    private fun convertToFormattedDate(dateUtc: String): String {
        val inputFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.ENGLISH)
        val date = inputFormat.parse(dateUtc)
        return date?.let {
            outputFormat.format(it)
        } ?: dateUtc
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

    override fun getItemCount(): Int {
        return response.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position))
            TYPE_HEADER
        else if (isPositionYearHeader(position))
            TYPE_YEAR_HEADER
        else
            TYPE_ITEM
    }

    private fun isPositionYearHeader(position: Int): Boolean {
        return !response[position - 1].year.isNullOrBlank()
    }

    fun setAdapterData(response: List<DocWithYear>, description: String, map: SortedMap<String, Int>) {
        this.response = response
        descriptionLaunch = description
        pairMap = map
        notifyDataSetChanged()
    }

    class LaunchDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView
        var missionName: TextView
        var launchDate: TextView
        var launchSuccessFul: TextView

        init {
            itemView.run {
                icon = findViewById(R.id.launch_info_icon)
                missionName = findViewById(R.id.mission_name)
                launchDate = findViewById(R.id.launch_date)
                launchSuccessFul = findViewById(R.id.launch_status)
            }
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lineChart: LineChart
        var tvRocketDescription: TextView

        init {
            itemView.run {
                tvRocketDescription = findViewById(R.id.rocket_description)
                lineChart = findViewById(R.id.lineChart)
            }
        }
    }

    class HeaderYearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var yearHeaderTextView: TextView

        init {
            itemView.run {
                yearHeaderTextView = findViewById(R.id.year_header_tv)
            }
        }
    }
}
