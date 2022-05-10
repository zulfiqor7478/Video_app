package uz.innavation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import uz.innavation.R
import uz.innavation.databinding.FragmentPhoneNumberRegisterBinding
import java.util.concurrent.TimeUnit

class PhoneNumberRegisterFragment : Fragment() {


    lateinit var auth:FirebaseAuth
    lateinit var binding: FragmentPhoneNumberRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneNumberRegisterBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("uz")

        binding.sendBtn.setOnClickListener {

/*            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(binding.number.text.toString())       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity())                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)*/


            var phoneNumber = binding.phoneInput.text.toString()
            phoneNumber = phoneNumber.replace("(", "", true)
            phoneNumber = phoneNumber.replace(")", "", true)
            phoneNumber = phoneNumber.replace(" ", "", true)
            phoneNumber = phoneNumber.replace("-", "", true)

            val bundle = Bundle()
            bundle.putString("number",phoneNumber)
            Toast.makeText(binding.root.context, phoneNumber, Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.verifyCodeFragment, bundle)

        }


        return binding.root
    }
/*

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
//            storedVerificationId = verificationId
//            resendToken = token
            Toast.makeText(binding.root.context, "Send", Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            bundle.putString("number",binding.number.text.toString())
            findNavController().navigate(R.id.verifyCodeFragment, bundle)

        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT).show()

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
*/

}