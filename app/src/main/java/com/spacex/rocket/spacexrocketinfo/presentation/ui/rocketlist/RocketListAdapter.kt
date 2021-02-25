package com.spacex.rocket.spacexrocketinfo.presentation.ui.rocketlist

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.spacex.rocket.spacexrocketinfo.R
import com.spacex.rocket.spacexrocketinfo.data.model.RocketInfo
import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.presentation.glide.GlideApp.with
import com.spacex.rocket.spacexrocketinfo.presentation.ui.RocketDetailsActivity

class RocketListAdapter(private var data: RocketListData) :
    RecyclerView.Adapter<RocketListAdapter.RocketListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rocket_list, parent, false)
        return RocketListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RocketListViewHolder, position: Int) {
        data[position].flickrImages?.get(0)?.let {
            with(holder.itemView.context).load(it)
                .placeholder(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.icon)
        }

        var name = "Name : ${data[position].name}"
        holder.name.text = name
        holder.country.text = "Country: ${data[position].country}"
        holder.engine.text =
            "Engine : ${data[position].engines?.let { it.number.toString() } ?: "Unnamed"}"
        holder.bind(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateList(rocketListData: RocketListData) {
        data = rocketListData
        notifyDataSetChanged()
    }


    class RocketListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var rocketInfo: RocketInfo
        var icon: ImageView
        var name: TextView
        var country: TextView
        var engine: TextView

        init {
            itemView.run {
                icon = findViewById(R.id.rocket_icon)
                name = findViewById(R.id.name)
                country = findViewById(R.id.country)
                engine = findViewById(R.id.engine)
            }
        }

        fun bind(rocketInfo: RocketInfo) {
            this.rocketInfo = rocketInfo
            itemView.setOnClickListener { _ ->
                run {
                    val intent = Intent(
                        itemView.context,
                        RocketDetailsActivity::class.java
                    )
                    intent.putExtra("response", rocketInfo)
                    var option: Bundle? = null
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        option = ActivityOptions.makeScaleUpAnimation(itemView, 100, 100, 250, 250)
                            .toBundle()
                    }
                    itemView.context.startActivity(intent, option)
                }
            }
        }

    }
}
