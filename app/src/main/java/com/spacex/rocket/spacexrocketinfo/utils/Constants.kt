package com.spacex.rocket.spacexrocketinfo.utils

object Constants {
    const val BASE_URL = "https://api.spacexdata.com/"
    const val ROCKET_LIST_ENDPOINT = "v4/rockets"
    const val LAUNCH_DETAIL_ENDPOINT = "v4/launches/query"

    const val TYPE_HEADER = 0
    const val TYPE_YEAR_HEADER = 1
    const val TYPE_ITEM = 2
    const val TYPE_LOADING = 3



    const val NUM_OF_LAUNCHES = "Number of launches per year"
    const val INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val OUTPUT_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss"
    const val SUCCESSFUL_MESSAGE = "Successful"
    const val FAILED_MESSAGE = "Failed"

    const val NO_DESCRIPTION_TEXT = "No Description Found"
    const val INTENT_KEY = "response"

    const val PREF_KEY = "firstTime"
    const val PREF_NAME = "Dialog status"

}