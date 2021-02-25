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
import com.spacex.rocket.spacexrocketinfo.data.model.details.Doc
import com.spacex.rocket.spacexrocketinfo.presentation.glide.GlideApp
import com.spacex.rocket.spacexrocketinfo.utils.Constants.FAILED_MESSAGE
import com.spacex.rocket.spacexrocketinfo.utils.Constants.INPUT_DATE_FORMAT
import com.spacex.rocket.spacexrocketinfo.utils.Constants.NUM_OF_LAUNCHES
import com.spacex.rocket.spacexrocketinfo.utils.Constants.OUTPUT_DATE_FORMAT
import com.spacex.rocket.spacexrocketinfo.utils.Constants.SUCCESSFUL_MESSAGE
import com.spacex.rocket.spacexrocketinfo.utils.Constants.TYPE_HEADER
import com.spacex.rocket.spacexrocketinfo.utils.Constants.TYPE_ITEM
import java.text.SimpleDateFormat
import java.util.*

class RocketLaunchDetailsAdapter(var response: List<Doc>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var descriptionLaunch: String = ""
    lateinit var pairMap: SortedMap<String, Int>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.rocket_launch_details, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_rocket_launch_detail, parent, false)
            LaunchDetailsViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LaunchDetailsViewHolder) {
            setDataInLaunchDetailViewHolder(holder, position)
        } else if (holder is HeaderViewHolder) {
            setDataInHeaderViewHolder(holder)
        }
    }

    private fun setDataInHeaderViewHolder(holder: HeaderViewHolder) {
        holder.tvRocketDescription.text = descriptionLaunch
        if (::pairMap.isInitialized)
            initLineChartData(holder.lineChart)
    }

    private fun setDataInLaunchDetailViewHolder(
        holder: LaunchDetailsViewHolder,
        position: Int
    ) {
        GlideApp.with(holder.itemView.context).load(response[position - 1].links.patch.small)
            .placeholder(R.drawable.no_image)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.icon)
        holder.missionName.text = response[position - 1].name
        holder.launchDate.text = convertToFormattedDate(response[position - 1].dateUtc)
        holder.launchSuccessFul.text = when (response[position - 1].success) {
            true -> SUCCESSFUL_MESSAGE
            else -> FAILED_MESSAGE
        }
    }

    private fun initLineChartData(lineChart: LineChart) {
        val countList = mutableListOf<Entry>()

        val maxRange = pairMap.values.maxOrNull()
        val minRange = pairMap.values.minOrNull()

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

        lineChart.data = lineDataSets

        lineChart.xAxis.mAxisMaximum = maxRange?.toFloat() ?: 1f
        lineChart.xAxis.mAxisMinimum = minRange?.toFloat() ?: -1f
        lineChart.xAxis.mAxisRange = 1f
        lineChart.description.isEnabled = false
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
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
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    fun setAdapterData(response: List<Doc>, description: String, map: SortedMap<String, Int>) {
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
}
