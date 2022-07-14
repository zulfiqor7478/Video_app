package uz.innavation.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import uz.innavation.R
import uz.innavation.databinding.FragmentSettingsBinding
import uz.innavation.utils.MySharedPreference

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.hdText.text = MySharedPreference.videoResolution

        binding.resolution.setOnClickListener {
            val dialog = AlertDialog.Builder(binding.root.context).create()
            val view = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.video_resolution_dialog, null, false)
            dialog.setView(view)


            val button = view.findViewById<RadioButton>(MySharedPreference.videoResolutionId!!)

           // button.isChecked = false

            val radioButton = view.findViewById<RadioGroup>(R.id.radio)


            dialog.setOnCancelListener {


                val checkedRadioButtonId = radioButton.checkedRadioButtonId


                val button = view.findViewById<RadioButton>(checkedRadioButtonId)

                if (MySharedPreference.videoResolution.toString() == button.text.toString()) {
                    button.isChecked = true
                }

                MySharedPreference.videoResolutionId = checkedRadioButtonId

                println("alovuddin: $checkedRadioButtonId")

                MySharedPreference.videoResolution = button.text.toString()
                binding.hdText.text = MySharedPreference.videoResolution


            }

            dialog.setContentView(view)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }

        binding.timeText.text =
            MySharedPreference.videoTime!!.toString() + " " + getString(R.string.soniya)
        binding.countText.text =
            MySharedPreference.automaticVideoCount!!.toString() + " " + getString(R.string.ta)

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
            val sliders = view.findViewById<Slider>(R.id.pb)
            sliders.value = MySharedPreference.videoTime!!.toFloat()

            view.findViewById<TextView>(R.id.pb_txt).text =
                MySharedPreference.videoTime!!.toString() + " " + getString(R.string.soniya)
            sliders.addOnChangeListener(Slider.OnChangeListener { slider, _, _ ->

                view.findViewById<TextView>(R.id.pb_txt).text =
                    slider.value.toInt().toString() + " " + getString(R.string.soniya)
                MySharedPreference.videoTime = slider.value.toInt()
                binding.timeText.text =
                    MySharedPreference.videoTime!!.toString() + " " + getString(R.string.soniya)
            })

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
            val sliders = view.findViewById<Slider>(R.id.pb)
            sliders.value = MySharedPreference.automaticVideoCount!!.toFloat()

            view.findViewById<TextView>(R.id.pb_txt).text =
                MySharedPreference.automaticVideoCount!!.toString() + " " + getString(R.string.ta)
            sliders.addOnChangeListener(Slider.OnChangeListener { slider, _, _ ->

                view.findViewById<TextView>(R.id.pb_txt).text =
                    slider.value.toInt().toString() + " " + getString(R.string.ta)
                MySharedPreference.automaticVideoCount = slider.value.toInt()
                binding.countText.text =
                    MySharedPreference.automaticVideoCount!!.toString() + " " + getString(R.string.ta)
            })


            dialog.setContentView(view)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }



        return binding.root
    }


}