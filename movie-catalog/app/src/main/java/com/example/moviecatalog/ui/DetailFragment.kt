package com.example.moviecatalog.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalog.R
import com.example.moviecatalog.databinding.FragmentDetailBinding
import com.example.moviecatalog.model.Element
import com.example.moviecatalog.viewmodel.ViewModel
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {

    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupDataBinding(inflater, container)

        return getRoot()
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
    }
    private fun getRoot(): View {
        return binding.root
    }

    private fun setupListener() {
        binding.posterDetails.toggleButton.setOnCheckedChangeListener { _, b ->
            setWatchListStatus(b)
        }
    }

    private fun setWatchListStatus(boolean: Boolean) {
        viewModel.setWatchListStatus(boolean)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupListener()
    }

    private fun setupViewModel() {
        getViewModel()
        observeDetailElement()
    }

    private fun getViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
    }

    private fun observeDetailElement() {
        viewModel.getDetailElement().observe(
            viewLifecycleOwner,
            Observer { updateUI(it) }
        )
    }

    private fun updateUI(element: Element?) {
        setName(element?.name)
        setOverview(element?.overview)
        setPoster(element?.getPosterPathURL())
        setVoteAverage(element?.voteAverage)
        setToggleButtonClickStatus(element?.watchList)
    }

    private fun setName(name: String?) {
        binding.name.text = name
    }

    private fun setOverview(overview: String?) {
        binding.overview.text = overview
    }

    private fun setPoster(posterPath: String?) {
        Picasso.get().load(posterPath).into(binding.posterDetails.poster)
    }

    private fun setVoteAverage(voteAverage: String?) {
        val voteAverageText = "Vote average: $voteAverage"
        binding.voteAverage.text = voteAverageText
    }

    private fun setToggleButtonClickStatus(status: Boolean?) {
        status?.let {
            binding.posterDetails.toggleButton.isChecked = status
        }
    }
}
