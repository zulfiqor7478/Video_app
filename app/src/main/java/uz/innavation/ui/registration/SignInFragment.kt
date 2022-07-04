package uz.innavation.ui.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentSignInBinding
import uz.innavation.ui.mainActivity.MainActivity
import uz.innavation.utils.setAnimation


class SignInFragment : Fragment() {

    lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)

        binding.signUpBtn.isSelected = true

        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment, Bundle(), setAnimation().build())
        }
        binding.btnCard.setOnClickListener {
            startActivity(Intent(binding.root.context, MainActivity::class.java))
        }
        return binding.root
    }


}