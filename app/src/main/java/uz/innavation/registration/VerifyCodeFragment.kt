package uz.innavation.registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import uz.innavation.R
import uz.innavation.databinding.FragmentVerifyCodeBinding
import java.util.concurrent.TimeUnit

class VerifyCodeFragment : Fragment() {

    private lateinit var timer: CountDownTimer
    lateinit var binding: FragmentVerifyCodeBinding
    lateinit var auth:FirebaseAuth
    lateinit var storedVerificationId: String
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    lateinit var number: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyCodeBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()

        number = arguments?.getString("number")!!

        setTime()
        sentVerificationCode(number)
/*

        binding.password.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                verifyCode()
                val view = activity?.currentFocus
                if (view != null) {
                    val imm: InputMethodManager =
                        activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            true
        }
*/

        binding.signIn.setOnClickListener {
            verifyCode()
        }

        binding.reSend.setOnClickListener {

            binding.reSend.setTextColor(ContextCompat.getColor(binding.root.context,
                R.color.teal_200
            ))
            binding.reSend.isClickable = false
            timer.start()
            binding.password.isEnabled = true

            resendCode(number)
        }

        return binding.root
    }


    private fun setTime() {

        timer = object : CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished < 10000) {

                    binding.password.isEnabled = true
                    binding.time.text = "00:0${millisUntilFinished / 1000}"
                } else {

                    binding.time.text = "00:${millisUntilFinished / 1000}"
                }

            }

            override fun onFinish() {
                try {
                    binding.reSend.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.red
                        )
                    )
                    binding.reSend.isClickable = true
                    vibratsiya()
                    binding.password.setBackgroundResource(R.color.teal_200)
                    binding.password.visibility
                    binding.password.setText("")
                    binding.password.isEnabled = false

                } catch (e: Exception) {
                }


            }
        }
        timer.start()

    }


    private fun vibratsiya() {
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, 10))
        }
    }

    private fun verifyCode() {
        val code = binding.password.text.toString().replace(" ", "", true)
        if (code.length == 6) {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(credential)
        }
    }

    fun sentVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendCode(phoneNumber: String) {
        if (resendToken != null) {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity())                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .setForceResendingToken(resendToken!!)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }


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
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information

                val user = task.result?.user?.phoneNumber

                val bundle = Bundle()
                bundle.putString("number2", number)
                findNavController().navigate(R.id.signUpFragment, bundle)
                Toast.makeText(binding.root.context, "Urra :)", Toast.LENGTH_SHORT).show()

            } else {
                // Sign in failed, display a message and update the UI

                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Snackbar.make(binding.root, "Kiritilgan kod noto'g'ri!", Snackbar.LENGTH_LONG)
                        .show()
                }
                // Update UI
            }
        }
    }


}