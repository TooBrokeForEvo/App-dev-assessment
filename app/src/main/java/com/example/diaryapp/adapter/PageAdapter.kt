package com.example.diaryapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.diaryapp.ui.Page1Fragment
import com.example.diaryapp.ui.Page2Fragment
import com.example.diaryapp.ui.Page3Fragment

/**
 * A [FragmentStateAdapter] that provides the fragments for the ViewPager2.
 * This adapter is responsible for creating and managing the fragments for each tab.
 *
 * @param fa The FragmentActivity that will host this adapter.
 * @param mNumOfTabs The total number of tabs.
 */
class PageAdapter(fa: FragmentActivity, private val mNumOfTabs: Int) :
    FragmentStateAdapter(fa) {

    /**
     * Returns the total number of items (pages/fragments) in the adapter.
     */
    override fun getItemCount(): Int {
        return mNumOfTabs
    }

    /**
     * Creates and returns the appropriate fragment for a given position.
     *
     * @param position The position of the tab.
     * @return The [Fragment] to be displayed at the given position.
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Page1Fragment()
            1 -> Page2Fragment()
            2 -> Page3Fragment()
            else -> Page1Fragment()
        }
    }
}