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
import uz.innavation.adapters.RecyclerViewAdapter
import uz.innavation.databinding.FragmentSavedVideoListBinding
import java.io.File


class SavedVideoListFragment : Fragment() {
    lateinit var binding: FragmentSavedVideoListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedVideoListBinding.inflate(layoutInflater)

        val arrayList = ArrayList<File>()
        val path = Environment.getExternalStorageDirectory().toString() + "/Movies/TwoMinutes/"
        Log.d("Files", "Path: $path")
        val directory = File(path)
        val files = directory.listFiles()
        if (directory.canRead() && files != null) {
            Log.d("Files", "Size: " + files.size)
            for (file in files) {
                Log.d("FILE", file.name)

                arrayList.add(file)

            }
        } else Log.d("Null?", "it is null")

        binding.rv.adapter =
            RecyclerViewAdapter(
                arrayList,
                binding.root.context,
                object : RecyclerViewAdapter.OnClick {
                    override fun click(uri: Uri) {


                        val dialog = AlertDialog.Builder(binding.root.context).create()
                        val view = LayoutInflater.from(binding.root.context)
                            .inflate(uz.innavation.R.layout.play_dialog, null, false)
                        dialog.setView(view)

                        view.findViewById<LinearLayout>(uz.innavation.R.id.play_btn).setOnClickListener {

                            val start =  Intent(Intent.ACTION_VIEW);
                            start.setDataAndType(Uri.parse(uri.path), "video/*");
                            startActivity(start);

                            dialog.cancel()
                        }
                        view.findViewById<View>(uz.innavation.R.id.cancel_btn).setOnClickListener {

                           dialog.cancel()

                        }

                        dialog.setContentView(view)
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog.setCancelable(false)
                        dialog.show()


                    }
                })


        return binding.root
    }


}