package com.jsus.tictacproject.ui.activities.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jsus.tictacproject.databinding.FragmentActivityContainerBinding

class ActivityContainerFragment: Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private var _binding: FragmentActivityContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityContainerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewPager = binding.activityContainerViewpager
        viewPager.adapter = ActivityPagerAdapter(requireContext(), childFragmentManager)

        tabLayout = binding.activityContainerTablayout
        tabLayout.setupWithViewPager(viewPager)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}