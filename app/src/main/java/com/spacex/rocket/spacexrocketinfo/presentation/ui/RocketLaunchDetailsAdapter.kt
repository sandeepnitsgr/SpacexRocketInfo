package com.spacex.rocket.spacexrocketinfo.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.spacex.rocket.spacexrocketinfo.R
import com.spacex.rocket.spacexrocketinfo.data.model.details.Doc
import com.spacex.rocket.spacexrocketinfo.presentation.glide.GlideApp

class RocketLaunchDetailsAdapter(var response: List<Doc>) :
    RecyclerView.Adapter<RocketLaunchDetailsAdapter.LaunchDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_rocket_launch_detail, parent, false)
        return LaunchDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaunchDetailsViewHolder, position: Int) {
        GlideApp.with(holder.itemView.context).load(response[position].links.patch.small)
            .placeholder(R.drawable.no_image)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(holder.icon)
        holder.missionName.text = response[position].name
        holder.launchDate.text = response[position].dateUtc
        holder.launchSuccessFul.text = response[position].success.toString()
    }

    override fun getItemCount(): Int {
        return response.size
    }

    fun setAdapterData(response: List<Doc>) {
        this.response = response
        notifyDataSetChanged()
    }

    class LaunchDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon : ImageView
        var missionName : TextView
        var launchDate:TextView
        var launchSuccessFul : TextView

        init {
            itemView.run {
                icon = findViewById(R.id.launch_info_icon)
                missionName = findViewById(R.id.mission_name)
                launchDate = findViewById(R.id.launch_date)
                launchSuccessFul = findViewById(R.id.launch_status)
            }
        }
    }
}

