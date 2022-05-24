package ru.geekbrains.nasapictureoftheday

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import androidx.fragment.app.FragmentManager
import ru.geekbrains.nasapictureoftheday.picture.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val sPref = getPreferences(MODE_PRIVATE)
        when(savedInstanceState?.getString("themeName", "")){
            "Cosmic" -> setTheme(R.style.Theme_NasaPictureOfTheDayCosmic)
            "Moon" -> setTheme(R.style.Theme_NasaPictureOfTheDayMoon)
            "Mars" -> setTheme(R.style.Theme_NasaPictureOfTheDayMars)
            "Default" -> setTheme(R.style.Theme_NasaPictureOfTheDay)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }
}