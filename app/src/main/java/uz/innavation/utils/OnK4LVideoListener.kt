package uz.innavation.utils

interface OnK4LVideoListener {
    fun onTrimStarted()
    fun onError(message: String?)
    fun onVideoPrepared()
}