package uz.innavation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.runtimepermission.kotlin.askPermission
import uz.innavation.databinding.ActivitySplashBinding
import uz.innavation.ui.mainActivity.MainActivity
import uz.innavation.ui.registration.RegisterMainActivity
import uz.innavation.utils.MySharedPreference
import java.util.*


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
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
        setContentView(binding.root)


        setAppLocale(this, MySharedPreference.language!!)




        allow()
    }


    private fun allow() {

        askPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        ) {

            if (it.isAccepted) {
                Handler(Looper.myLooper()!!).postDelayed({
                    if (!MySharedPreference.isLogin!!) {
                        startActivity(Intent(this, RegisterMainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }, 2000)

            }

        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Iltimos, ruxsatnomaga ruxsat bering. Aks holda siz bizning dasturimizdan foydalana olmaysiz!")
                    .setPositiveButton(
                        "Ok"
                    ) { _, _ -> e.askAgain() }
                    .setNegativeButton(
                        "No"
                    ) { dialog, _ -> dialog?.dismiss() }
                    .show()


            }
            if (e.hasForeverDenied()) {

                e.goToSettings()
            }


        }

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
}