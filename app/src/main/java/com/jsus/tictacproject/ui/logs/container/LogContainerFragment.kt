package com.jsus.tictacproject.ui.logs.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jsus.tictacproject.databinding.FragmentLogContainerBinding

class LogContainerFragment: Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private var _binding: FragmentLogContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogContainerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewPager = binding.logContainerViewpager
        viewPager.adapter = LogPagerAdapter(requireContext(), childFragmentManager)

        tabLayout = binding.logContainerTablayout
        tabLayout.setupWithViewPager(viewPager)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}