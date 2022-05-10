package uz.innavation.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object MySharedPreference {

    lateinit var sharedPreferences: SharedPreferences

    fun getInstance(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            "" +
                    "", Context.MODE_PRIVATE
        )
    }

    var isUzbekLanguage: Boolean?
        get() = sharedPreferences.getBoolean("isUzbekLanguage", true)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putBoolean("isUzbekLanguage", value)
            }
        }

}