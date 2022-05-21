package uz.innavation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import uz.innavation.databinding.FragmentSignUp2Binding
import uz.innavation.databinding.FragmentSignUpBinding
import uz.innavation.models.User
import uz.innavation.utils.MySharedPreference
import uz.innavation.utils.setAnimation


class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUp2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUp2Binding.inflate(layoutInflater)


        binding.signInBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.locationCard.setOnClickListener {
            findNavController().navigate(
                R.id.selectLocationFragment,
                Bundle(),
                setAnimation().build()
            )
        }

        return binding.root
    }

    override fun onResume() {
        binding.apply {

            btnCard.setOnClickListener {

                if (check()) {

                    val bundle = Bundle()
                    bundle.putString("name",name.text.toString())
                    bundle.putString("lastName",lastName.text.toString())
                    bundle.putString("email",email.text.toString())
                    bundle.putString("password",password.text.toString())

                    findNavController().navigate(R.id.verifyCodeFragment, bundle)

                }


                /*          var phoneNumber = binding.phone.text.toString()
                          phoneNumber = phoneNumber.replace("(", "", true)
                          phoneNumber = phoneNumber.replace(")", "", true)
                          phoneNumber = phoneNumber.replace(" ", "", true)
                          phoneNumber = phoneNumber.replace("-", "", true)*/

                /*
  */


            }


        }

        super.onResume()
    }

    private fun check(): Boolean {
        var a = false
        if (binding.name.text.isNotBlank()) {
            if (binding.lastName.text.isNotBlank()) {
                if (binding.email.text.isNotBlank()) {
                    if (binding.password.length() < 8) {
                        if (MySharedPreference.region!!.isNotBlank() && MySharedPreference.country!!.isNotBlank() && MySharedPreference.streetNumber!!.isNotBlank()) {

                            a = true

                        } else
                            Toast.makeText(
                                binding.root.context,
                                "Iltimos, turar joyingizni tanlang!",
                                Toast.LENGTH_SHORT
                            ).show()
                    } else
                        Toast.makeText(
                            binding.root.context,
                            "Iltimos, kamida 8 tadan iborat harf yoki raqam kiriting!",
                            Toast.LENGTH_LONG
                        ).show()
                } else
                    Toast.makeText(
                        binding.root.context,
                        "Iltimos, email'ingizni kiriting!",
                        Toast.LENGTH_SHORT
                    ).show()
            } else
                Toast.makeText(
                    binding.root.context,
                    "Iltimos, familiyangizni kiriting!",
                    Toast.LENGTH_SHORT
                ).show()
        } else
            Toast.makeText(
                binding.root.context,
                "Iltimos, ismingizni kiriting!",
                Toast.LENGTH_SHORT
            ).show()

        return a
    }


}