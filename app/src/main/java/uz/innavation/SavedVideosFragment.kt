package uz.innavation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.innavation.databinding.FragmentSavedVideosBinding


class SavedVideosFragment : Fragment() {

    lateinit var binding: FragmentSavedVideosBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedVideosBinding.inflate(layoutInflater)



        return binding.root
    }


}