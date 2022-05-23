package uz.innavation.app

import android.app.Application
import uz.innavation.utils.MySharedPreference

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MySharedPreference.getInstance(applicationContext)

    }

}