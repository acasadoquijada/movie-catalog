package com.example.moviecatalog.model

import com.google.gson.annotations.SerializedName

/*
Parent class for Movie and TVShow. It holds the common fields and methods for both of them
 */
open class VisualEntertainment {

    var id: Long = -1L
    open var name: String = ""
    @SerializedName("poster_path")
    open var posterPath: String? = ""
    @SerializedName("vote_average")
    var voteAverage: Double? = -1.0
    var overview: String? = ""

    fun isEmpty(): Boolean {
        return id == -1L || name.isEmpty()
    }

    override fun toString(): String {
        return "$id - $name - $posterPath - $voteAverage - $overview"
    }

    fun getPosterPathURL(): String {
        val baseUrl = "https://image.tmdb.org/t/p/"
        val size = "w185"

        return baseUrl + size + posterPath
    }
}
