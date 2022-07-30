package uz.innavation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.innavation.databinding.VideoItemBinding
import uz.innavation.models.Video


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
                onClick.click(uri, adapterPosition, video)

            }

//            itemVideoBinding.videoName.text = getFileFromUri(context, uri)!!.name
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


    interface OnClick {

        fun click(uri: Uri, position: Int, video: Video)

    }

}
