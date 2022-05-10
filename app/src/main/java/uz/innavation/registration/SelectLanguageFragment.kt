package uz.innavation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentSelectLanguageBinding
import uz.innavation.utils.MySharedPreference


class SelectLanguageFragment : Fragment() {
    lateinit var binding: FragmentSelectLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectLanguageBinding.inflate(layoutInflater)

        binding.kiril.setOnClickListener {
            MySharedPreference.isUzbekLanguage = false
            findNavController().navigate(R.id.selectRegisterTypeFragment)
        }
        binding.latin.setOnClickListener {
            MySharedPreference.isUzbekLanguage = true
            findNavController().navigate(R.id.selectRegisterTypeFragment)
        }

        return binding.root
    }

}