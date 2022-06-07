package uz.innavation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.runtimepermission.kotlin.askPermission
import uz.innavation.databinding.ActivitySplashBinding
import uz.innavation.ui.mainActivity.MainActivity
import uz.innavation.ui.registration.RegisterMainActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )





         allow()
    }


    private fun allow() {

        askPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
        ) {

            if (it.isAccepted) {
                Handler(Looper.myLooper()!!).postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
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
}