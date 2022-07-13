package uz.innavation.ui.home

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import uz.innavation.databinding.FragmentVideoTrimBinding
import uz.innavation.utils.OnK4LVideoListener


class VideoTrimFragment : Fragment(), OnK4LVideoListener {
    lateinit var binding: FragmentVideoTrimBinding
    private var mProgressDialog: ProgressDialog? = null
    var path: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoTrimBinding.inflate(layoutInflater)
        path = arguments?.getString("str")

        //setting progressbar
/*        mProgressDialog = ProgressDialog(binding.root.context)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.setMessage("message")
        mVideoTrimmer = binding.trimVideoViewTrimVideoAc
        if (mVideoTrimmer != null) {
            mVideoTrimmer!!.setMaxDuration(60)
            mVideoTrimmer!!.setOnTrimVideoListener(this)
            mVideoTrimmer!!.setOnTrimVideoListener(this)
            mVideoTrimmer!!.setDestinationPath("/storage/emulated/0/Cuckoo/")
            mVideoTrimmer!!.setVideoURI(Uri.parse(path))
        }*/

        return binding.root
    }

    override fun onTrimStarted() {
        mProgressDialog!!.show()
    }
/*

    override fun getResult(uri: Uri) {
        mProgressDialog!!.cancel()
        activity?.runOnUiThread {
            Toast.makeText(binding.root.context, uri.toString(), Toast.LENGTH_SHORT).show()
        }
        */
/* Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    intent.setDataAndType(uri, "video/mp4");
    startActivity(intent);
    finish();*//*

        */
/*val intent = Intent(binding.root.context, FinalVideoActivity::class.java)
        intent.putExtra(MainActivity.VideoUri, uri.toString())
        startActivity(intent)*//*

    }
*/


    override fun onError(message: String?) {
        mProgressDialog!!.cancel()
        activity?.runOnUiThread {
            Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onVideoPrepared() {
        activity?.runOnUiThread {
            Toast.makeText(binding.root.context, "onVideoPrepared", Toast.LENGTH_SHORT).show()
        }
    }


}