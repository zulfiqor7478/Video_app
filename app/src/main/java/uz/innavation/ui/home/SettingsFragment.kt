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

    @SuppressLint("SetTextI18n", "CutPasteId")
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


            /*      val button12 = view.findViewById<RadioButton>(MySharedPreference.videoResolutionId!!)

                  button12.isChecked = true*/

            val radioButton = view.findViewById<RadioGroup>(R.id.radio)

//            val button = view.findViewById<RadioButton>(R.id.radio)
            val button1 = view.findViewById<RadioButton>(R.id.radio2)
            val button2 = view.findViewById<RadioButton>(R.id.radio3)
            val button3 = view.findViewById<RadioButton>(R.id.radio4)
            val button4 = view.findViewById<RadioButton>(R.id.radio5)

            when (MySharedPreference.videoResolutionId) {
                1 -> {
                    button1.isChecked = true
                }
                2 -> button2.isChecked = true
                3 -> button3.isChecked = true
                4 -> button4.isChecked = true
            }

            button1.setOnCheckedChangeListener { _, p1 ->

                if (p1) {
                    MySharedPreference.videoResolutionId = 1
                    MySharedPreference.videoResolution = button1.text.toString()
                    binding.hdText.text = MySharedPreference.videoResolution
                }

            }
            button2.setOnCheckedChangeListener { _, p1 ->

                if (p1) {
                    MySharedPreference.videoResolutionId = 2
                    MySharedPreference.videoResolution = button2.text.toString()
                    binding.hdText.text = MySharedPreference.videoResolution
                }

            }
            button3.setOnCheckedChangeListener { _, p1 ->

                if (p1) {
                    MySharedPreference.videoResolutionId = 3
                    MySharedPreference.videoResolution = button3.text.toString()
                    binding.hdText.text = MySharedPreference.videoResolution
                }

            }
            button4.setOnCheckedChangeListener { _, p1 ->

                if (p1) {
                    MySharedPreference.videoResolutionId = 4
                    MySharedPreference.videoResolution = button4.text.toString()
                    binding.hdText.text = MySharedPreference.videoResolution
                }

            }


            dialog.setOnCancelListener {


                val checkedRadioButtonId = radioButton.checkedRadioButtonId


                //


                println("alovuddin: $checkedRadioButtonId")


            }
            view.findViewById<View>(R.id.cancel_btn).setOnClickListener {

                dialog.cancel()

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