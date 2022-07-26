package uz.innavation.ui.mainActivity

import android.content.Context
import android.content.IntentSender
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import uz.innavation.databinding.ActivityMainBinding
import uz.innavation.utils.MySharedPreference
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale(this, MySharedPreference.language!!)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        turnOnGPS()


        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(viewBinding.root)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        MySharedPreference.isLogin = true
        setAppLocale(this, MySharedPreference.language!!)


    }


    private fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }

    override fun onDestroy() {
        setAppLocale(this, MySharedPreference.language!!)
        super.onDestroy()
    }

    private fun turnOnGPS() {

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())



        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)


            } catch (e: ApiException) {

                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                        try {

                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                this,
                                1
                            )


                        } catch (e: IntentSender.SendIntentException) {
                            e.printStackTrace()
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                        //Device does not have location
                    }
                }
            }
        }


    }

}