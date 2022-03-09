package uz.innavation.utils

import kotlin.math.round
import kotlin.math.roundToInt

class MyTasks {
    companion object{
        fun getTimeText(time: Double): String{
            var rounded = time.roundToInt()
            var seconds: Int = ((rounded % 86400) % 3600) % 60
            var minutes: Int = ((rounded) % 86400) / 60
            var hours: Int = ((rounded %86400)/3600)

            return formatTime(seconds, minutes, hours)
        }

        private fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
            return String.format("%02d", minutes) + " : " + String.format("%02d", seconds)
        }

    }
}