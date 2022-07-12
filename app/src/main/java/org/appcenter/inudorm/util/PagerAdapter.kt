package org.appcenter.inudorm.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fa:FragmentActivity, private var fragments:ArrayList<Fragment>) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]

    fun addFragments(fragmentsToAdd: ArrayList<Fragment>) {
        fragments.addAll(fragmentsToAdd)
    }
}