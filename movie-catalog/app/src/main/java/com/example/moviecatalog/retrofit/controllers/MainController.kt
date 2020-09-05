package com.example.moviecatalog.retrofit.controllers

import com.example.moviecatalog.model.movie.MovieList
import com.example.moviecatalog.model.tv_show.TVShowList
import com.example.moviecatalog.retrofit.API
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainController {

    private val BASE_URL = "https://api.themoviedb.org/3/"
    private val API_KEY = "a645b5803140e9f1bda613760dda6057" // This is the API from themovie.org

    private lateinit var retrofit: Retrofit
    private lateinit var api: API

    init {
        createRetrofit(createGson())
        createAPI()
    }

    private fun createRetrofit(gson: Gson) {
        retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    private fun createGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    private fun createAPI() {
        api = retrofit.create(API::class.java)
    }

    fun searchTVShow(name: String): Call<TVShowList> {
        return api.searchTVShow(API_KEY, name)
    }

    fun getTVShows(): Call<TVShowList> {
        return api.getTVShows(API_KEY)
    }

    fun searchMovie(name: String): Call<MovieList> {
        return api.searchMovie(API_KEY, name)
    }

    fun getMovies(): Call<MovieList> {
        return api.getMovies(API_KEY)
    }
}
