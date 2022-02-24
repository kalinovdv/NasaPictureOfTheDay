package ru.geekbrains.nasapictureoftheday.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.main_fragment.*
import ru.geekbrains.nasapictureoftheday.R

class PictureOfTheDayFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var viewModel: PictureOfTheDayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }

        viewModel = ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
        viewModel.getData().observe(
            this@PictureOfTheDayFragment,
            { renderData(it) })
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
                    image_view.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }

                    bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            when (newState) {
                                BottomSheetBehavior.STATE_EXPANDED -> {
                                    val textDescription = bottomSheet.findViewById<TextView>(R.id.bottom_sheet_description)
                                    textDescription.setText(explanation)
                                }
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {

                        }
                    })
                }
            }
            is PictureOfTheDayData.Loading -> {
                Toast.makeText(context, "Загрузка данных...", Toast.LENGTH_LONG).show()
            }
            is PictureOfTheDayData.Error -> {
                Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
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