package com.example.myapplicationfragments.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplicationfragments.ui.Page1Fragment
import com.example.myapplicationfragments.ui.Page2Fragment
import com.example.myapplicationfragments.ui.Page3Fragment

class PageAdapter(fa: FragmentActivity, private val mNumOfTabs: Int) :
    FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return mNumOfTabs
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Page1Fragment()
            1 -> Page2Fragment()
            2 -> Page3Fragment()
            else -> Page1Fragment()
        }
    }
}