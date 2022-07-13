package uz.innavation.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
                navigate(R.id.profileFragment)
            }
            aboutBtn.setOnClickListener {
                navigate(R.id.aboutFragment2)
            }
            galleryBtn.setOnClickListener {
                navigate(R.id.galleryFragment2)
            }
            settingsBtn.setOnClickListener {
                navigate(R.id.settingsFragment2)
            }


            openmapBtn.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(activity?.packageManager!!) != null) {
                    startActivity(mapIntent)
                }
            }

        }




        return binding.root

    }

    private fun navigate(way: Int) {
        findNavController().navigate(way, Bundle(), setAnimation().build())
    }


}