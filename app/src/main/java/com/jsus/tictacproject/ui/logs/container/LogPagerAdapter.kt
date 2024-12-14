package com.jsus.tictacproject.ui.logs.container

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jsus.tictacproject.R
import com.jsus.tictacproject.ui.logs.graph.GraphFragment
import com.jsus.tictacproject.ui.logs.logs.LogsFragment

private val TAB_TITTLE = arrayOf(
    R.string.logs_logs,
    R.string.logs_graph
)

class LogPagerAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> LogsFragment()
            1 -> GraphFragment()
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