package uz.innavation

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.innavation.databinding.FragmentVideoPlayerBinding


class VideoPlayerFragment : Fragment() {

    lateinit var binding:FragmentVideoPlayerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoPlayerBinding.inflate(layoutInflater)

        arguments
//        binding.videoView.setVideoURI()

        return binding.root
    }

}