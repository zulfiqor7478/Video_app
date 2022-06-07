package uz.innavation.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentSettingsBinding
import uz.innavation.databinding.FragmentVideosListBinding

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.resolution.setOnClickListener {
            val dialog = AlertDialog.Builder(binding.root.context).create()
            val view = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.video_resolution_dialog, null, false)
            dialog.setView(view)

        /*    view.findViewById<LinearLayout>(R.id.play_btn).setOnClickListener {


                dialog.cancel()
            }*/
            view.findViewById<View>(R.id.cancel_btn).setOnClickListener {

                dialog.cancel()

            }

            dialog.setContentView(view)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }

        binding.time.setOnClickListener {
            val dialog = AlertDialog.Builder(binding.root.context).create()
            val view = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.time_dialog, null, false)
            dialog.setView(view)

        /*    view.findViewById<LinearLayout>(R.id.play_btn).setOnClickListener {


                dialog.cancel()
            }*/
            view.findViewById<View>(R.id.cancel_btn).setOnClickListener {

                dialog.cancel()

            }

            dialog.setContentView(view)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }



        return binding.root
    }

}