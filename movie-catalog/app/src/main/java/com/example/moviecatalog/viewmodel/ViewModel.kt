package com.example.moviecatalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalog.adapter.ElementAdapter.Companion.MOVIE
import com.example.moviecatalog.adapter.ElementAdapter.Companion.TVSHOW
import com.example.moviecatalog.model.Element
import com.example.moviecatalog.repository.Repository

class ViewModel : ViewModel() {

    private val repository = Repository()
    private var tvShowList = repository.getTvShows()
    private var movieList = repository.getMovies()
    private var tvShowSearchList = MutableLiveData<List<Element>>()
    private var movieSearchList = MutableLiveData<List<Element>>()
    private var detailElement = MutableLiveData<Element>()

    // New list for watchList.
    // To do so, we need another menu item -> ... "explore"/"watchList"
    var query: String = ""

    /**
     * Query.isEmpty() == true means that the user didn't search any Movie or TVShow, then we return
     * the Movie/TVShow list obtain from the Repository
     */

    fun getTvShowList(): LiveData<List<Element>> {

        if (query.isEmpty()) {
            return tvShowList
        }
        return tvShowSearchList
    }

    fun getMovieList(): LiveData<List<Element>> {

        if (query.isEmpty()) {
            return movieList
        }

        return movieSearchList
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

    private fun updateQuery(query: String){
        this.query = query
    }

    fun getDetailElement(): MutableLiveData<Element> {
        return detailElement
    }

    /**
     * As there are two adapter (one for Movies and another for TVShows) we need to check the
     * clicked element position along with the adapter where the click was performed
     */

    fun setDetailElement(position: Int, type: String) {
        println(tvShowList.value?.size)
        println(movieList.value?.size)

        when (type) {
            TVSHOW -> setDetailElementMovie(position)
            MOVIE -> setDetailElementTVShow(position)
        }
    }

    /**
     * Check if query is empty. In case the user clicked in a TVShow/Movie result of a search
     */
    private fun setDetailElementMovie(position: Int) {
        detailElement.value = if (query.isEmpty()) {
            tvShowList.value?.get(position)
        } else {
            tvShowSearchList.value?.get(position)
        }
    }

    private fun setDetailElementTVShow(position: Int) {
        detailElement.value = if (query.isEmpty()) {
            movieList.value?.get(position)
        } else {
            movieSearchList.value?.get(position)
        }
    }

    fun setWatchListStatus(boolean: Boolean) {
        detailElement.value?.watchList = boolean

        // Here I should call repository.addToWatchList(detailElementValue)
    }


    /**
     * For a better user experience, when the query is empty, the values of tvShowSearchList and
     * movieSearchList are set to tvShowList/movieSearchList in order to show the "default" elements
     * to the user.
     *
     * If this is not done, it would be necessary to perform a configuration change (rotation for
     * example) to call again getTVShowList() and getMovieList() to obtain the "default" elements
     */

    fun cleanQuery() {
        query = ""
        resetSearchListValues()
    }

    private fun resetSearchListValues() {
        tvShowSearchList.value = tvShowList.value
        movieSearchList.value = movieList.value
    }
}
