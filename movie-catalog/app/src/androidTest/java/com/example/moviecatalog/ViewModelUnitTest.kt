package com.example.moviecatalog

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.moviecatalog.adapter.ElementAdapter.Companion.MOVIE
import com.example.moviecatalog.adapter.ElementAdapter.Companion.TVSHOW
import com.example.moviecatalog.util.getOrAwaitValue
import com.example.moviecatalog.viewmodel.ViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewModelUnitTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ViewModel
    private val tvShowName = "office"
    private val movieName = "tenet"

    @Before
    fun setup() {
        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        viewModel = ViewModel(application)
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
    fun setDetailElement_Sets_TVSHOW_Type_Query() {
        viewModel.searchTVShow(tvShowName).getOrAwaitValue()
        viewModel.setDetailElement(0, TVSHOW)
        assert(!viewModel.getDetailElement().getOrAwaitValue().isEmpty())
    }

    @Test
    fun setDetailElement_Sets_Movie_Type_Query() {
        viewModel.searchMovie(movieName).getOrAwaitValue()
        viewModel.setDetailElement(0, MOVIE)
        assert(!viewModel.getDetailElement().getOrAwaitValue().isEmpty())
    }
}
