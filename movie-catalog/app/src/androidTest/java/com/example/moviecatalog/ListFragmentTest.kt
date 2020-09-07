package com.example.moviecatalog

import android.widget.AutoCompleteTextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviecatalog.adapter.ElementAdapter
import com.example.moviecatalog.ui.ListFragment
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListFragmentTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun infoIsShownCorrectly() {
        checkTextIsNotEmpty(R.id.movieLabel)
        checkTextIsNotEmpty(R.id.tvShowLabel)
        checkRecyclerViewHasDescendant(R.id.movieRecyclerView)
        checkRecyclerViewHasDescendant(R.id.tvShowRecyclerView)
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

    private fun checkRecyclerViewHasDescendant(recyclerView: Int) {
        onView(withId(recyclerView)).perform(RecyclerViewActions.scrollToPosition<ElementAdapter.ElementHolder>(0)).check(
            matches(
                hasDescendant(
                    withId(R.id.image)
                )
            )
        )
    }

    @Test
    fun searchShowsCorrectInfo() {
        onView(withId(R.id.action_search)).perform(click())
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText("something"))

        checkRecyclerViewHasDescendant(R.id.movieRecyclerView)
        checkRecyclerViewHasDescendant(R.id.tvShowRecyclerView)
    }

    @Test
    fun navigateFragmentDetail() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<ListFragment>()

        // Set the NavController property on the fragment
        titleScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController’s state
        onView(withId(R.id.movieRecyclerView)).perform(RecyclerViewActions.scrollToPosition<ElementAdapter.ElementHolder>(0)).perform(click())

        assert(navController.currentDestination?.id == R.id.detailFragment)
    }
}
