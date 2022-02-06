package com.challenge.foodlover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.dispatcher.DispatcherMap
import com.challenge.domain.model.Restaurant
import com.challenge.domain.usecase.GetSortedRestaurantListUseCase
import com.challenge.domain.usecase.ToggleRestaurantFavoriteStatusUseCase
import kotlinx.coroutines.launch

class FoodLoverViewModel(
    private val getSortedRestaurantList: GetSortedRestaurantListUseCase,
    private val toggleRestaurantFavoriteStatus: ToggleRestaurantFavoriteStatusUseCase,
    private val dispatcherMap: DispatcherMap
) : ViewModel() {

    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> = _restaurants

    init {
        updateRestaurantList()
    }

    fun onTestButtonClicked() = updateRestaurantList()

    private fun updateRestaurantList() {
        viewModelScope.launch(dispatcherMap.io) {
            val result = getSortedRestaurantList()
            _restaurants.postValue(result)
        }
    }

    fun toggleFavoriteRestaurant(position: Int) {
        viewModelScope.launch(dispatcherMap.io) {
            val items = _restaurants.value.orEmpty()
            toggleRestaurantFavoriteStatus(items[position])
            _restaurants.postValue(items)
        }
    }
}
