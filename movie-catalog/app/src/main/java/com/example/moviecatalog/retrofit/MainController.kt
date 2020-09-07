package com.example.moviecatalog.retrofit

import com.example.moviecatalog.model.ElementList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainController {

    private val baseUrl = "https://api.themoviedb.org/3/"
    private val apiKey = "a645b5803140e9f1bda613760dda6057" // This is the API from themovie.org

    private lateinit var retrofit: Retrofit
    private lateinit var api: API

    init {
        createRetrofit(createGson())
        createAPI()
    }

    private fun createRetrofit(gson: Gson) {
        retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    private fun createGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    private fun createAPI() {
        api = retrofit.create(API::class.java)
    }

    fun searchTVShow(name: String): Call<ElementList> {
        return api.searchTVShow(apiKey, name)
    }

    fun searchMovie(name: String): Call<ElementList> {
        return api.searchMovie(apiKey, name)
    }

    fun getTVShows(): Call<ElementList> {
        return api.getTVShows(apiKey)
    }

    fun getMovies(): Call<ElementList> {
        return api.getMovies(apiKey)
    }
}
