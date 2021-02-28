package com.spacex.rocket.spacexrocketinfo.data.model

import com.spacex.rocket.spacexrocketinfo.data.model.details.Doc
import java.util.*

data class LaunchDetailContainer(
    val docWithYearList: ArrayList<DocWithYear>,
    val docList: List<Doc>?,
    val countMap: SortedMap<String, Int>
)