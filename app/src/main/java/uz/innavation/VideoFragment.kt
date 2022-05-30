package uz.innavation

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import uz.innavation.databinding.FragmentVideoBinding
import uz.innavation.ui.mainActivity.MainActivityViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VideoFragment : Fragment() {
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var locationRequest: LocationRequest
    private lateinit var dialog: AlertDialog

    private val abs = 10001
    private lateinit var binding: FragmentVideoBinding
    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(layoutInflater)

        setProgress()
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(binding.root.context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations) {
                    viewModel.getAddressForLocation(binding.root.context, location)
                    viewModel.getSpeedForLocation(location)
                }
            }
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        Handler(Looper.myLooper()!!).postDelayed({

            dialog.cancel()

            turnOnGPS()
            startCamera()

            binding.videoCaptureButton.setOnClickListener {
                captureVideo()
            }

            setSpeed()

        }, 1500)

        binding.home.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setSpeed() {
        viewModel.speedText.observe(viewLifecycleOwner) {
            binding.speedText.text = it.speed.toInt().toString()

            if (it.speed.toInt() > 0)
                binding.content.startRippleAnimation()
            else
                binding.content.stopRippleAnimation()

        }
    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        binding.videoCaptureButton.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            recording = null
            binding.videoIcon.setImageResource(R.drawable.ic_video)

            return
        } else {

            binding.videoIcon.setImageResource(R.drawable.ic_playing_video_icon)

            val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis())
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Yxx")
                }
            }

            val mediaStoreOutputOptions = MediaStoreOutputOptions
                .Builder(activity?.contentResolver!!, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues)
                .build()
            recording = videoCapture.output
                .prepareRecording(binding.root.context, mediaStoreOutputOptions)
                .apply {
                    if (PermissionChecker.checkSelfPermission(
                            binding.root.context,
                            Manifest.permission.RECORD_AUDIO
                        ) ==
                        PermissionChecker.PERMISSION_GRANTED
                    ) {
                        withAudioEnabled()
                    }
                }
                .start(ContextCompat.getMainExecutor(binding.root.context)) { recordEvent ->
                    when (recordEvent) {
                        is VideoRecordEvent.Start -> {
                            binding.videoCaptureButton.apply {
                                //     text = getString(R.string.stop_capture)
                                isEnabled = true
                            }
                        }
                        is VideoRecordEvent.Finalize -> {
                            if (!recordEvent.hasError()) {
                                //        viewModel.stopTimer()
                                val msg = "Video capture succeeded: " +
                                        "${recordEvent.outputResults.outputUri}"
                                Toast.makeText(
                                    binding.root.context,
                                    "Video saqlandi!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                Log.d(TAG, msg)
                            } else {
                                recording?.close()
                                recording = null
                                Log.e(
                                    TAG, "Video capture ends with error: " +
                                            "${recordEvent.error}"
                                )
                            }
                            binding.videoCaptureButton.apply {
                                //   text = getString(R.string.start_capture)
                                isEnabled = true
                            }
                        }
                    }
                }
        }


    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(binding.root.context)


        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.SD))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(binding.root.context))
    }


    private fun turnOnGPS() {

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(binding.root.context)
            .checkLocationSettings(builder.build())



        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)


                Toast.makeText(binding.root.context, "GPS is already toured on", Toast.LENGTH_SHORT)
                    .show()


            } catch (e: ApiException) {

                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                        try {

                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                requireActivity(),
                                abs
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

    private fun isGPSEnabled(): Boolean {
        var locationManager: LocationManager? = null

        if (locationManager == null) {
            locationManager =
                activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }


    private fun setProgress() {
        dialog = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.custom_progress, null, false)
        dialog.setView(view)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        viewModel.startLocationUpdates(binding.root.context, fusedLocationClient, locationCallback)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLocationUpdates(fusedLocationClient, locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}
