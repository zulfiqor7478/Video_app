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
import uz.innavation.utils.Constant
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
        val l = arrayList[position].length() / 1000000
        holder.videoSize.text = "$l mb"

        /*      try {
                  var durationTime: Long
                  MediaPlayer.create(holder.videoSize.context, uri).also {

                      durationTime = (it.duration / 1000).toLong()

                      it.reset()
                      it.release()
                  }



                  if (durationTime < 60){
                      holder.videoDuration.text = "00:$durationTime"
                  }else{
                      val l1 = durationTime.toFloat() / 60
                      holder.videoDuration.text = "${l1}:$durationTime"
                  }




              } catch (e: Exception) {
              }*/

        Glide.with(mContext)
            .load(uri).thumbnail(0.1f).into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n")
    internal inner class FileLayoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var videoTitle: TextView
        var videoDuration: TextView
        var videoSize: TextView

        init {
            thumbnail = itemView.findViewById(R.id.video_img)
            videoTitle = itemView.findViewById(R.id.video_name)
            videoDuration = itemView.findViewById(R.id.videoDuration)
            videoSize = itemView.findViewById(R.id.video_size)
            itemView.setOnClickListener {
                val uri = Uri.fromFile(arrayList[adapterPosition])
                onClick.click(uri)
            }


        }

    }

    interface OnClick {

        fun click(uri: Uri)

    }

}