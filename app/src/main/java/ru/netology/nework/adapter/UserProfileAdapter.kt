package ru.netology.nework.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nework.ui.EventsFragment
import ru.netology.nework.ui.JobsFragment
import ru.netology.nework.ui.WallFragment
import ru.netology.nework.ui.WallFragmentEvent

@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val itemCount = 3

    override fun getItemCount(): Int {
        return this.itemCount
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return WallFragment()
            1 -> return WallFragmentEvent()
            2 -> return JobsFragment()
        }
        return Fragment()
    }
}