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

    var isLogin: Boolean?
        get() = sharedPreferences.getBoolean("isLogin", true)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putBoolean("isLogin", value)
            }
        }

    var region: String?
        get() = sharedPreferences.getString("region", "")
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("region", value)
            }
        }

    var country: String?
        get() = sharedPreferences.getString("country", "")
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("country", value)
            }
        }

    var streetNumber: String?
        get() = sharedPreferences.getString("streetNumber", "")
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("streetNumber", value)
            }
        }

    var videoResolution: String?
        get() = sharedPreferences.getString("video21", "")
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("video21", value)
            }
        }

}