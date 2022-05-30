package uz.innavation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentHomePageBinding

class HomePageFragment : Fragment() {
    lateinit var binding: FragmentHomePageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(layoutInflater)



        binding.infoProfile.setOnClickListener {
            findNavController().navigate(R.id.profile)
        }
        binding.aboutBtn.setOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }
        binding.galleryBtn.setOnClickListener {
            findNavController().navigate(R.id.galleryFragment)
        }
        binding.settingsBtn.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
        binding.openmapBtn.setOnClickListener {

        }





        return binding.root

    }
}