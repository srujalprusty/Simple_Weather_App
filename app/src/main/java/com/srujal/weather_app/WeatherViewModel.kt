package com.srujal.weather_app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.srujal.weather_app.Api.ApiKey
import com.srujal.weather_app.Api.NetworkResponse
import com.srujal.weather_app.Api.RetrofitInstance
import com.srujal.weather_app.Api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel(): ViewModel() {


    private val weatherApi = RetrofitInstance.weatherApi
    //Important
    private val _weatherData = MutableLiveData<NetworkResponse<WeatherModel>>()
     val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherData

    fun getData(city:String){

        _weatherData.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(ApiKey.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherData.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherData.value = NetworkResponse.Error("Failed to Load the Weather Status")
                }
            }catch (e : Exception){
                _weatherData.value = NetworkResponse.Error("Failed to Load the Weather Status")
            }
        }

    }

}