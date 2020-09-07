package com.example.moviecatalog

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.moviecatalog.repository.Repository
import com.example.moviecatalog.util.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var repository: Repository

    @Before
    fun setup() {
        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        repository = Repository(application.applicationContext)
    }

    @Test
    fun getMovies_Returns_NotEmpty_LiveData() {
        repository.getMovies().getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }

    @Test
    fun getTVShows_Returns_NotEmpty_LiveData() {
        repository.getTvShows().getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }

    @Test
    fun searchMovie_Returns_NotEmpty_LiveData() {
        repository.searchMovie("tenet").getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }

    @Test
    fun searchTVShow_Returns_NotEmpty_LiveData() {
        repository.searchTVShow("tenet").getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }
}
