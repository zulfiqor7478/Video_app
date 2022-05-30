package uz.innavation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentHomePageBinding
import uz.innavation.utils.setAnimation

class HomePageFragment : Fragment() {
    lateinit var binding: FragmentHomePageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(layoutInflater)



        binding.apply {
            startBtn.setOnClickListener {
                navigate(R.id.videoFragment)
            }
            infoProfile.setOnClickListener {
                navigate(R.id.profile)
            }
            aboutBtn.setOnClickListener {
                navigate(R.id.aboutFragment2)
            }
            galleryBtn.setOnClickListener {
                navigate(R.id.galleryFragment)
            }
            settingsBtn.setOnClickListener {
                navigate(R.id.settingsFragment2)
            }


            openmapBtn.setOnClickListener {

            }
        }




        return binding.root

    }

    private fun navigate(way: Int) {
        findNavController().navigate(way, Bundle(), setAnimation().build())
    }

}