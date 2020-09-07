package com.example.moviecatalog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviecatalog.adapter.ElementAdapter.Companion.MOVIE
import com.example.moviecatalog.adapter.ElementAdapter.Companion.WATCHLIST
import com.example.moviecatalog.model.Element
import com.example.moviecatalog.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This class can be divided into 2 according to its logic:
 *  - get and provide LiveData to the UI classes.
 *  - update the elements in the watchList database
 *
 * */

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application)
    private lateinit var elementList: LiveData<List<Element>>
    private var watchList: LiveData<List<Element>> = MutableLiveData<List<Element>>()
    private var movieSearchList = MutableLiveData<List<Element>>()
    private var tvShowSearchList = MutableLiveData<List<Element>>()
    private var detailElement = MutableLiveData<Element>()

    private var query: String = ""

    /**
     * Get and provide LiveData to the UI classes.
     */
    fun getWatchList(): LiveData<List<Element>> {
        watchList = repository.getWatchList()
        return watchList
    }

    fun getElementList(): LiveData<List<Element>> {
        elementList = getCombinedMovieTvShowLiveData()
        return elementList
    }

    private fun getCombinedMovieTvShowLiveData(): LiveData<List<Element>> {

        val liveData1 = repository.getMovies()
        val liveData2 = repository.getTvShows()

        val result = MediatorLiveData<List<Element>>()

        result.addSource(liveData1) { value ->
            result.value = combineMovieTvShowLiveData(liveData1, liveData2)
        }
        result.addSource(liveData2) { value ->
            result.value = combineMovieTvShowLiveData(liveData1, liveData2)
        }
        return result
    }

    private fun combineMovieTvShowLiveData(
        movieLiveData: LiveData<List<Element>>,
        tvShowLiveData: LiveData<List<Element>>
    ): List<Element>? {

        val movieValueList = movieLiveData.value
        val tvShowValueLIst = tvShowLiveData.value
        val combinedValueList: MutableList<Element> = ArrayList()

        var size = 0

        movieValueList?.size?.let {
            size = it
        }

        for (i in 0 until size) {
            movieValueList?.get(i)?.let { combinedValueList.add(it) }
            tvShowValueLIst?.get(i)?.let { combinedValueList.add(it) }
        }
        return combinedValueList
    }

    fun getDetailElement(): MutableLiveData<Element> {
        return detailElement
    }

    /*
     * We need to select the correct Element depending on the current status of the adapters
     */
    fun setDetailElement(position: Int, type: String) {

        when (type) {
            MOVIE -> setDetailElementMovie(position)
            WATCHLIST -> setDetailElementWatchList(position)
            else -> setDetailElementTvShow(position)
        }

        checkDetailElementInWatchList()
    }

    private fun checkDetailElementInWatchList() {
        watchList.value?.forEach { element -> if (detailElement.value?.id == element.id) detailElement.value?.watchList = true }
    }

    private fun setDetailElementMovie(position: Int) {
        detailElement.value = if (query.isEmpty()) {
            elementList.value?.get(position)
        } else {
            movieSearchList.value?.get(position)
        }
    }

    private fun setDetailElementWatchList(position: Int) {
        detailElement.value = watchList.value?.get(position)
    }

    private fun setDetailElementTvShow(position: Int) {
        detailElement.value = if (query.isEmpty()) {
            elementList.value?.get(position)
        } else {
            tvShowSearchList.value?.get(position)
        }
    }

    fun searchTVShow(name: String): LiveData<List<Element>> {
        updateQuery(name)
        tvShowSearchList = repository.searchTVShow(name)
        return tvShowSearchList
    }

    fun searchMovie(name: String): LiveData<List<Element>> {
        updateQuery(name)
        movieSearchList = repository.searchMovie(name)
        return movieSearchList
    }

    private fun updateQuery(query: String) {
        this.query = query
    }

    /*
     * For a better user experience, when the query is empty, the values of listElement and
     * watchList are set to tvShowList/movieSearchList in order to show the "default" elements
     * to the user.
     */

    fun cleanQuery() {
        updateQuery("")
        resetSearchListValues()
    }

    private fun resetSearchListValues() {
        tvShowSearchList.value = watchList.value
        movieSearchList.value = elementList.value
    }

    /**
     * Update the elements in the watchList database according to the user actions
     */
    fun setWatchListStatus(inWatchList: Boolean) = viewModelScope.launch(Dispatchers.IO) {

        detailElement.value?.let { element ->
            element.watchList = inWatchList

            if (inWatchList) {
                insertElementWatchList(element)
            } else {
                deleteElementWatchList(element)
            }
        }
    }

    private fun insertElementWatchList(element: Element) {
        updateWatchListStatus(element, true)
        repository.insertElementWatchList(element)
    }

    private fun deleteElementWatchList(element: Element) {
        updateWatchListStatus(element, false)
        repository.deleteElementWatchList(element)
    }

    private fun updateWatchListStatus(element: Element, boolean: Boolean) {
        element.watchList = boolean
    }
}
