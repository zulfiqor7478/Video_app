package uz.innavation.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gowtham.library.utils.TrimVideo
import uz.innavation.R
import uz.innavation.adapters.MyAdapter
import uz.innavation.adapters.RecyclerViewAdapter
import uz.innavation.databinding.FragmentTrimVideosBinding
import uz.innavation.models.Video
import uz.innavation.room.AppDatabase
import java.io.File

class TrimVideosFragment : Fragment() {

    lateinit var binding: FragmentTrimVideosBinding
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTrimVideosBinding.inflate(layoutInflater)


        val arrayList = ArrayList<File>()
        val path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Trim/"
        Log.d("Files", "Path: $path")
        val directory = File(path)
        val files = directory.listFiles()
        if (directory.canRead() && files != null) {
            Log.d("Files", "Size: " + files.size)
            for (file in files) {
                Log.d("FILE", file.name)
                //if (files.size < 10){
                arrayList.add(file)
                //}else{
                //   files[files.size].delete()
                // }


            }
        } else Log.d("Null?", "it is null")


        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }



        recyclerViewAdapter = RecyclerViewAdapter(
            arrayList,
            binding.root.context,
            object : RecyclerViewAdapter.OnClick {
                override fun click(uri: Uri, position: Int) {


                    val dialog = AlertDialog.Builder(binding.root.context).create()
                    val view = LayoutInflater.from(binding.root.context)
                        .inflate(R.layout.play_dialog2, null, false)
                    dialog.setView(view)

                    view.findViewById<LinearLayout>(R.id.play_btn)
                        .setOnClickListener {

                            val start = Intent(Intent.ACTION_VIEW);
                            start.setDataAndType(Uri.parse(uri.toString()), "video/*");
                            startActivity(start);

                            dialog.cancel()
                        }
                    view.findViewById<View>(R.id.cancel_btn).setOnClickListener {

                        dialog.cancel()

                    }
                    view.findViewById<View>(R.id.cut_btn).setOnClickListener {


                        /*         val bundle = Bundle()
                                 bundle.putString("str", uri.path)
                                 findNavController().navigate(R.id.videoTrimFragment, bundle)
                                 dialog.cancel()*/


                        TrimVideo.activity(uri.toString())
                            .setDestination("/storage/emulated/0/DCIM/Trim")
                            .start(this@TrimVideosFragment)




                        dialog.cancel()


                    }

                    view.findViewById<View>(R.id.delete_btn).setOnClickListener {
                        arrayList[position].delete()
                        arrayList.removeAt(position)
                        recyclerViewAdapter.notifyItemRemoved(position)
                        dialog.cancel()

                    }

                    dialog.setContentView(view)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.show()


                }
            })


        //val all = AppDatabase.getInstants(binding.root.context).dao().getAllVideo()

        binding.rv.adapter = recyclerViewAdapter


        return binding.root
    }


    fun getFileFromUri(context: Context, uri: Uri?): File? {
        uri ?: return null
        uri.path ?: return null

        var newUriString = uri.toString()
        newUriString = newUriString.replace(
            "content://com.android.providers.downloads.documents/",
            "content://com.android.providers.media.documents/"
        )
        newUriString = newUriString.replace(
            "/msf%3A", "/image%3A"
        )
        val newUri = Uri.parse(newUriString)

        var realPath = String()
        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (newUri.path?.contains("/document/image:") == true) {
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(newUri).split(":")[1])
        } else {
            databaseUri = newUri
            selection = null
            selectionArgs = null
        }
        try {
            val column = "_data"
            val projection = arrayOf(column)
            val cursor = context.contentResolver.query(
                databaseUri,
                projection,
                selection,
                selectionArgs,
                null
            )
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(column)
                    realPath = cursor.getString(columnIndex)
                }
                cursor.close()
            }
        } catch (e: Exception) {
            Log.i("GetFileUri Exception:", e.message ?: "")
        }
        val path = realPath.ifEmpty {
            when {
                newUri.path?.contains("/document/raw:") == true -> newUri.path?.replace(
                    "/document/raw:",
                    ""
                )
                newUri.path?.contains("/document/primary:") == true -> newUri.path?.replace(
                    "/document/primary:",
                    "/storage/emulated/0/"
                )
                else -> return null
            }
        }
        return if (path.isNullOrEmpty()) null else File(path)
    }


}