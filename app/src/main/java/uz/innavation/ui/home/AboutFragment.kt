package uz.innavation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.innavation.R
import uz.innavation.databinding.FragmentAboutBinding
import uz.innavation.databinding.FragmentHomePageBinding

class AboutFragment : Fragment() {
    lateinit var binding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAboutBinding.inflate(layoutInflater)



        return binding.root
    }


}