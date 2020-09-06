package com.example.moviecatalog.viewmodel

import androidx.lifecycle.LiveData
import com.example.moviecatalog.model.movie.Movie
import com.example.moviecatalog.model.tv_show.TVShow
import com.example.moviecatalog.repository.Repository

class ViewModel {

    private val repository = Repository()
    private var tvShowList: LiveData<List<TVShow>>
    private var movieList: LiveData<List<Movie>>

    init {
        tvShowList = repository.getTvShows()
        movieList = repository.getMovies()
    }

    fun getTvShowList(): LiveData<List<TVShow>> {
        return tvShowList
    }

    fun getMovieList(): LiveData<List<Movie>> {
        return movieList
    }

    fun searchTVShow(name: String): LiveData<List<TVShow>> {
        return repository.searchTVShow(name)
    }

    fun searchMovie(name: String): LiveData<List<Movie>> {
        return repository.searchMovie(name)
    }
}
