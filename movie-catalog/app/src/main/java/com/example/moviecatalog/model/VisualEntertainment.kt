package com.example.moviecatalog.model

/*
Parent class for Movie and TVShow. It holds the common fields and methods for both of them
 */
open class VisualEntertainment {

    var id: Int = -1
    open var name: String = ""
    open var posterPath: String = ""
    var voteAverage: Double = -1.0
    var overview: String = ""

    fun isEmpty(): Boolean {
        return id == -1 && name.isEmpty() && posterPath.isEmpty() && voteAverage == -1.0 && overview.isEmpty()
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
