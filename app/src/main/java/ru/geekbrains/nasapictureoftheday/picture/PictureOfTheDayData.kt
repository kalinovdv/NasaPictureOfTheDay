package ru.geekbrains.nasapictureoftheday.picture

sealed class PictureOfTheDayData {
    data class Success(val serverResponseData: PictureOfTheDayServerResponseData) : PictureOfTheDayData()
    data class Error(val error: Throwable) : PictureOfTheDayData()
    data class Loading(val progress: Int?) : PictureOfTheDayData()
}
