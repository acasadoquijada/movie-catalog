package com.example.moviecatalog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviecatalog.util.getOrAwaitValue
import com.example.moviecatalog.viewmodel.ViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ViewModelUnitTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()
    private val viewModel = ViewModel()
    private val tvShowName = "office"
    private val movieName = "tenet"

    @Test
    fun getTVShowList_Return_NotEmpty_LiveData() {
        viewModel.getTvShowList().getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }

    @Test
    fun getMovieList_Return_NotEmpty_LiveData() {
        viewModel.getMovieList().getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }

    @Test
    fun searchTVShow_Return_NotEmpty_LiveData() {
        viewModel.searchTVShow(tvShowName).getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }

    @Test
    fun searchMovie_Return_NotEmpty_LiveData() {
        viewModel.searchMovie(movieName).getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }
}
