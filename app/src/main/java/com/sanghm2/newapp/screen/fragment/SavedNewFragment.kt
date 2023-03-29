package com.sanghm2.newapp.screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sanghm2.newapp.viewmodel.NewViewModel
import com.sanghm2.newapp.R
import com.sanghm2.newapp.adapter.NewAdapter
import com.sanghm2.newapp.screen.MainActivity
import kotlinx.android.synthetic.main.fragment_saved_new.*


class SavedNewFragment : Fragment(R.layout.fragment_saved_new) {

    private lateinit var viewModel: NewViewModel
    lateinit var newAdapter: NewAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        newAdapter.setOnItemClickListener {
            var bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_savedNewFragment_to_articleFragment,bundle)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted article",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }
        viewModel.getSaveNews().observe(viewLifecycleOwner, Observer {  articles->
            newAdapter.differ.submitList(articles)
        })
    }

    private fun setUpRecyclerView() {
        newAdapter = NewAdapter()
        rvSavedNews.apply {
            adapter = newAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}