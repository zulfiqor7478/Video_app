package uz.innavation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.innavation.R
import uz.innavation.databinding.FragmentCustomDialog2Binding

class CustomDialogFragment2 : DialogFragment() {
    lateinit var binding: FragmentCustomDialog2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomDialog2Binding.inflate(layoutInflater)

        return binding.root
    }
}