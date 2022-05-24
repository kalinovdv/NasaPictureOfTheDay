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
import ru.geekbrains.nasapictureoftheday.MainActivity
import ru.geekbrains.nasapictureoftheday.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = Bundle();

        settings_choose_theme_chip_group_space.setOnClickListener {
           //bundle.putString("themeName", "Cosmic")
            val sPref = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = sPref.edit()
            editor.putString("themeName", "Cosmic")
            editor.commit()
            recreate(requireActivity())
        }

        settings_choose_theme_chip_group_moon.setOnClickListener {
            //bundle.putString("themeName", "Moon")
            val sPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val editor: SharedPreferences.Editor = sPref.edit()
            editor.putString("themeName", "Moon")
            editor.commit()
            recreate(requireActivity())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}