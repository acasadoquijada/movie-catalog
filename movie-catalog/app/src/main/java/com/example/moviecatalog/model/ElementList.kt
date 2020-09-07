package com.example.moviecatalog.model

import com.google.gson.annotations.SerializedName

/**
 * Needed for the retrofit calls, as they return a list of elements
 */
class ElementList {
    @SerializedName("results")
    var list: List<Element> = ArrayList()
}
