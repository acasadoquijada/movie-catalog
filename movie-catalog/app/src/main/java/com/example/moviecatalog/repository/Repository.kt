package com.example.moviecatalog.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviecatalog.adapter.ElementAdapter.Companion.MOVIE
import com.example.moviecatalog.adapter.ElementAdapter.Companion.TVSHOW
import com.example.moviecatalog.db.AppDataBase
import com.example.moviecatalog.db.ElementDAO
import com.example.moviecatalog.model.Element
import com.example.moviecatalog.model.ElementList
import com.example.moviecatalog.retrofit.MainController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The class can be divided into 2 pieces according to its logic:
 *  - Perform the calls to obtain the information from themovie.org API using retrofit
 *  - Perform the operations related to the database using Room
 */
class Repository(context: Context) {

    private val mainController =
        MainController()

    private var elementDao: ElementDAO = AppDataBase.getDatabase(context).elementDAO()

    /**
     * Perform the calls to obtain the information from themovie.org API using retrofit
     */
    fun getMovies(): MutableLiveData<List<Element>> {

        val movieList = MutableLiveData<List<Element>>()

        makeRetrofitCall(mainController.getMovies(), movieList, MOVIE)

        return movieList
    }

    fun getTvShows(): MutableLiveData<List<Element>> {

        val movieList = MutableLiveData<List<Element>>()

        makeRetrofitCall(mainController.getTVShows(), movieList, TVSHOW)

        return movieList
    }

    fun searchMovie(name: String): MutableLiveData<List<Element>> {

        val movieList = MutableLiveData<List<Element>>()

        makeRetrofitCall(mainController.searchMovie(name), movieList, MOVIE)

        return movieList
    }

    fun searchTVShow(name: String): MutableLiveData<List<Element>> {

        val movieList = MutableLiveData<List<Element>>()

        makeRetrofitCall(mainController.searchTVShow(name), movieList, TVSHOW)

        return movieList
    }

    private fun makeRetrofitCall(movieCall: Call<ElementList>, elementList: MutableLiveData<List<Element>>, type: String) {
        movieCall.enqueue(object : Callback<ElementList?> {
            override fun onResponse(call: Call<ElementList?>, response: Response<ElementList?>) {
                if (response.isSuccessful) {
                    setLogo(response.body()?.list, type)
                    elementList.value = response.body()?.list
                }
            }

            override fun onFailure(call: Call<ElementList?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    /**
     * The logo will differentiate the movies from the TV shows when shown in the recyclerviews
     */
    private fun setLogo(elementList: List<Element>?, type: String) {
        if (type == MOVIE) {
            setLogoMovie(elementList)
        } else {
            setLogoTVShow(elementList)
        }
    }

    private fun setLogoTVShow(list: List<Element>?) {
        list?.forEach { it.type = TVSHOW }
    }

    private fun setLogoMovie(list: List<Element>?) {
        list?.forEach { it.type = MOVIE }
    }

    /**
     * Perform the operations related to the database using Room
     */

    fun getWatchList(): LiveData<List<Element>> {
        return elementDao.getWatchList()
    }

    fun insertElementWatchList(element: Element) {

        if (!isElementInWatchList(element)) {
            elementDao.insert(element)
        }
    }

    fun deleteElementWatchList(element: Element) {
        elementDao.delete(element)
    }

    private fun isElementInWatchList(element: Element): Boolean {
        val elementDB = elementDao.getElementById(element.id)
        return elementDB != null
    }
}
