package ru.geekbrains.nasapictureoftheday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.nasapictureoftheday.picture.PictureOfTheDayFragment

const val ThemeDefault = "DEFAULT"
const val ThemeSpace = "SPACE"
const val ThemeMoon = "MOON"
const val ThemeMars = "MARS"

class MainActivity : AppCompatActivity() {

    private val KEY_SP = "sp"
    private val KEY_CURRNT_THEME = "current_theme"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(getStyle(getCurrentTheme()))
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }

    fun setCurrentTheme(currentTheme: String) {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_CURRNT_THEME, currentTheme)
        editor.apply()
    }

    private fun getCurrentTheme(): String? {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        return sharedPreferences.getString(KEY_CURRNT_THEME, "")
    }

    private fun getStyle(currentTheme: String?): Int {
        return when(currentTheme) {
            ThemeSpace -> R.style.Theme_NasaPictureOfTheDayCosmic
            ThemeMoon -> R.style.Theme_NasaPictureOfTheDayMoon
            ThemeMars -> R.style.Theme_NasaPictureOfTheDayMars
            else -> R.style.Theme_NasaPictureOfTheDay
        }
    }
}