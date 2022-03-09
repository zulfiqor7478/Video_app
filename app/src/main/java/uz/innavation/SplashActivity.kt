package uz.innavation

import android.Manifest
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import uz.innavation.databinding.ActivitySplashBinding
import java.security.Permission

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.card.setOnClickListener {
            Dexter.withContext(this@SplashActivity)
                .withPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Toast.makeText(this@SplashActivity, "Granted", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        if (response.isPermanentlyDenied) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        val alertDialog = AlertDialog.Builder(this@SplashActivity)
                        alertDialog.setMessage("Record audio uchun ruxsat ber")
                        alertDialog.setPositiveButton("Ruxsat so'rash") { dialog, which ->
                            token?.continuePermissionRequest()
                        }
                        alertDialog.setNegativeButton("Ruxsat so'ramaslik") { dialog, which -> dialog?.dismiss() }
                        alertDialog.show()

                    }
                }).check()

            Dexter.withContext(this@SplashActivity)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Toast.makeText(this@SplashActivity, "Granted", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        if (response.isPermanentlyDenied) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        val alertDialog = AlertDialog.Builder(this@SplashActivity)
                        alertDialog.setMessage("Record audio uchun ruxsat ber")
                        alertDialog.setPositiveButton("Ruxsat so'rash") { dialog, which ->
                            token?.continuePermissionRequest()
                        }
                        alertDialog.setNegativeButton("Ruxsat so'ramaslik") { dialog, which -> dialog?.dismiss() }
                        alertDialog.show()

                    }
                }).check()

            Dexter.withContext(this@SplashActivity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Toast.makeText(this@SplashActivity, "Granted", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        if (response.isPermanentlyDenied) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        val alertDialog = AlertDialog.Builder(this@SplashActivity)
                        alertDialog.setMessage("Record audio uchun ruxsat ber")
                        alertDialog.setPositiveButton("Ruxsat so'rash") { dialog, which ->
                            token?.continuePermissionRequest()
                        }
                        alertDialog.setNegativeButton("Ruxsat so'ramaslik") { dialog, which -> dialog?.dismiss() }
                        alertDialog.show()

                    }
                }).check()

        }
    }

}