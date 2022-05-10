package uz.innavation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import uz.innavation.databinding.FragmentSignUpBinding
import uz.innavation.models.User
import uz.innavation.utils.MyData

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    lateinit var shahar: String
    lateinit var viloyat: String
    var position = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)

        val number = arguments?.getString("number2")

        val database = Firebase.database
        val myRef = database.getReference("users/$number")

        binding.apply {

            btn.setOnClickListener {

                val user = User(
                    name.text.toString(),
                    lastName.text.toString(),
                    fatherName.text.toString(),
                    number,
                    viloyat,
                    shahar,
                    location.text.toString(),
                    zipCode.text.toString()
                )

                myRef.setValue(user)


                Toast.makeText(
                    binding.root.context,
                    "Muvaffaqiyatli ro'yxatdan o'tdingiz!",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

        val spinnerAdapter = ArrayAdapter(
            binding.root.context,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
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

        return binding.root
    }

    fun setSpinnerAdapter(list: List<String>) {

        val adapter = ArrayAdapter(
            binding.root.context,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
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

}