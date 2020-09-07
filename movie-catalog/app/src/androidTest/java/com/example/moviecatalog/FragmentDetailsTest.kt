package com.example.moviecatalog

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.moviecatalog.adapter.ElementAdapter
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FragmentDetailsTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Before
    fun setup() {
        onView(withId(R.id.movieRecyclerView)).perform(RecyclerViewActions.scrollToPosition<ElementAdapter.ElementHolder>(0)).perform(click())
    }

    @Test
    fun infoIsShownCorrectly() {
        checkTextIsNotEmpty(R.id.name)
        checkTextIsNotEmpty(R.id.overview)
        checkTextIsNotEmpty(R.id.voteAverage)
        checkImageIsDisplayed(R.id.poster)
        checkImageIsDisplayed(R.id.toggleButton)
    }

    private fun checkTextIsNotEmpty(id: Int) {
        onView(withId(id)).check(
            matches(
                Matchers.not(
                    withText("")
                )
            )
        )
    }

    private fun checkImageIsDisplayed(id: Int) {
        onView(allOf(withId(id), isDescendantOfA(withId(R.id.posterDetails)))).check(
            matches(
                isDisplayed()
            )
        )
    }
}
