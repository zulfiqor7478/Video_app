package uz.innavation.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.R
import androidx.navigation.fragment.findNavController
import uz.innavation.databinding.FragmentSelectLocationBinding
import uz.innavation.utils.MyData
import uz.innavation.utils.MySharedPreference


class SelectLocationFragment : Fragment() {
    lateinit var binding: FragmentSelectLocationBinding
    lateinit var shahar: String
    lateinit var viloyat: String
    var position = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectLocationBinding.inflate(layoutInflater)

        binding.view.layoutParams.height = getStatusBarHeight()




        val spinnerAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.support_simple_spinner_dropdown_item,
            MyData.countryList()
        )

        binding.spinnerR.adapter = spinnerAdapter
        binding.spinnerR.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position2: Int,
                id: Long
            ) {
                position = position2
                viloyat = MyData.countryList()[position2]

                when (position2) {
                    0 -> setSpinnerAdapter(MyData.tashcityList())
                    1 -> setSpinnerAdapter(MyData.tashkentList())
                    2 -> setSpinnerAdapter(MyData.andijanList())
                    3 -> setSpinnerAdapter(MyData.ferganaList())
                    4 -> setSpinnerAdapter(MyData.namanganList())
                    5 -> setSpinnerAdapter(MyData.gulistanList())
                    6 -> setSpinnerAdapter(MyData.jizzakhList())
                    7 -> setSpinnerAdapter(MyData.samarkandList())
                    8 -> setSpinnerAdapter(MyData.kawkadaryaList())
                    9 -> setSpinnerAdapter(MyData.surxandaryaList())
                    10 -> setSpinnerAdapter(MyData.navaiList())
                    11 -> setSpinnerAdapter(MyData.bukharaList())
                    12 -> setSpinnerAdapter(MyData.xorazmList())
                    13 -> setSpinnerAdapter(MyData.karakalpkList())
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


        binding.save.setOnClickListener {
            save()
        }



        return binding.root
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun setSpinnerAdapter(list: List<String>) {

        val adapter = ArrayAdapter(
            binding.root.context,
            R.layout.support_simple_spinner_dropdown_item,
            list
        )

        binding.spinnerC.adapter = adapter

        binding.spinnerC.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                shahar = list[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }

    private fun save() {
        if (binding.street.text.isNotBlank()) {
            MySharedPreference.region = viloyat
            MySharedPreference.country = shahar
            MySharedPreference.streetNumber = binding.street.text.toString()
            findNavController().popBackStack()
        } else {
            Toast.makeText(
                binding.root.context,
                "Iltimos, ko'changizni raqamini kiriting!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}