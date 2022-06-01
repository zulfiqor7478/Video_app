package uz.innavation.ui.home

import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.innavation.databinding.FragmentSavedVideoListBinding
import java.io.File


class SavedVideoListFragment : Fragment() {
    lateinit var binding: FragmentSavedVideoListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedVideoListBinding.inflate(layoutInflater)




        return binding.root
    }

}