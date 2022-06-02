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

class RecyclerViewAdapter internal constructor(
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
        (holder as FileLayoutHolder).videoTitle.text = Constant.allMediaList[position].name
        //we will load thumbnail using glide library
        val uri = Uri.fromFile(Constant.allMediaList[position])
        val l = Constant.allMediaList[position].length() / 1000000
        holder.videoSize.text = "$l mb"

        Glide.with(mContext)
            .load(uri).thumbnail(0.1f).into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return Constant.allMediaList.size
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
            thumbnail.setOnClickListener {
                val uri = Uri.fromFile(Constant.allMediaList[adapterPosition])
                onClick.click(uri)
            }




        }

    }

    interface OnClick {

        fun click(uri: Uri)

    }

}