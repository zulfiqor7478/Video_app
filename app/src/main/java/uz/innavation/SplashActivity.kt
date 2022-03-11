package uz.innavation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import uz.innavation.databinding.ActivitySplashBinding
import java.security.Permission

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var a = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.card.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@SplashActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                a++
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)

            } else {
                requestLocation(context =this )
            }

        }
    }

    private fun requestLocation(context: Context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ){
            val alertDialog=AlertDialog.Builder(this)
            alertDialog.setMessage("Joylashuv  uchun ruxsat ber")
            alertDialog.setPositiveButton("Ruxsat so'rash",object :DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                }
            })
                alertDialog.setNegativeButton("Ruxsat so'ramaslik", object :DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0?.dismiss()
                    }
                })
            alertDialog.show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
    }
}