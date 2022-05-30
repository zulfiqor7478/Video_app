package uz.innavation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentAboutBinding
import uz.innavation.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    lateinit var binding: FragmentGalleryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(layoutInflater)

        binding.videosBtn.setOnClickListener {
            findNavController().navigate(R.id.videosListFragment)
        }
        binding.savedVideosBtn.setOnClickListener {
            findNavController().navigate(R.id.savedVideoListFragment)
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}