package com.project.mydeardiary.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.project.mydeardiary.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
// its a container for fragments and navigation

    private lateinit var navController: NavController //reference to NavController
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
    navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)
    }
    override fun onSupportNavigateUp(): Boolean { //when clicking Back
        return navController.navigateUp() || super.onSupportNavigateUp()
}
}

const val AddPostResultOk = Activity.RESULT_FIRST_USER
const val EditPostResultOk = Activity.RESULT_FIRST_USER + 1