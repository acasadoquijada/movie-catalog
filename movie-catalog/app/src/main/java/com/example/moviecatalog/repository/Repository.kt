package com.example.moviecatalog.repository

import androidx.lifecycle.MutableLiveData
import com.example.moviecatalog.model.Element
import com.example.moviecatalog.model.ElementList
import com.example.moviecatalog.retrofit.MainController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class uses the MainController to perform the calls to themovie.org APIs according to the
 * users actions
 */
class Repository {

    private val mainController =
        MainController()

    fun getMovies(): MutableLiveData<List<Element>> {

        val movieList = MutableLiveData<List<Element>>()

        makeMovieRetrofitCall(mainController.getMovies(), movieList)

        return movieList
    }

    fun searchMovie(name: String): MutableLiveData<List<Element>> {

        val movieList = MutableLiveData<List<Element>>()

        makeMovieRetrofitCall(mainController.searchMovie(name), movieList)

        return movieList
    }

    fun getTvShows(): MutableLiveData<List<Element>> {

        val movieList = MutableLiveData<List<Element>>()

        makeTvShowRetrofitCall(mainController.getTVShows(), movieList)

        return movieList
    }

    fun searchTVShow(name: String): MutableLiveData<List<Element>> {

        val movieList = MutableLiveData<List<Element>>()

        makeTvShowRetrofitCall(mainController.searchTVShow(name), movieList)

        return movieList
    }

    private fun makeMovieRetrofitCall(movieCall: Call<ElementList>, movieList: MutableLiveData<List<Element>>) {
        movieCall.enqueue(object : Callback<ElementList?> {
            override fun onResponse(call: Call<ElementList?>, response: Response<ElementList?>) {
                if (response.isSuccessful) {
                    movieList.value = response.body()?.list
                }
            }

            override fun onFailure(call: Call<ElementList?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun makeTvShowRetrofitCall(tvShowCall: Call<ElementList>, tvShowList: MutableLiveData<List<Element>>) {
        tvShowCall.enqueue(object : Callback<ElementList?> {
            override fun onResponse(call: Call<ElementList?>, response: Response<ElementList?>) {
                if (response.isSuccessful) {
                    tvShowList.value = response.body()?.list
                }
            }

            override fun onFailure(call: Call<ElementList?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
