package com.example.helixproject

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.helixproject.databinding.Fragment2Binding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class Fragment2 : Fragment() {

    companion object {
        const val API_KEY: String = "bc07e6ea8d9713b2761367afa2012991"
        const val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/weather"
        const val MIN_TIME: Long = 5000
        const val MIN_DISTANCE: Float = 1000F
        const val WEATHER_REQUEST: Int = 102
    }

    private var binding: Fragment2Binding? = null
    private lateinit var weatherState: TextView
    private lateinit var temperature: TextView
    private lateinit var weatherIcon: ImageView

    private lateinit var mLocationManager: LocationManager
    private lateinit var mLocationListener: LocationListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding?.startFragment = this
        binding?.apply {
            temperature = temperatureTv
            weatherState = weatherTv
            weatherIcon = weatherIc
        }

    }

    override fun onResume() {
        super.onResume()
        getWeatherInCurrentLocation()
    }

    private fun getWeatherInCurrentLocation() {
        mLocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mLocationListener = LocationListener { p0 ->
            val params: RequestParams = RequestParams()
            params.put("lat", p0.latitude)
            params.put("lon", p0.longitude)
            params.put("appid", API_KEY)
            doNetworking(params)
        }
        //

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>( android.Manifest.permission.ACCESS_FINE_LOCATION),
                WEATHER_REQUEST
            )
            return
        }
        mLocationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_TIME,
            MIN_DISTANCE,
            mLocationListener
        )
        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME,
            MIN_DISTANCE,
            mLocationListener
        )
    }


    private fun doNetworking(params: RequestParams) {
        var client = AsyncHttpClient()

        client.get(WEATHER_URL, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                val weatherData = WeatherData().fromJson(response)
                if (weatherData != null) {
                    updateWeather(weatherData)
                }
            }
        })
    }

    private fun updateWeather(weather: WeatherData) {
        temperature.setText(weather.tempString + " ℃")
        weatherState.setText(weather.weatherType)
        val resourceID = resources.getIdentifier(weather.icon, "drawable", activity?.packageName)
        weatherIcon.setImageResource(resourceID)
    }

    override fun onPause() {
        super.onPause()
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener)
        }
    }

    class WeatherData{

        lateinit var tempString: String
        lateinit var icon: String
        lateinit var weatherType: String
        private var weatherId: Int = 0
        private var tempInt: Int =0

        fun fromJson(jsonObject: JSONObject?): WeatherData? {
            try{
                var weatherData = WeatherData()
                weatherData.weatherId = jsonObject?.getJSONArray("weather")?.getJSONObject(0)?.getInt("id")!!
                weatherData.weatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
                weatherData.icon = updateWeatherIcon(weatherData.weatherId)
                val roundedTemp: Int = (jsonObject.getJSONObject("main").getDouble("temp")-273.15).toInt()
                weatherData.tempString = roundedTemp.toString()
                weatherData.tempInt = roundedTemp
                return weatherData
            }catch (e: JSONException){
                e.printStackTrace()
                return null
            }
        }

        private fun updateWeatherIcon(condition: Int): String {
            if (condition in 200..299) {
                return "thunderstorm"
            } else if (condition in 300..499) {
                return "lightrain"
            } else if (condition in 500..599) {
                return "rain"
            } else if (condition in 600..700) {
                return "snow"
            } else if (condition in 701..771) {
                return "fog"
            } else if (condition in 772..799) {
                return "overcast"
            } else if (condition == 800) {
                return "clear"
            } else if (condition in 801..804) {
                return "cloudy"
            } else if (condition in 900..902) {
                return "thunderstorm"
            }
            if (condition == 903) {
                return "snow"
            }
            if (condition == 904) {
                return "clear"
            }
            return if (condition in 905..1000) {
                "thunderstorm"
            } else "dunno"

        }

    }
}
