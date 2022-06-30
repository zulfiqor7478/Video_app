package uz.innavation.utils

import androidx.navigation.NavOptions
import uz.innavation.R

fun setAnimation(): NavOptions.Builder {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.enter_anim)
        .setExitAnim(R.anim.exit_anim)
        .setPopEnterAnim(R.anim.pop_enter_anim)
        .setPopExitAnim(R.anim.pop_exit_anim)
}
