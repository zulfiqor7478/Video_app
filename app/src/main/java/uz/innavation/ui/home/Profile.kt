package uz.innavation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentHomePageBinding
import uz.innavation.databinding.FragmentProfileBinding

class Profile : Fragment() {
    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)



        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.homePageFragment)
        }
binding.editBtn.setOnClickListener {
    findNavController().navigate(R.id.editFragment)
}




        return binding.root
    }

}