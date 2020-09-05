package com.example.moviecatalog

import com.example.moviecatalog.model.movie.MovieList
import com.example.moviecatalog.model.tv_show.TVShowList
import com.example.moviecatalog.retrofit.controllers.MainController
import junit.framework.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

/*
We test the different retrofit calls.
The testing consists in checking that the call are actually successful and that its content is
not empty.

With more time, the call to fail() could be improved to a method wrapping fail() which would make
the code cleaner
 */
@RunWith(JUnit4::class)
class RetrofitUnitTest {

    private val mainController = MainController()
    private val tvShowName = "office"

    @Test
    fun searchTVShow_Returns_NotEmptyList() {

        val response: Response<TVShowList> = mainController.searchTVShow(tvShowName).execute()

        if (response.isSuccessful) {
            assertTVShowListIsNotEmpty(response.body())
        } else {
            fail("Call to searchTVShow with param ($tvShowName) was unsuccessful")
        }
    }

    @Test
    fun getTVShows_Returns_NotEmptyList() {
        val response: Response<TVShowList> = mainController.getTVShows().execute()

        if (response.isSuccessful) {
            assertTVShowListIsNotEmpty(response.body())
        } else {
            fail("Call to getTVShows was unsuccessful")
        }
    }

    @Test
    fun searchMovie_Returns_NotEmptyList() {

        val response: Response<MovieList> = mainController.searchMovie(tvShowName).execute()

        if (response.isSuccessful) {
            assertMovieListIsNotEmpty(response.body())
        } else {
            fail("Call to movieList with param ($tvShowName) was unsuccessful")
        }
    }

    @Test
    fun getMovies_Returns_NotEmptyList() {

        val response: Response<MovieList> = mainController.getMovies().execute()

        if (response.isSuccessful) {
            assertMovieListIsNotEmpty(response.body())
        } else {
            fail("Call to getMovies was unsuccessful")
        }
    }

    private fun assertMovieListIsNotEmpty(movieList: MovieList?) {
        movieList?.list?.forEach { assert(!it.isEmpty()) }
    }

    private fun assertTVShowListIsNotEmpty(tvShowList: TVShowList?) {
        tvShowList?.list?.forEach { assert(!it.isEmpty()) }
    }
}
