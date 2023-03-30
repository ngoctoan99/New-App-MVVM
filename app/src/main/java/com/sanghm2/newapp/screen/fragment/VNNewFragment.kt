package com.sanghm2.newapp.screen.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sanghm2.newapp.R
import com.sanghm2.newapp.adapter.NewAdapter
import com.sanghm2.newapp.screen.MainActivity
import com.sanghm2.newapp.util.Resource
import com.sanghm2.newapp.viewmodel.NewViewModel
import kotlinx.android.synthetic.main.fragment_breaking_new.*
import kotlinx.android.synthetic.main.fragment_v_n_new.*


class VNNewFragment : Fragment(R.layout.fragment_v_n_new) {
    lateinit var viewModel : NewViewModel
    lateinit var newAdapter: NewAdapter
    val TAG = "toan"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()
        newAdapter.setOnItemClickListener {
            val bundle  = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_VNNewFragment_to_articleFragment,bundle)
        }

        viewModel.VNNew.observe(viewLifecycleOwner, Observer { response ->
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
                        Snackbar.make(view,it,Snackbar.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                    Log.d(TAG , "VNNew Loading")
                }
            }
        })
    }
    private fun hideProgressBar(){
        VNNewProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar(){
        VNNewProgressBar.visibility = View.VISIBLE
    }
    private fun setUpRecyclerView() {
        newAdapter = NewAdapter()
        rvVNNew.apply {
            adapter = newAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}