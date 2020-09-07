package com.example.moviecatalog

import com.example.moviecatalog.model.ElementList
import com.example.moviecatalog.retrofit.MainController
import junit.framework.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

/**
 * We test the different retrofit calls.
 * The testing consists in checking that the call are actually successful and that its content is not empty.
 * With more time, the call to fail() could be improved to a method wrapping fail() which would make the code cleaner
 */

@RunWith(JUnit4::class)
class RetrofitUnitTest {

    private val mainController =
        MainController()
    private val tvShowName = "office"

    @Test
    fun searchTVShow_Returns_NotEmptyList() {

        val response: Response<ElementList> = mainController.searchTVShow(tvShowName).execute()

        if (response.isSuccessful) {
            assertElementListIsNotEmpty(response.body())
        } else {
            fail("Call to searchTVShow with param ($tvShowName) was unsuccessful")
        }
    }

    @Test
    fun getTVShows_Returns_NotEmptyList() {
        val response: Response<ElementList> = mainController.getTVShows().execute()

        if (response.isSuccessful) {
            assertElementListIsNotEmpty(response.body())
        } else {
            fail("Call to getTVShows was unsuccessful")
        }
    }

    @Test
    fun searchMovie_Returns_NotEmptyList() {

        val response: Response<ElementList> = mainController.searchMovie(tvShowName).execute()

        if (response.isSuccessful) {
            assertElementListIsNotEmpty(response.body())
        } else {
            fail("Call to movieList with param ($tvShowName) was unsuccessful")
        }
    }

    @Test
    fun getMovies_Returns_NotEmptyList() {

        val response: Response<ElementList> = mainController.getMovies().execute()

        if (response.isSuccessful) {
            assertElementListIsNotEmpty(response.body())
        } else {
            fail("Call to getMovies was unsuccessful")
        }
    }

    private fun assertElementListIsNotEmpty(elementList: ElementList?) {
        elementList?.list?.forEach { assert(!it.isEmpty()) }
    }
}
