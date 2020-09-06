package com.example.moviecatalog.repository

import androidx.lifecycle.MutableLiveData
import com.example.moviecatalog.model.movie.Movie
import com.example.moviecatalog.model.movie.MovieList
import com.example.moviecatalog.model.tv_show.TVShow
import com.example.moviecatalog.model.tv_show.TVShowList
import com.example.moviecatalog.retrofit.controllers.MainController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

    private val mainController = MainController()

    fun getMovies(): MutableLiveData<List<Movie>> {

        val movieList = MutableLiveData<List<Movie>>()

        makeMovieRetrofitCall(mainController.getMovies(), movieList)

        return movieList
    }

    fun searchMovie(name: String): MutableLiveData<List<Movie>> {

        val movieList = MutableLiveData<List<Movie>>()

        makeMovieRetrofitCall(mainController.searchMovie(name), movieList)

        return movieList
    }

    fun getTvShows(): MutableLiveData<List<TVShow>> {

        val movieList = MutableLiveData<List<TVShow>>()

        makeTvShowRetrofitCall(mainController.getTVShows(), movieList)

        return movieList
    }

    fun searchTVShow(name: String): MutableLiveData<List<TVShow>> {

        val movieList = MutableLiveData<List<TVShow>>()

        makeTvShowRetrofitCall(mainController.searchTVShow(name), movieList)

        return movieList
    }

    private fun makeMovieRetrofitCall(movieCall: Call<MovieList>, movieList: MutableLiveData<List<Movie>>) {
        movieCall.enqueue(object : Callback<MovieList?> {
            override fun onResponse(call: Call<MovieList?>, response: Response<MovieList?>) {
                if (response.isSuccessful) {
                    movieList.value = response.body()?.list
                }
            }

            override fun onFailure(call: Call<MovieList?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun makeTvShowRetrofitCall(tvShowCall: Call<TVShowList>, tvShowList: MutableLiveData<List<TVShow>>) {
        tvShowCall.enqueue(object : Callback<TVShowList?> {
            override fun onResponse(call: Call<TVShowList?>, response: Response<TVShowList?>) {
                if (response.isSuccessful) {
                    tvShowList.value = response.body()?.list
                }
            }

            override fun onFailure(call: Call<TVShowList?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
