package uz.innavation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.innavation.utils.MyTasks
import java.util.*

class MainActivityViewModel : ViewModel() {
    private val timer = Timer()
    private  var timerTask: TimerTask? = null
    private var time: Double = 0.0
    val timeForTextView = MutableLiveData<String>()

    fun startTimer() {
        timerTask = object : TimerTask() {
            override fun run() {
                time++
                CoroutineScope((Dispatchers.Main)).launch {
                    timeForTextView.value = MyTasks.getTimeText(time)
                }
            }

        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    fun stopTimer() {
        if (timerTask != null) {
            timerTask?.cancel()
            CoroutineScope((Dispatchers.Main)).launch {
                timeForTextView.value = MyTasks.getTimeText(0.0)
            }
        }
    }
}