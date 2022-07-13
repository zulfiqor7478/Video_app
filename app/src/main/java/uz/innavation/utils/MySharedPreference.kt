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

    var language: String?
        get() = sharedPreferences.getString("isUzbekLanguage", "uz")
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("isUzbekLanguage", value)
            }
        }

    var isLogin: Boolean?
        get() = sharedPreferences.getBoolean("isLogin", false)
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
        get() = sharedPreferences.getString("video21", "480p")
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("video21", value)
            }
        }

    var videoResolutionId: Int?
        get() = sharedPreferences.getInt("video21123", 2131231266)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putInt("video21123", value)
            }
        }

    var videoTime: Int?
        get() = sharedPreferences.getInt("paubile", 15)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putInt("paubile", value)
            }
        }
    var automaticVideoCount: Int?
        get() = sharedPreferences.getInt("asddsa", 10)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putInt("asddsa", value)
            }
        }

}