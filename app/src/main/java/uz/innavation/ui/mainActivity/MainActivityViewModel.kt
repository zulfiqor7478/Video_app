package uz.innavation.ui.mainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.innavation.utils.Speed
import java.util.*

class MainActivityViewModel : ViewModel() {
    private val _timeForTextView = MutableLiveData<String>()
    private val _locationText = MutableLiveData<String>()
    private val _speedText = MutableLiveData<Speed>()
    val timeForTextView: LiveData<String> = _timeForTextView
    val locationText: LiveData<String> = _locationText
    val speedText: LiveData<Speed> = _speedText


    fun getSpeedForLocation(location: Location?) {
        if (location != null) {
            val speedKpPerHour = location.speed * 3.6
            _speedText.value = Speed(speedKpPerHour.toFloat(), 500)
        }
    }

    fun startLocationUpdates(
        context: Context,
        fusedLocationClient: FusedLocationProviderClient,
        locationCallback: LocationCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }

    fun stopLocationUpdates(
        fusedLocationClient: FusedLocationProviderClient,
        locationCallback: LocationCallback
    ) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("SetTextI18n")
    fun getAddressForLocation(context: Context?, location: Location?): Address? {
        if (location == null) {
            return null
        }
        val latitude = location.latitude
        val longitude = location.longitude

        val maxResults = 1
        try {
            val gc = Geocoder(context, Locale.getDefault())
            val addresses = gc.getFromLocation(latitude, longitude, maxResults)
            return if (addresses.size == 1) {
                _locationText.value =
                    "${addresses[0].thoroughfare ?: "__"} / ${addresses[0].locality} / ${addresses[0].countryName}"
                addresses[0]
            } else {
                null
            }
        } catch (e: java.lang.Exception) {
        }
        return null
    }

}