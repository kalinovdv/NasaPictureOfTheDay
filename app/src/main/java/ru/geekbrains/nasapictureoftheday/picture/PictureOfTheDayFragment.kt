package ru.geekbrains.nasapictureoftheday.picture

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.main_fragment.*
import ru.geekbrains.nasapictureoftheday.MainActivity
import ru.geekbrains.nasapictureoftheday.R
import ru.geekbrains.nasapictureoftheday.settings.SettingsFragment
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var viewModel: PictureOfTheDayViewModel

    private lateinit var dateOfCalendar: Calendar

    private lateinit var dateOfPicture: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateOfCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateOfPicture = simpleDateFormat.format(dateOfCalendar.time)

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet))

        main_search_in_wiki.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                Uri.parse("https://en.wikipedia.org/wiki/${main_search_in_wiki_text.text.toString()}")
            })
        }

        main_fab.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        main_image_selection_chips_group_today.setOnClickListener {
            dateOfCalendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateOfPicture = simpleDateFormat.format(dateOfCalendar.time)
            viewModel.getData(dateOfPicture).observe(
                this@PictureOfTheDayFragment
            ) { renderData(it) }
        }

        main_image_selection_chips_group_yesterday.setOnClickListener {
            dateOfCalendar = Calendar.getInstance()
            dateOfCalendar.add(Calendar.DATE, -1)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateOfPicture = simpleDateFormat.format(dateOfCalendar.time)
            viewModel.getData(dateOfPicture).observe(
                this@PictureOfTheDayFragment
            ) { renderData(it) }
        }

        main_image_selection_chips_group_two_days_ago.setOnClickListener {
            dateOfCalendar = Calendar.getInstance()
            dateOfCalendar.add(Calendar.DATE, -2)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateOfPicture = simpleDateFormat.format(dateOfCalendar.time)
            viewModel.getData(dateOfPicture).observe(
                this@PictureOfTheDayFragment
            ) { renderData(it) }
        }

        viewModel = ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
        viewModel.getData(dateOfPicture).observe(
            this@PictureOfTheDayFragment
        ) { renderData(it) }

        setBottomAppBar(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_bar_favorit -> Toast.makeText(context, "Избранное", Toast.LENGTH_SHORT)
                .show()
            R.id.bottom_bar_search -> {
                val settingsFragment = SettingsFragment.newInstance()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, SettingsFragment.newInstance(), "settings")?.addToBackStack("")?.commit()

            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(
                        it.supportFragmentManager,
                        "tag"
                    )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.main_bottom_app_bar))
        setHasOptionsMenu(true)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                val explanation = serverResponseData.explanation
                if (url.isNullOrEmpty()) {
                    Toast.makeText(context, "Пустая ссылка", Toast.LENGTH_LONG).show()
                } else {
                    main_image_view.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }

                    bottomSheetBehavior.addBottomSheetCallback(object :
                        BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            when (newState) {
                                BottomSheetBehavior.STATE_EXPANDED -> {
                                    val textDescription =
                                        bottomSheet.findViewById<TextView>(R.id.bottom_sheet_description)
                                    textDescription.text = explanation
                                }
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {

                        }
                    })
                }
            }
            is PictureOfTheDayData.Loading -> {
                //Toast.makeText(context, "Загрузка данных...", Toast.LENGTH_SHORT).show()
            }
            is PictureOfTheDayData.Error -> {
                Toast.makeText(context, data.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}