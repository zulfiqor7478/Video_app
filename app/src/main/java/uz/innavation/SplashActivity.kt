package uz.innavation

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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
            Dexter.withContext(this@SplashActivity)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object: MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                Toast.makeText(this@SplashActivity,"ok",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {

                        token?.continuePermissionRequest()
                    }
                })
                .withErrorListener {
                    Toast.makeText(this@SplashActivity,"${it.toString()}",Toast.LENGTH_SHORT).show()
                }
                .check()
        }
    }
}