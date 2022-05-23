package uz.innavation.ui.registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import uz.innavation.R
import uz.innavation.databinding.FragmentVerifyCodeBinding
import uz.innavation.models.User
import uz.innavation.ui.mainActivity.MainActivity
import uz.innavation.utils.MySharedPreference
import java.util.concurrent.TimeUnit

class VerifyCodeFragment : Fragment() {

    private lateinit var timer: CountDownTimer
    lateinit var binding: FragmentVerifyCodeBinding
    lateinit var auth: FirebaseAuth
    var isCodeSend = false
    lateinit var storedVerificationId: String
     lateinit var dialog: AlertDialog
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    lateinit var number: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyCodeBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()



        binding.phone.addTextChangedListener {

            if (it!!.toString().length == 19) {
                binding.code.visibility = View.VISIBLE
                binding.codeCard.visibility = View.VISIBLE

                var phoneNumber = binding.phone.text.toString()
                phoneNumber = phoneNumber.replace("(", "", true)
                phoneNumber = phoneNumber.replace(")", "", true)
                phoneNumber = phoneNumber.replace(" ", "", true)
                phoneNumber = phoneNumber.replace("-", "", true)
                number = phoneNumber

                sentVerificationCode(phoneNumber)
                setTime()
                closeKerBoard()
            }

        }


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

        binding.btnCard.setOnClickListener {
            if (binding.password.text.toString().isNotBlank()) {
                verifyCode()
            }
        }

        binding.password.addTextChangedListener {
            if (it.toString().length >= 6) {
                verifyCode()
            }
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
                    if (isCodeSend) {
                        resendCode(number)
                        vibratsiya()
                        binding.password.setText("")
                        timer.start()
                        Toast.makeText(
                            binding.root.context,
                            "Kod qayta yuborildi",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Agarda tasdiqlash kodi yetib bormagan bo'lsa, keyinroq urinib ko'ring!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    isCodeSend = true

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

    private fun sentVerificationCode(phoneNumber: String) {
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

                setProgress()

                val bundle = Bundle()
                bundle.putString("number2", number)


                val name = arguments?.getString("name")
                val lastName = arguments?.getString("lastName")
                val email = arguments?.getString("email")
                val password = arguments?.getString("password")

                val user = User(
                    name,
                    lastName,
                    number,
                    email, password,
                    MySharedPreference.region,
                    MySharedPreference.country,
                    MySharedPreference.streetNumber
                )


                val database = Firebase.database
                val myRef = database.getReference("users/$number")
                myRef.setValue(user)

                Handler(Looper.myLooper()!!).postDelayed({

                    dialog.cancel()
                    startActivity(Intent(binding.root.context, MainActivity::class.java))
                    timer.cancel()
                    activity?.finish()

                }, 1000)

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

    private fun closeKerBoard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }


    private fun setProgress() {
        dialog = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.custom_progress, null, false)
        dialog.setView(view)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.show()
    }


}