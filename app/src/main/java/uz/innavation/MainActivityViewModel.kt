package uz.innavation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.innavation.utils.MyTasks
import java.util.*

class MainActivityViewModel : ViewModel() {
    private val timer = Timer()
    private var timerTask: TimerTask? = null
    private var time: Double = 0.0
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    val timeForTextView = MutableLiveData<String>()
    val locationText = MutableLiveData<String>()


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
                                locationText.value = "$lat $long"
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
                    locationText.value = addresses[0].countryName
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
                    timeForTextView.value = MyTasks.getTimeText(time)
                }
            }

        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    fun stopTimer() {
        if (timerTask != null) {
            timerTask?.cancel()
            CoroutineScope((Dispatchers.Main)).launch {
                timeForTextView.value = MyTasks.getTimeText(0.0)
            }
        }
    }
}