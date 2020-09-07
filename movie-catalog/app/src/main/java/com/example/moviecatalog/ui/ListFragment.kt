package com.example.moviecatalog.ui

import android.content.res.Configuration
import android.os.Bundle
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
import com.example.moviecatalog.adapter.ElementAdapter.Companion.WATCHLIST
import com.example.moviecatalog.databinding.FragmentListBinding
import com.example.moviecatalog.model.Element
import com.example.moviecatalog.viewmodel.ViewModel

/**
 * The class can be divided into 4 pieces according to its logic:
 *  - Setup DataBinding and RecyclerViews
 *  - Setup ViewModel and observe the different LiveData objects
 *  - Setup behavior searchView for searching movies/TVShows
 *  - Override onClickListener from Element Adapter, get the clicked element and launch
 *  DetailFragment
 *
 *  In order to provide an smooth user experience we modify the elements available on ListFragment:
 *
 * - Adapters:
 *      - leftAdapter
 *      - RightAdapter
 *
 *      Each adapter has a type. This is used by the ViewModel in order to access to the correct
 *      information when an specific element is clicked. See setDetailElement in ViewModel for more
 *      details. The possible values are (defined in ElementAdapter):
 *          - MOVIE
 *          - TVSHOW
 *          - WATCHLIST
 *
 *  - Adapter labels
 *      - leftAdapter
 *      - rightAdapter
 *
 * These elements are changed in the following situations:
 *
 *  - Application is launched or no search performed:
 *      - leftAdapter -> elementList (combination of Movies and TVShows). type = MOVIE
 *      - rightAdapter -> watchList. type = WATCHLIST
 *
 *      - leftLabel -> Movies/TV Show
 *      - rightLabel -> WatchList
 *
 *  - Search is performed:
 *      - leftAdapter -> movieSearchList. type = MOVIE
 *      - rightAdapter -> tvShowSearchList. type = TVSHOW
 *
 *      - leftLabel -> Movies
 *      - rightLabel -> TV Show
 */

class ListFragment : Fragment(), ElementAdapter.OnClickElementInterface {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ViewModel
    private var leftAdapter = ElementAdapter(this, MOVIE)
    private var rightAdapter = ElementAdapter(this, WATCHLIST)

