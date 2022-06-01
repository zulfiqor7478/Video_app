package uz.innavation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.innavation.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    lateinit var binding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAboutBinding.inflate(layoutInflater)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.dlg1.setOnClickListener {
            val dlg1 = CustomDialogFragment1()
            dlg1.show(childFragmentManager, "customDiolog")
        }
        binding.dlg2.setOnClickListener {
            val dlg2 = CustomDialogFragment2()
            dlg2.show(childFragmentManager, "customDiolog")
        }
        return binding.root
    }


}