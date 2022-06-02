package uz.innavation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.innavation.databinding.FragmentCustomDialog1Binding

class CustomDialogFragment1 : DialogFragment() {
    lateinit var binding: FragmentCustomDialog1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomDialog1Binding.inflate(layoutInflater)

        return binding.root
    }
}