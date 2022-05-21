package uz.innavation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.databinding.FragmentFirstBinding
import uz.innavation.utils.setAnimation


class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(layoutInflater)

        binding.langUzb.setOnClickListener { navigate() }
        binding.langEn.setOnClickListener { navigate() }
        binding.langRu.setOnClickListener { navigate() }


        return binding.root
    }

    private fun navigate() {
        findNavController().navigate(R.id.signInFragment, Bundle(), setAnimation().build())
    }

}