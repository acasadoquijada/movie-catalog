package com.example.moviecatalog.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.R
import com.example.moviecatalog.adapter.ElementAdapter
import com.example.moviecatalog.adapter.ElementAdapter.Companion.MOVIE
import com.example.moviecatalog.adapter.ElementAdapter.Companion.TVSHOW
import com.example.moviecatalog.databinding.FragmentListBinding
import com.example.moviecatalog.model.Element
import com.example.moviecatalog.viewmodel.ViewModel
/**
 * The class can be divided into 4 pieces according to its logic:
 *  - Setup DataBinding and RecyclerViews (for movies and TVShows)
 *  - Setup ViewModel and observe the different LiveData objects
 *  - Setup behavior searchView for searching movies/TVShows
 *  - Override onClickListener from Element Adapter, get the clicked element and launch
 * the DetailFragment
 */

class ListFragment : Fragment(), ElementAdapter.OnClickElementInterface {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ViewModel
    private var movieAdapter = ElementAdapter(this, MOVIE)
    private var tvShowAdapter = ElementAdapter(this, TVSHOW)

    /**
     * Part 1 - Setup DataBinding and RecyclerViews (for movies and TVShows)
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupDataBinding(inflater, container)
        setupRecyclerViews()
        return getRoot()
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
    }

    private fun getRoot(): View {
        return binding.root
    }

    private fun setupRecyclerViews() {
        setupMovieListRecyclerView()
        setupTvShowRecyclerView()
    }

    private fun setupMovieListRecyclerView() {
        setMovieLayoutManager()
        setMovieAdapter()
    }

    private fun setupTvShowRecyclerView() {
        setTVShowLayoutManager()
        setTVShowAdapter()
    }

    private fun setMovieAdapter() {
        binding.movieRecyclerView.adapter = movieAdapter
    }

    private fun setTVShowAdapter() {
        binding.tvShowRecyclerView.adapter = tvShowAdapter
    }

    private fun setMovieLayoutManager() {
        binding.movieRecyclerView.layoutManager = createGridLayoutManager()
    }

    private fun setTVShowLayoutManager() {
        binding.tvShowRecyclerView.layoutManager = createGridLayoutManager()
    }

    private fun createGridLayoutManager(): GridLayoutManager? {
        val manager = GridLayoutManager(context, 1)
        manager.orientation = RecyclerView.VERTICAL
        return manager
    }

    /**
     * Part 2 - Setup ViewModel and observe the different LiveData objects
     */

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        getViewModel()
        observeMoveList()
        observeTvShowList()
    }

    private fun getViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
    }

    private fun observeMoveList() {
        viewModel.getMovieList().observe(
            viewLifecycleOwner,
            Observer { movieList -> setMovieList(movieList) }
        )
    }

    private fun observeTvShowList() {
        viewModel.getTvShowList().observe(
            viewLifecycleOwner,
            Observer { tvShowList -> setTvShowList(tvShowList) }
        )
    }

    private fun setMovieList(movieList: List<Element>) {
        movieAdapter.setElementList(movieList)
    }

    private fun setTvShowList(tvShowList: List<Element>) {
        tvShowAdapter.setElementList(tvShowList)
    }

    /**
     * Part 3 - Setup behavior searchView for searching movies/TVShows
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem? = menu.findItem(R.id.action_search)

        val test: MenuItem? = menu.findItem(R.id.action_get_top_rated_elements)
        val searchView: SearchView? = searchItem?.actionView as SearchView

        test?.setOnMenuItemClickListener {
            viewModel.cleanQuery()
            searchView?.setQuery("", false)
            searchView?.onActionViewCollapsed();
            true
        }

        setOnActionExpandListener(searchItem, searchView)

        setOnQueryTextListener(searchView)
    }

    private fun setOnActionExpandListener(searchItem: MenuItem?, searchView: SearchView?) {

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            /**
             * onMenuItemActionExpand sets iconified to false and requests the focusFromTouch for the
             * searchView element. This is done for a better user experience, the searchView is expanded and
             * keyboard launched when the user clicks on the search icon
             */
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                searchView?.isIconified = false
                searchView?.requestFocusFromTouch()
                return true
            }

            /** onMenuItemActionCollapse clear the query (this is for usability purposes) and call the
             * cleanQuery method of ViewModel. See the method for more information about it.
             */
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                searchView?.setQuery("", false)
                viewModel.cleanQuery()
                return true
            }
        })
    }

    private fun setOnQueryTextListener(searchView: SearchView?) {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return handleQueryChange(s)
            }
        })
    }

    /**
     * The query is stored in the ViewModel and the corresponding search methods are called.
     * The adapters are updated with the result of the search methods.
     */
    private fun handleQueryChange(query: String): Boolean {
        updateAdapterElements(query)
        return false
    }

    private fun updateAdapterElements(query: String) {

        updateTVShowAdapterElements(query)
        updateMovieAdapterElements(query)
    }

    private fun updateTVShowAdapterElements(query: String) {
        viewModel.searchTVShow(query).observe(
            viewLifecycleOwner,
            Observer { setTvShowList(it) }
        )
    }

    private fun updateMovieAdapterElements(query: String) {
        viewModel.searchMovie(query).observe(
            viewLifecycleOwner,
            Observer { setMovieList(it) }
        )
    }

    /**
     * Part 4 - Override onClickListener from Element Adapter, get the clicked element and launch
     * the DetailFragment
     */

    /**
     * The detailElement (element we want to see the details) is obtained. Then DetailFragment is
     * launched using Navigation Component
     */
    override fun onItemClick(clickedItem: Int, type: String) {
        viewModel.setDetailElement(clickedItem, type)
        navigateToDrinkListFragment()
    }

    private fun navigateToDrinkListFragment() {

        val action =
            ListFragmentDirections.actionListFragmentToDetailFragment()

        NavHostFragment.findNavController(this).navigate(action)
    }
}
