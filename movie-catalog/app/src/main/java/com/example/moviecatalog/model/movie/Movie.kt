package com.example.moviecatalog.model.movie

import com.example.moviecatalog.model.VisualEntertainment
import com.google.gson.annotations.SerializedName

/*
In this case, the serialize name of the name and poster_path fields is not the same as in
VisualEntertainment. Because of this, we have to override them and use @SerializedName to select
the correct field when the corresponding retrofit call is performed.
 */
class Movie : VisualEntertainment() {

    @SerializedName("title")
    override var name: String = ""
}
