package com.example.moviecatalog.retrofit

import com.example.moviecatalog.model.movie.MovieList
import com.example.moviecatalog.model.tv_show.TVShowList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query*/

interface API {

    @GET("search/tv")
    fun searchTVShow(
        @Query("api_key") api_key: String,
        @Query("query") name: String
    ): Call<TVShowList>

    @GET("tv/top_rated")
    fun getTVShows(@Query("api_key") api_key: String): Call<TVShowList>

    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") api_key: String,
        @Query("query") name: String
    ): Call<MovieList>

    @GET("movie/top_rated")
    fun getMovies(@Query("api_key") api_key: String): Call<MovieList>
}
