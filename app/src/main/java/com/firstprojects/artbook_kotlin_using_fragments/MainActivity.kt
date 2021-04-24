package com.firstprojects.artbook_kotlin_using_fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.firstprojects.artbook_kotlin_using_fragments.fragments.TheFragmentForMainScreenAndRecyclerViewDirections

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}