package uz.innavation.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gowtham.library.utils.TrimType
import com.gowtham.library.utils.TrimVideo
import uz.innavation.adapters.RecyclerViewAdapter
import uz.innavation.databinding.FragmentVideosListBinding
import java.io.File


class VideosListFragment : Fragment() {

    lateinit var binding: FragmentVideosListBinding
    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideosListBinding.inflate(layoutInflater)


        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        val arrayList = ArrayList<File>()
        val path = Environment.getExternalStorageDirectory().toString() + "/Movies/YxxVideos/"
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

        val aaa = object : RecyclerViewAdapter.OnClick {
            override fun click(uri: Uri, position: Int) {


                val dialog = AlertDialog.Builder(binding.root.context).create()
                val view = LayoutInflater.from(binding.root.context)
                    .inflate(uz.innavation.R.layout.play_dialog2, null, false)
                dialog.setView(view)

                view.findViewById<LinearLayout>(uz.innavation.R.id.play_btn)
                    .setOnClickListener {

                        val start = Intent(Intent.ACTION_VIEW);
                        start.setDataAndType(Uri.parse(uri.path), "video/*");
                        startActivity(start);

                        dialog.cancel()
                    }

                view.findViewById<View>(uz.innavation.R.id.cancel_btn).setOnClickListener {
                    dialog.cancel()

                }

                view.findViewById<View>(uz.innavation.R.id.delete_btn).setOnClickListener {
                    files!![position].delete()
                    arrayList.removeAt(position)
                    recyclerViewAdapter.notifyItemRemoved(position)
                    dialog.cancel()

                }

                view.findViewById<View>(uz.innavation.R.id.cut_btn).setOnClickListener {


                    TrimVideo.activity(uri.toString())
                        .setDestination("/storage/emulated/0/Movies/TrimVideos")
                        .start(this@VideosListFragment)


                    dialog.cancel()


                }

                dialog.setContentView(view)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()


            }


        }

        recyclerViewAdapter = RecyclerViewAdapter(
            arrayList,
            binding.root.context, aaa
        )

        binding.rv.adapter = recyclerViewAdapter

        return binding.root
    }


}