package com.sanghm2.newapp.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sanghm2.newapp.NewRepository
import com.sanghm2.newapp.NewViewModel
import com.sanghm2.newapp.NewViewModelProviderFactory
import com.sanghm2.newapp.R
import com.sanghm2.newapp.database.ArticleDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

     lateinit var viewModel : NewViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = NewRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewViewModelProviderFactory(application,repository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[NewViewModel::class.java]
//        bottomNavigationView.setupWithNavController( newsNavHostFragment.findNavController())
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController= navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }
}