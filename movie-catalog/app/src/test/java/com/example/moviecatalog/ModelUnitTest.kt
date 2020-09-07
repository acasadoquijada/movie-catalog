package com.example.moviecatalog

import com.example.moviecatalog.model.Element
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * In this test we want to ensure the getPosterPath() method returns an actual URL.
 * The poster_path url is formed by three parts:
 *      - base_url -> Same for all the TVShows/Movies = "https://image.tmdb.org/t/p/"
 *      - size -> this is hardcoded as we want always the same size
 *      -  poster_path -> this part is filled when the Movie/TVShow is obtained. In RetrofitUnitTest
 *      we ensure this field is not empty
 *
 *      That explained and for simplicity, we are going to check that the returned string contains the
 *      base_url. This test would be improved having more development time
 *
 */

@RunWith(JUnit4::class)
class ModelUnitTest {

    private val baseUrl = "https://image.tmdb.org/t/p/"
    private val fixedPosterPath = "jtAI6OJIWLWiRItNSZoWjrsUtmi.jpg" // actual data from a movie
    private lateinit var element: Element

    @Before
    fun setup() {
        element = Element()
    }

    @Test
    fun elementGetPath_Returns_ActualURL() {

        element.posterPath = fixedPosterPath

        assert(element.getPosterPathURL().contains(baseUrl))
    }

    @Test
    fun elementIsEmpty_Returns_Empty_When_Expected() {

        element.name = "fake name"
        assert(element.isEmpty())
    }

    @Test
    fun elementIsEmpty_Returns_NotEmpty_When_Expected() {

        element.id = 12
        element.name = "fake name"

        assert(!element.isEmpty())
    }
}
