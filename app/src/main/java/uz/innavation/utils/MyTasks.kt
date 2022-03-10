package uz.innavation.utils

import kotlin.math.round
import kotlin.math.roundToInt

class MyTasks {
    companion object{
        fun getTimeText(time: Double): String{
            val rounded = time.roundToInt()
            val seconds: Int = ((rounded % 86400) % 3600) % 60
            val minutes: Int = ((rounded) % 86400) / 60
            val hours: Int = ((rounded %86400)/3600)

            return formatTime(seconds, minutes, hours)
        }

        private fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
            return String.format("%02d", minutes) + " : " + String.format("%02d", seconds)
        }

    }
}