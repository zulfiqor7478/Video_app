package uz.innavation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.innavation.R
import java.io.File

class RecyclerViewAdapter internal constructor(
    var arrayList: ArrayList<File>,
    private val mContext: Context,
    var onClick: OnClick
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return FileLayoutHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FileLayoutHolder).videoTitle.text = arrayList[position].name
        //we will load thumbnail using glide library
        val uri = Uri.fromFile(arrayList[position])
/*        val l = arrayList[position].length() / 1000000
        var a = ""
        if (l < 10) {
            a = "0"
        }

        holder.videoSize.text = "$a$l mb"

        try {
            var durationTime: Long
            MediaPlayer.create(holder.videoSize.context, uri).also {

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

            holder.videoDuration.text = "$m${minute}:$s$second"

        } catch (e: Exception) {
        }*/


//        Picasso.get().load(uri).resize(200,200).into(holder.thumbnail)
        Glide
            .with(mContext)
            .load(uri)
            .centerCrop()
            .into(holder.thumbnail);

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n")
    internal inner class FileLayoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var videoTitle: TextView

        //var videoDuration: TextView
       // var videoSize: TextView

        init {
            thumbnail = itemView.findViewById(R.id.video_img)
            videoTitle = itemView.findViewById(R.id.video_name)
            //   videoDuration = itemView.findViewById(R.id.videoDuration)
            //   videoSize = itemView.findViewById(R.id.video_size)
            itemView.setOnClickListener {
                val uri = Uri.fromFile(arrayList[adapterPosition])
                onClick.click(uri, adapterPosition)
            }


        }

    }

    interface OnClick {

        fun click(uri: Uri, position: Int)

    }

}