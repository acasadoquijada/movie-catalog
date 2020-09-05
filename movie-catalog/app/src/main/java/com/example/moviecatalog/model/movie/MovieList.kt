package com.example.moviecatalog.model.movie

import com.google.gson.annotations.SerializedName

class MovieList {
    @SerializedName("results")
    var list: List<Movie> = ArrayList()
}
