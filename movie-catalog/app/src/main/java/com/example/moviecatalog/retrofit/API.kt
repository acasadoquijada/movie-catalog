package com.example.moviecatalog.retrofit

import com.example.moviecatalog.model.ElementList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Definition of the calls to themovie.org APIs
 */
interface API {

    @GET("search/tv")
    fun searchTVShow(
        @Query("api_key") api_key: String,
        @Query("query") name: String
    ): Call<ElementList>

    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") api_key: String,
        @Query("query") name: String
    ): Call<ElementList>

    @GET("tv/top_rated")
    fun getTVShows(@Query("api_key") api_key: String): Call<ElementList>

    @GET("movie/top_rated")
    fun getMovies(@Query("api_key") api_key: String): Call<ElementList>
}
