package com.challenge.data.remote.network

import android.content.Context
import com.challenge.data.remote.response.RestaurantResponse
import com.challenge.data.remote.response.RestaurantResponseWrapper
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.IOException

private const val DEFAULT_RESTAURANTS_FILENAME = "default_restaurant_collection.json"

internal object FoodLoverClientBuilder {

    fun buildGson() = Gson()

    fun buildApiClient(context: Context, gson: Gson): FoodLoverApi = object : FoodLoverApi {
        override suspend fun fetchRestaurants(): List<RestaurantResponse> =
            parseFromJsonToResponse(
                jsonResponse = getJsonDataFromAsset(context = context),
                gson = gson
            )
    }

    private fun parseFromJsonToResponse(jsonResponse: String?, gson: Gson) =
        try {
            gson.fromJson(jsonResponse, RestaurantResponseWrapper::class.java).restaurants
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            listOf()
        }

    private suspend fun getJsonDataFromAsset(context: Context): String? =
        try {
            context
                .assets
                .open(DEFAULT_RESTAURANTS_FILENAME)
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
}
