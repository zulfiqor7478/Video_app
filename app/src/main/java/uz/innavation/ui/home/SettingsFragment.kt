package uz.innavation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentSettingsBinding
import uz.innavation.utils.MySharedPreference

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

            val radioButton = view.findViewById<RadioGroup>(R.id.radio)


            dialog.setOnCancelListener {


                val checkedRadioButtonId = radioButton.checkedRadioButtonId

                val button = view.findViewById<RadioButton>(checkedRadioButtonId)
                Toast.makeText(binding.root.context, button.text, Toast.LENGTH_SHORT).show()
                MySharedPreference.videoResolution = button.text.toString()

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

        binding.number.setOnClickListener {
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