package com.sanghm2.newapp.screen.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sanghm2.newapp.viewmodel.NewViewModel
import com.sanghm2.newapp.R
import com.sanghm2.newapp.adapter.NewAdapter
import com.sanghm2.newapp.screen.MainActivity
import com.sanghm2.newapp.util.Constants
import com.sanghm2.newapp.util.Resource
import kotlinx.android.synthetic.main.fragment_search_new.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewFragment : Fragment(R.layout.fragment_search_new) {
    val TAG = "toan"
    private lateinit var viewModel: NewViewModel

    private lateinit var newAdapter : NewAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        newAdapter.setOnItemClickListener {
            val bundle =  Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_searchNewFragment_to_articleFragment,bundle)
        }
        var job: Job? = null
        etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_NEWS_DELAY)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.searchNew(it.toString())
                    }
                }
            }
        }

        viewModel.searchNew.observe(viewLifecycleOwner, Observer {  response->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let {
                        newAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG ,"An error occured $it")
                        Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }

        })
    }
    private fun hideProgressBar(){
    }
    private fun showProgressBar(){
    }
    private fun setUpRecyclerView() {
        newAdapter = NewAdapter()
        rvSearchNews.apply {
            adapter = newAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}