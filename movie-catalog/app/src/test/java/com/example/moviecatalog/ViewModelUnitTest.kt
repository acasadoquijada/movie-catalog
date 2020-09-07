package com.example.moviecatalog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviecatalog.adapter.ElementAdapter.Companion.MOVIE
import com.example.moviecatalog.adapter.ElementAdapter.Companion.TVSHOW
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
    private var viewModel = ViewModel()
    private val tvShowName = "office"
    private val movieName = "tenet"

    @Test
    fun getTVShowList_Return_NotEmpty_LiveData_NoQuery() {
        viewModel.getTvShowList().getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }

    @Test
    fun getMovieList_Return_NotEmpty_LiveData_NoQuery() {
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

    @Test
    fun getTVShowList_Return_NotEmpty_LiveData_Query() {
        viewModel.searchTVShow(tvShowName)
        viewModel.query = tvShowName
        viewModel.getTvShowList().getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }

    @Test
    fun getMovieList_Return_NotEmpty_LiveData_Query() {
        viewModel.searchMovie(movieName)
        viewModel.query = movieName
        viewModel.getMovieList().getOrAwaitValue().forEach { assert(!it.isEmpty()) }
    }

    
    @Test
    fun setDetailElement_Sets_TVSHOW_Type_NoQuery() {
        viewModel.getTvShowList().getOrAwaitValue()
        viewModel.setDetailElement(0, TVSHOW)
        assert(!viewModel.getDetailElement().getOrAwaitValue().isEmpty())
    }

    @Test
    fun setDetailElement_Sets_Movie_Type_NoQuery() {
        viewModel.getMovieList().getOrAwaitValue()
        viewModel.setDetailElement(0, MOVIE)
        assert(!viewModel.getDetailElement().getOrAwaitValue().isEmpty())
    }

    @Test
    fun setDetailElement_Sets_TVSHOW_Type_Query() {
        viewModel.searchTVShow(tvShowName).getOrAwaitValue()
        viewModel.query = tvShowName
        viewModel.setDetailElement(0, TVSHOW)
        assert(!viewModel.getDetailElement().getOrAwaitValue().isEmpty())
    }

    @Test
    fun setDetailElement_Sets_Movie_Type_Query() {
        viewModel.searchMovie(movieName).getOrAwaitValue()
        viewModel.query = movieName
        viewModel.setDetailElement(0, MOVIE)
        assert(!viewModel.getDetailElement().getOrAwaitValue().isEmpty())
    }
}
