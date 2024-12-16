package com.jsus.tictacproject.ui.activities.container

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jsus.tictacproject.R
import com.jsus.tictacproject.ui.activities.activities.ActivitiesFragment
import com.jsus.tictacproject.ui.activities.task.TaskFragment

private val TAB_TITTLE = arrayOf(
    R.string.activity_activity,
    R.string.activity_task
)

class ActivityPagerAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> ActivitiesFragment()
            1 -> TaskFragment()
            else -> throw IllegalArgumentException ("Invalid Position $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITTLE[position])
    }

    override fun getCount(): Int {
        return TAB_TITTLE.size
    }
}