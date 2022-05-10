package uz.innavation.registration

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentSelectRegisterTypeBinding
import uz.innavation.utils.MySharedPreference


class SelectRegisterTypeFragment : Fragment() {

    private lateinit var binding: FragmentSelectRegisterTypeBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectRegisterTypeBinding.inflate(layoutInflater)

        if (MySharedPreference.isUzbekLanguage!!) {
            binding.welcome.text = "Hush kelibsiz!"
            binding.tv2.text = "Dasturga kirish turini tanlang"
            binding.phoneTv.text = "Telefon raqami orqali"
        } else {
            binding.welcome.text = "Ҳуш келибсиз!"
            binding.tv2.text = "Дастурга кириш турини танланг"
            binding.phoneTv.text = "Телефон рақами орқали"
        }

        binding.phone.setOnClickListener {
            findNavController().navigate(R.id.phoneNumberRegisterFragment)
        }

        return binding.root
    }

}