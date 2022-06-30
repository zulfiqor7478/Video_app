package uz.innavation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentProfileBinding
import uz.innavation.utils.setAnimation

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener { findNavController().popBackStack() }

        binding.editBtn.setOnClickListener {
            findNavController().navigate(
                R.id.editFragment2, Bundle(), setAnimation().build()
            )
        }
        return binding.root
    }
}