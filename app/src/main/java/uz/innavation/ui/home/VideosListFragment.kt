package uz.innavation.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.innavation.adapters.RecyclerViewAdapter
import uz.innavation.databinding.FragmentVideosListBinding
import uz.innavation.utils.Method
import uz.innavation.utils.StorageUtil
import java.io.File


class VideosListFragment : Fragment() {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var binding: FragmentVideosListBinding


    private var fileUri: Uri? = null

    //
    private val permission = false
    private var storage: File? = null
    private lateinit var storagePaths: Array<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideosListBinding.inflate(layoutInflater)


        checkStorageAccessPermission();

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }


        recyclerViewAdapter =  RecyclerViewAdapter(binding.root.context)

        binding.rv.adapter = recyclerViewAdapter;


        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //load data here
                //for first time data will be loaded here
                //then it will be loaded in splash screen
                //because if we could not have permission then we could not load data in splash screen window
                storagePaths = StorageUtil.getStorageDirectories(binding.root.context)
                for (path in storagePaths) {
                    storage = File(path)
                    Method.load_Directory_Files(storage)
                }
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun checkStorageAccessPermission() {
        //ContextCompat use to retrieve resources. It provide uniform interface to access resources.
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(binding.root.context)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to access media file in your phone")
                    .setPositiveButton("OK") { dialog, which ->
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            1
                        )
                    }
                    .setNegativeButton(
                        "CANCEL"
                    ) { dialog, which -> dialog.dismiss() }.show()
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            // Do nothing. Because if permission is already granted then files will be accessed/loaded in splash_screen_activity
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == Activity.RESULT_OK) {
                fileUri = data!!.data
                activity?.contentResolver!!.takePersistableUriPermission(
                    fileUri!!, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }

    }

}