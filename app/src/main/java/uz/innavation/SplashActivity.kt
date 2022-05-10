package uz.innavation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import uz.innavation.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.card.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@SplashActivity,
                    Manifest.permission.CAMERA
                )
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this@SplashActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this@SplashActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                binding.tvCard.text = "Ruxsat berildi"
                val intent=Intent(this@SplashActivity,MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@SplashActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                requestRecordAudio()
            }

        }
    }

    private fun requestRecordAudio() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)
        ) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("Record audio uchun ruxsat ber")
            alertDialog.setPositiveButton("Ruxsat so'rash") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@SplashActivity,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA
                    ),
                    1
                )
            }
            alertDialog.setNegativeButton("Ruxsat so'ramaslik") { dialog, which -> dialog?.dismiss() }
            alertDialog.show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA),
                1

            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.size == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                binding.imageGpsX.setImageResource(R.drawable.right_icon)
                Toast.makeText(this, "Ruxsat 1", Toast.LENGTH_SHORT).show()
            }
            if (grantResults.size == 3 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                binding.imageKameraX.setImageResource(R.drawable.right_icon)
                Toast.makeText(this, "Ruxsat 2", Toast.LENGTH_SHORT).show()
            }
            if (grantResults.size == 3 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                binding.imageGalleriyaX.setImageResource(R.drawable.right_icon)
                Toast.makeText(this, "Ruxsat 3", Toast.LENGTH_SHORT).show()
            }
             if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )|| ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )|| ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                Toast.makeText(this, "Ruxsat berilmadi", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }
}