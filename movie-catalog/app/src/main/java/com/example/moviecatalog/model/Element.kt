package com.example.moviecatalog.model

import com.google.gson.annotations.SerializedName

/**
 * This class represents both, Movie and TVShows.
 *
 * The only difference between both of them, is how the title/name wording of the TVShow/Movie.
 * - For TVShow is name
 * - For Movie is title
 *
 * Thanks to @SerializedName value and alternate, we can set a list of serialized names for the
 * same fields.
 *
 * By doing so, we can use the same class to represent a Movie and a TVShow
 */
class Element {

    var id: Long = -1L
    @SerializedName("name", alternate = ["title"])
    var name: String = ""
    @SerializedName("poster_path")
    var posterPath: String? = ""
    @SerializedName("vote_average")
    var voteAverage: String? = ""
    var overview: String? = ""
    var watchList: Boolean = false

    /**
     * Design decision, we consider a element empty if its ID or name is empty. With more time and a
     * better study of themovie.org API this could change
     */
    fun isEmpty(): Boolean {
        return id == -1L || name.isEmpty()
    }

    override fun toString(): String {
        return "$id - $name - $posterPath - $voteAverage - $overview"
    }

    /**
     * Returns an actual URL to the poster of the corresponding movie
     */
    fun getPosterPathURL(): String {
        val baseUrl = "https://image.tmdb.org/t/p/"
        val size = "w500"
        return baseUrl + size + posterPath
    }
}
