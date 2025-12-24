package kz.madik.rhythm_musichub.ui.components.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kz.madik.rhythm_musichub.R
import java.util.Calendar

@Composable
fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 6..11 -> stringResource(R.string.home_greeting_morning)
        in 12..17 -> stringResource(R.string.home_greeting_afternoon)
        else -> stringResource(R.string.home_greeting_evening)
    }
}
