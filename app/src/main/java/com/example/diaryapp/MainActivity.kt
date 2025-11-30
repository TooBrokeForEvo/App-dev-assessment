package com.example.diaryapp

import com.example.diaryapp.adapter.PageAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * The main and only Activity in this application.
 * It hosts a Toolbar, a TabLayout, and a ViewPager2 to manage the navigation
 * between the three main fragments of the app.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Called when the activity is first created. This is where you should do all of
     * your normal static set up: create views, bind data to lists, etc.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_page1))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_page2))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_page3))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        val adapter = PageAdapter(this,3)
        viewPager.setAdapter(adapter)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "Select Date"
                1 -> tab.text = "Diary Entry"
                2 -> tab.text = "View Entries"
            }
        }.attach()

    }

}
