package uz.innavation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.databinding.FragmentVideosListBinding

class VideosListFragment : Fragment() {
    lateinit var binding: FragmentVideosListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideosListBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}