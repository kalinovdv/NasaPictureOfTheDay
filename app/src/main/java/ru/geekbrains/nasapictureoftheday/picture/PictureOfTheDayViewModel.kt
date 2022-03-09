package ru.geekbrains.nasapictureoftheday.picture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.geekbrains.nasapictureoftheday.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PictureOfTheDayRetrofitImpl = PictureOfTheDayRetrofitImpl()
) :
    ViewModel() {
    fun getData(date: String): LiveData<PictureOfTheDayData> {
        sendServerRequest(date)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(date: String) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        //val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        //val date: String = simpleDateFormat.format(Calendar.getInstance().time)
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("Вам необходим API ключ"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, date).enqueue(object : Callback<PictureOfTheDayServerResponseData> {
                override fun onResponse(
                    call: Call<PictureOfTheDayServerResponseData>,
                    response: Response<PictureOfTheDayServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable("Неизвестная ошибка"))
                        } else {
                            liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(
                    call: Call<PictureOfTheDayServerResponseData>,
                    t: Throwable
                ) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable(t))
                }

            })
        }
    }
}