package org.appcenter.inudorm.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fa:FragmentActivity, private var fragments:ArrayList<Fragment>) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]

    fun addFragment(fragmentToAdd: Fragment) {
        fragments.add(fragmentToAdd)
        notifyItemInserted(itemCount)
    }

    fun deleteFragment(position: Int) {
        fragments.removeAt(position)
        notifyItemRemoved(position)
    }
}