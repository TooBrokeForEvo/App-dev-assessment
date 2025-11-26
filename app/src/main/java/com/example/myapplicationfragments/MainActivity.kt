package com.example.myapplicationfragments

import PageAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_linear)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_page1))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_page2))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        val adapter = PageAdapter(this,2)
        viewPager.setAdapter(adapter)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Page " + (position + 1)
        }.attach()

    }

    override fun addMenuProvider(
        provider: MenuProvider,
        owner: androidx.lifecycle.LifecycleOwner,
        state: androidx.lifecycle.Lifecycle.State
    ) {
        TODO("Not yet implemented")
    }
    override fun removeMenuProvider(provider: MenuProvider) {
        TODO("Not yet implemented")
    }

}