    /**
     * Part 1 - Setup DataBinding and RecyclerViews
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
        setupLeftRecyclerView()
        setupRightRecyclerView()
    }

    private fun setupLeftRecyclerView() {
        setElementLayoutManager()
        setElementAdapter()
    }

    private fun setupRightRecyclerView() {
        setWatchListLayoutManager()
        setWatchListAdapter()
    }

    private fun setElementAdapter() {
        binding.leftRecyclerView.adapter = leftAdapter
    }

    private fun setWatchListAdapter() {
        binding.rightRecyclerView.adapter = rightAdapter
    }

    private fun setElementLayoutManager() {
        binding.leftRecyclerView.layoutManager = createGridLayoutManager()
    }

    private fun setWatchListLayoutManager() {
        binding.rightRecyclerView.layoutManager = createGridLayoutManager()
    }

    private fun createGridLayoutManager(): GridLayoutManager? {
        val manager = GridLayoutManager(context, getSpanCount())
        manager.orientation = RecyclerView.VERTICAL
        return manager
    }

    private fun getSpanCount(): Int {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            1
        } else {
            2
        }
    }

    /**
     * Part 2 - Setup ViewModel and observe the different LiveData objects
     */

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
    }

    private fun getViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
    }

    private fun setupViewModel() {
        getViewModel()
        observeElementList()
        observeWatchList()
    }

    private fun observeElementList() {
        viewModel.getElementList().observe(
            viewLifecycleOwner,
            Observer { elementList -> setElementList(elementList) }
        )
    }

    private fun observeWatchList() {
        viewModel.getWatchList().observe(
            viewLifecycleOwner,
            Observer { elementList -> setWatchList(elementList) }
        )
    }

    private fun setElementList(elementList: List<Element>) {
        leftAdapter.setElementList(elementList)
    }

    private fun setWatchList(elementList: List<Element>) {
        rightAdapter.setElementList(elementList)
    }

    private fun setMovieList(movieList: List<Element>) {
        leftAdapter.setElementList(movieList)
    }

    private fun setTvShowList(tvShowList: List<Element>) {
        rightAdapter.setElementList(tvShowList)
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

        val searchView: SearchView? = searchItem?.actionView as SearchView

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
                setRegularTitlesAndUpdateRightAdapterType()
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
                return handleQueryChangeAndUpdateUI(s)
            }
        })
    }

    /**
     * The query is stored in the ViewModel and the corresponding search methods are called.
     * The adapters are updated with the result of the search methods.
     */
    private fun handleQueryChangeAndUpdateUI(query: String): Boolean {
        updateAdapterElements(query)
        setSearchTitlesAndUpdateRightAdapterType()
        return false
    }

    /*
     * During a search the recyclerview label are changed as follows:
     * - leftLabel = Movies
     * - RightLabel = TV Shows
     */

    private fun setSearchTitlesAndUpdateRightAdapterType() {
        setSearchTitles()
        setRightAdapterType(TVSHOW)
    }

    private fun setSearchTitles() {
        val isLeftLabelRegularTitle = (binding.leftRecyclerViewLabel.text == getString(R.string.moviesAndTVShowsLabel))
        val isRightLabelRegularTitle = (binding.rightRecyclerViewLabel.text == getString(R.string.watchListLabel))

        if (isLeftLabelRegularTitle && isRightLabelRegularTitle) {
            setSearchUI()
        }
    }

    private fun setSearchUI() {
        setLeftLabelText(R.string.moviesLabel)
        setRightLabelText(R.string.tvShowsLabel)
        setRightAdapterType(TVSHOW)
    }

    private fun setLeftLabelText(textId: Int) {
        binding.leftRecyclerViewLabel.text = getString(textId)
    }

    private fun setRightLabelText(textId: Int) {
        binding.rightRecyclerViewLabel.text = getString(textId)
    }

    /*
    * The recyclerview labels in a non-search situation are set as follows:
    * - leftLabel = Movies
    * - RightLabel = TV Shows
    */

    private fun setRegularTitlesAndUpdateRightAdapterType() {
        setRegularTitles()
        setRightAdapterType(WATCHLIST)
        cleanViewModelQuery()
    }

    private fun setRegularTitles() {
        setLeftLabelText(R.string.moviesAndTVShowsLabel)
        setRightLabelText(R.string.watchListLabel)
    }

    private fun updateAdapterElements(query: String) {
        updateRightAdapterSearchShowList(query)
        updateLeftAdapterMovieList(query)
    }

    private fun updateRightAdapterSearchShowList(query: String) {
        viewModel.searchTVShow(query).observe(
            viewLifecycleOwner,
            Observer { setTvShowList(it) }
        )
    }

    private fun updateLeftAdapterMovieList(query: String) {
        viewModel.searchMovie(query).observe(
            viewLifecycleOwner,
            Observer { setMovieList(it) }
        )
    }

    private fun setRightAdapterType(type: String) {
        rightAdapter.type = type
    }

    /**
     * Part 4 - Override onClickListener from Element Adapter, get the clicked element and launch
     * the DetailFragment
     */

    /*
     * The detailElement (element we want to see the details) is obtained. Then DetailFragment is
     * launched using Navigation Component and some settings are cleaned
     */
    override fun onItemClick(clickedItem: Int, type: String) {
        setDetailElement(clickedItem, type)
        navigateToDrinkListFragment()
        cleanFragmentAfterNavigating()
    }

    private fun setDetailElement(clickedItem: Int, type: String) {
        viewModel.setDetailElement(clickedItem, type)
    }

    private fun cleanViewModelQuery() {
        viewModel.cleanQuery()
    }

    private fun cleanFragmentAfterNavigating() {
        cleanViewModelQuery()
        setRightAdapterType(WATCHLIST)
    }

    private fun navigateToDrinkListFragment() {

        val action =
            ListFragmentDirections.actionListFragmentToDetailFragment()

        NavHostFragment.findNavController(this).navigate(action)
    }
}
