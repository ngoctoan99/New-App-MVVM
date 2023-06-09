package com.sanghm2.newapp.screen.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sanghm2.newapp.viewmodel.NewViewModel
import com.sanghm2.newapp.R
import com.sanghm2.newapp.adapter.NewAdapter
import com.sanghm2.newapp.screen.MainActivity
import com.sanghm2.newapp.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_new.*

class BreakingNewFragment : Fragment(R.layout.fragment_breaking_new) {

    lateinit var viewModel: NewViewModel
    private val TAG = "toan"
    lateinit var newAdapter : NewAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecylerView()

        newAdapter.setOnItemClickListener {
            val bundle =  Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_breakingNewFragment_to_articleFragment,bundle)
        }
        viewModel.breakingNew.observe(viewLifecycleOwner, Observer {  response ->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let { newResponse ->
                        newAdapter.differ.submitList(newResponse.articles)
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let { message->
                        Log.e(TAG,"An error occured: $message")
                        Snackbar.make(view, message,Snackbar.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setUpRecylerView(){
        newAdapter = NewAdapter()
        rvBreakingNews.apply {
            adapter = newAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}