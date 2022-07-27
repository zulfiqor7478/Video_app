package uz.innavation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.innavation.databinding.FragmentInfoVideoBinding
import uz.innavation.models.Video
import java.io.File


class InfoVideoFragment : Fragment() {
    lateinit var binding: FragmentInfoVideoBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentInfoVideoBinding.inflate(layoutInflater)

        val video = arguments?.getSerializable("video") as Video
        binding.latInfo.text = "Latitude: ${video.lat}"
        binding.lanInfo.text = "Longitude: ${video.longitude}"

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        val uri =
            Uri.parse(video.uri)
        try {
            val la = getFileFromUri(binding.root.context, uri)
            val l = la!!.length() / 1000000
            var a = ""
            if (l < 10) {
                a = "0"
            }

            binding.storageInfoTxt.text = "$a$l mb"
        } catch (e: Exception) {
        }


        try {
            var durationTime: Long
            MediaPlayer.create(binding.root.context, uri).also {

                durationTime = (it.duration / 1000).toLong()

                it.reset()
                it.release()
            }


            val minute = durationTime / 60
            val second = durationTime % 60
            var m = ""
            if (minute < 10) {
                m = "0"
            }
            var s = ""
            if (second < 10) {
                s = "0"
            }

            binding.timeInfoTxt.text = "$m${minute}:$s$second"

        } catch (e: Exception) {
        }

        binding.timeVideoInfoTxt.text = video.time
        binding.dateVideoInfoTxt.text = video.date

        binding.openMap.setOnClickListener {

            val uri: Uri =
                Uri.parse("https://maps.google.com/?q=${video.lat},${video.longitude}") // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


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