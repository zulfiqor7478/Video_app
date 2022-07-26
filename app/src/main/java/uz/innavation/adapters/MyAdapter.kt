package uz.innavation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.innavation.databinding.VideoItemBinding
import uz.innavation.models.Video
import java.io.File


class MyAdapter(
    private val movies: ArrayList<Video>, var context: Context,
    var onClick: OnClick
) :
    RecyclerView.Adapter<MyAdapter.ViewH>() {

    inner class ViewH(private var itemVideoBinding: VideoItemBinding) :
        RecyclerView.ViewHolder(itemVideoBinding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(video: Video) {

            Glide
                .with(context)
                .load(video.uri)
                .centerCrop()
                .into(itemVideoBinding.videoImg)


            val uri = Uri.parse(video.uri)

            itemView.setOnClickListener {
                onClick.click(uri, adapterPosition)

            }

            itemVideoBinding.videoName.text = getFileFromUri(context, uri)!!.name
        /*    val la = getFileFromUri(context, uri)
            val l = la!!.length() / 1000000
            var a = ""
            if (l < 10) {
                a = "0"
            }

            itemVideoBinding.videoSize.text = "$a$l mb"
*/
/*            try {
                var durationTime: Long
                MediaPlayer.create(itemVideoBinding.videoSize.context, uri).also {

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

                itemVideoBinding.videoDuration.text = "$m${minute}:$s$second"

            } catch (e: Exception) {
            }*/


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {

        return ViewH(VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return movies.size
    }


    override fun onBindViewHolder(holder: ViewH, position: Int) {
        holder.onBind(movies[position])
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

    interface OnClick {

        fun click(uri: Uri, position: Int)

    }

}
