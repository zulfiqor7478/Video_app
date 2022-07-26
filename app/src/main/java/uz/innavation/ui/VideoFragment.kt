package uz.innavation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.MediaMetadataRetriever
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import uz.innavation.R
import uz.innavation.databinding.FragmentVideoBinding
import uz.innavation.models.Video
import uz.innavation.room.AppDatabase
import uz.innavation.ui.mainActivity.MainActivityViewModel
import uz.innavation.utils.MySharedPreference
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


open class VideoFragment : Fragment(), OnMapReadyCallback {

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var locationRequest: LocationRequest
    private lateinit var dialog: AlertDialog
    private lateinit var handler: Handler
    var height: Int? = 0
    var width: Int? = 0
    lateinit var handlerMain: Handler
    var retriever: MediaMetadataRetriever? = null
    lateinit var arrayList: ArrayList<Video>

    private lateinit var timer: CountDownTimer
    var isStart = false
    private lateinit var timer2: CountDownTimer

    private lateinit var map: GoogleMap

    var mCurrentLocation: Location? = null

    private val abs = 10001
    private lateinit var binding: FragmentVideoBinding

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(41.3775, 64.5853)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Uzbekistan"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(layoutInflater)

        setProgress()
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        arrayList = ArrayList()

        activity?.window!!.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        handler = Handler(Looper.myLooper()!!)

        setDate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(binding.root.context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations) {
                    viewModel.getAddressForLocation(binding.root.context, location)
                    viewModel.getSpeedForLocation(location)
                    mCurrentLocation = location
                    val mapFragment = childFragmentManager
                        .findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this@VideoFragment)
                    if (ActivityCompat.checkSelfPermission(
                            binding.root.context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            binding.root.context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }


                }
            }
        }

        timer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {


            }

            override fun onFinish() {

                timer.start()

                captureVideoForTwoMinutes()
                captureVideoForTwoMinutes()
            }
        }.start()

        cameraExecutor = Executors.newSingleThreadExecutor()

        startCamera()

        handlerMain = Handler(Looper.myLooper()!!)

        handlerMain.postDelayed({

            turnOnGPS()
            dialog.cancel()

            Handler(Looper.myLooper()!!).postDelayed({
                captureVideoForTwoMinutes()
            }, 2500)

            binding.videoCaptureButton.setOnClickListener {
                if (isStart) {
                    binding.videoTime.base = SystemClock.elapsedRealtime()
                    binding.videoTime.start()
                    binding.videoCaptureButton.isClickable = false
                    captureVideoForTwoMinutes()
                    captureVideo()

                    handler = Handler(Looper.myLooper()!!)

                    handler.postDelayed({
                        captureVideo()
                        captureVideoForTwoMinutes()
                        binding.videoCaptureButton.isClickable = true

                        binding.videoTime.start()
                        binding.videoTime.stop()

                    }, (MySharedPreference.videoTime!! * 1000 + 1000).toLong())
                } else {

                    Toast.makeText(
                        binding.root.context,
                        "Iltimos, dastur GPS ga bog'lanishi kuting!",
                        Toast.LENGTH_SHORT
                    ).show()


                }


            }

            setSpeed()


        }, 3000)

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


        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            binding.videoIcon.setImageResource(R.drawable.ic_video)
            recording = null
            return
        } else {

            binding.videoIcon.setImageResource(R.drawable.ic_playing_video_icon)
            val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis())
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/YxxVideos")
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


                                AppDatabase.getInstants(binding.root.context).dao().add(
                                    Video(
                                        recordEvent.outputResults.outputUri.toString(),
                                        12,
                                        12,
                                        "nn",
                                        "kkk"
                                    )
                                )


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

    private fun captureVideoForTwoMinutes() {

        binding.videoCaptureButton.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            recording = null

            return
        } else {

            startTwoMinutesVideo()

        }


    }

    private fun startTwoMinutesVideo() {
        try {
            val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis())
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/TwoMinutes")
                }
            }

            val mediaStoreOutputOptions = MediaStoreOutputOptions
                .Builder(
                    activity?.contentResolver!!,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                )
                .setContentValues(contentValues)
                .build()

            val videoCapture = this.videoCapture ?: return
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

                                Log.d(TAG, msg)
                                //  addTextProcess("/storage/emulated/0/DCIM/Camera/magic123.mp4")
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

        } catch (e: Exception) {

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
            if (MySharedPreference.videoResolution.toString() == "1080p(hd)")
                recorder.setQualitySelector(QualitySelector.from(Quality.FHD))
            else if (MySharedPreference.videoResolution.toString() == "720p")
                recorder.setQualitySelector(QualitySelector.from(Quality.HD))
            else if (MySharedPreference.videoResolution.toString() == "480p")
                recorder.setQualitySelector(QualitySelector.from(Quality.SD))
            else if (MySharedPreference.videoResolution.toString() == "360p")
                recorder.setQualitySelector(QualitySelector.from(Quality.SD))



            videoCapture = VideoCapture.withOutput(recorder.build())

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
        timer.start()
        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            binding.videoIcon.setImageResource(R.drawable.ic_video)
            recording = null
            return
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLocationUpdates(fusedLocationClient, locationCallback)
        captureVideoForTwoMinutes()
        timer.cancel()
        handler.removeCallbacksAndMessages(null)
        handlerMain.removeCallbacksAndMessages(null)
        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            binding.videoIcon.setImageResource(R.drawable.ic_video)
            recording = null
            return
        }
    }

    override fun onDestroy() {
        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            binding.videoIcon.setImageResource(R.drawable.ic_video)
            recording = null
            return
        }
        super.onDestroy()
        cameraExecutor.shutdown()
        captureVideoForTwoMinutes()
        timer.cancel()
        handler.removeCallbacksAndMessages(null)
        handlerMain.removeCallbacksAndMessages(null)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        if (ActivityCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true

        /*        if (mCurrentLocation != null) {

                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                mCurrentLocation?.latitude ?: 0.0, mCurrentLocation?.longitude ?: 0.0
                            ), 17.0f
                        )
                    )
                }*/
    }


    /*    override fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
            when (requestCode) {
                Common.VIDEO_FILE_REQUEST_CODE -> {
                    if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                   //     tvInputPathVideo.text = mediaFiles[0].path
                   //     isInputVideoSelected = true
                        CompletableFuture.runAsync {
                            retriever = MediaMetadataRetriever()
                            retriever?.setDataSource("tvInputPathVideo.text.toString()")
                            val bit = retriever?.frameAtTime
                            width = bit?.width
                            height = bit?.height
                        }
                    } else {
                    }
                }
            }
        }*/


    @SuppressLint("SimpleDateFormat")
    private fun setDate() {

        val s1 = SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date())
        binding.date.text = s1
        timer2 = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {


            }

            @SuppressLint("SimpleDateFormat")
            override fun onFinish() {

                timer2.start()


                val s = SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date())
                binding.date.text = s
            }
        }.start()!!

        Handler(Looper.myLooper()!!).postDelayed({

            isStart = true

        }, 3000)

    }

}