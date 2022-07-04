package uz.innavation.ui.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.innavation.R
import uz.innavation.databinding.FragmentFirstBinding
import uz.innavation.utils.MySharedPreference
import uz.innavation.utils.setAnimation
import java.util.*


class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(layoutInflater)

        binding.langUzb.setOnClickListener {
            navigate()
            setAppLocale(binding.root.context, "uz")
            MySharedPreference.language = "uz"
        }
        binding.langEn.setOnClickListener {
            navigate()
            setAppLocale(binding.root.context, "ru")
            MySharedPreference.language = "ru"

        }
        binding.langRu.setOnClickListener {
            navigate()
            setAppLocale(binding.root.context, "en")
            MySharedPreference.language = "en"
        }

        return binding.root
    }

    private fun navigate() {
        findNavController().navigate(R.id.signInFragment, Bundle(), setAnimation().build())
    }

    private fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }

}