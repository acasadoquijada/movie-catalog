package com.example.moviecatalog

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.moviecatalog.adapter.ElementAdapter
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentDetailsTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Before
    fun setup() {
    }

    /**
     * This test passes if run alone, it seems it's some kind of timing issue.
     * I would have improved its behavior with more time
     */
    @Test
    fun infoIsShownCorrectly() {
        // Timing issue, need to click twice. This would have been improved with more time
        onView(withId(R.id.leftRecyclerView)).perform(RecyclerViewActions.scrollToPosition<ElementAdapter.ElementHolder>(0)).perform(click())
        onView(withId(R.id.leftRecyclerView)).perform(RecyclerViewActions.scrollToPosition<ElementAdapter.ElementHolder>(0)).perform(click())

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

    inline fun waitUntilLoaded(crossinline recyclerProvider: () -> RecyclerView) {
        Espresso.onIdle()

        lateinit var recycler: RecyclerView

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            recycler = recyclerProvider()
        }

        while (recycler.hasPendingAdapterUpdates()) {
            Thread.sleep(10)
        }
    }
}
