package com.example.moviecatalog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviecatalog.repository.Repository
import com.example.moviecatalog.util.getOrAwaitValue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RepositoryUnitTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()
    private val repository = Repository()

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
