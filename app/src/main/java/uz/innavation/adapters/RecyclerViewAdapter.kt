package uz.innavation.adapters

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import uz.innavation.R
import uz.innavation.adapters.RecyclerViewAdapter.FileLayoutHolder
import com.bumptech.glide.Glide
import android.widget.TextView
import uz.innavation.utils.Constant

class RecyclerViewAdapter internal constructor(private val mContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return FileLayoutHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FileLayoutHolder).videoTitle.text = Constant.allMediaList[position].name
        //we will load thumbnail using glid library
        val uri = Uri.fromFile(Constant.allMediaList[position])
        Glide.with(mContext)
            .load(uri).thumbnail(0.1f).into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return Constant.allMediaList.size
    }

    internal inner class FileLayoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var videoTitle: TextView
        var ic_more_btn: ImageView? = null

        init {
            thumbnail = itemView.findViewById(R.id.video_img)
            videoTitle = itemView.findViewById(R.id.video_name)
        }
    }
}