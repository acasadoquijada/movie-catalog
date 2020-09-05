package com.example.moviecatalog.model.tv_show

import com.google.gson.annotations.SerializedName

class TVShowList {

    @SerializedName("results")
    var list: List<TVShow> = ArrayList()
}
