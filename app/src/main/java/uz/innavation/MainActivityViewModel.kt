package uz.innavation

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.innavation.utils.MyTasks
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel : ViewModel() {
    private val timer = Timer()
    private var timerTask: TimerTask? = null
    private var time: Double = 0.0
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val _timeForTextView = MutableLiveData<String>()
    private val _locationText = MutableLiveData<String>()
    private val _speedText = MutableLiveData<Speed>()
    val timeForTextView : LiveData<String> = _timeForTextView
    val locationText : LiveData<String> = _locationText
    val speedText : LiveData<Speed> = _speedText




    fun getLocation(context: Context): String? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationProviderClient = FusedLocationProviderClient(context)
                fusedLocationProviderClient?.lastLocation
                    ?.addOnSuccessListener { location ->
                        if (location != null) {
                            val lat = location.latitude
                            val long = location.longitude
                            CoroutineScope((Dispatchers.Main)).launch {
                                _locationText.value = "$lat $long"
                            }
                            getNameBaseOnLocation(context, lat, long)
                        }
                    }
            } else {
                //requestPermissons
            }
        }
        return null
    }

    private fun getNameBaseOnLocation(context: Context, latitude : Double, longitude: Double) {
        try{
            val geo =  Geocoder(context, Locale.getDefault());
            val addresses = geo.getFromLocation(latitude, longitude, 1)
            if (addresses.isEmpty()) {
                Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
            }else{
                CoroutineScope((Dispatchers.Main)).launch {
                    _locationText.value = addresses[0].countryName
                }
            }
        }
        catch (e :Exception) {
            e.printStackTrace();
        }
    }

    fun startTimer() {
        timerTask = object : TimerTask() {
            override fun run() {
                time++
                CoroutineScope((Dispatchers.Main)).launch {
                    _timeForTextView.value = MyTasks.getTimeText(time)
                }
            }

        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    fun stopTimer() {
        if (timerTask != null) {
            timerTask?.cancel()
            CoroutineScope((Dispatchers.Main)).launch {
                _timeForTextView.value = MyTasks.getTimeText(0.0)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getAddressForLocation(context: Context?, location: Location?): Address? {
        if (location == null) {
            return null
        }
        val latitude = location.latitude
        val longitude = location.longitude
        val speed = location.speed
        var speedKpPerHour = speed * 3.6
        _speedText.value = Speed(speedKpPerHour.toFloat(), 500)

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