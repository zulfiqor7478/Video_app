package uz.innavation.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentSignInBinding
import uz.innavation.utils.setAnimation


class SignInFragment : Fragment() {

    lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)

        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment2, Bundle(),setAnimation().build())
        }

        return binding.root
    }




}