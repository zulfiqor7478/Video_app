package uz.innavation.utils

class MyTasks {
    companion object {
        fun getTimeText(time: Long): String {
            val seconds: Long = ((time % 86400) % 3600) % 60
            val minutes: Long = ((time) % 86400) / 60
            val hours: Long = ((time % 86400) / 3600)

            return formatTime(seconds, minutes, hours)
        }

        private fun formatTime(seconds: Long, minutes: Long, hours: Long): String {
            return String.format(
                "%02d",
                minutes
            ) + ":" + String.format("%02d", seconds)
        }

    }
}