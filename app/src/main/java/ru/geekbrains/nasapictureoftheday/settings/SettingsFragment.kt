package ru.geekbrains.nasapictureoftheday.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.settings_fragment.*
import ru.geekbrains.nasapictureoftheday.*

class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var parentActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settings_choose_theme_chip_group_default.setOnClickListener(this)
        settings_choose_theme_chip_group_space.setOnClickListener(this)
        settings_choose_theme_chip_group_moon.setOnClickListener(this)
        settings_choose_theme_chip_group_mars.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.settings_choose_theme_chip_group_space -> {
                parentActivity.setCurrentTheme(ThemeSpace)
                parentActivity.recreate()
            }
            R.id.settings_choose_theme_chip_group_moon -> {
                parentActivity.setCurrentTheme(ThemeMoon)
                parentActivity.recreate()
            }
            R.id.settings_choose_theme_chip_group_mars -> {
                parentActivity.setCurrentTheme(ThemeMars)
                parentActivity.recreate()
            }
            else -> {
                parentActivity.setCurrentTheme(ThemeDefault)
                parentActivity.recreate()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

}