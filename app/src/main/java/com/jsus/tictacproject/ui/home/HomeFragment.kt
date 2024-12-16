package com.jsus.tictacproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), ActivityChange {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        set()

        return root
    }

    fun set(){
        val dbHelper = DBHelper(requireContext())
        val now = dbHelper.getNowActivity()
        val list = if (now != Activity()) mutableListOf(now)
                    else emptyList()
        recyclerViewNow(list, dbHelper)
    }

    private fun recyclerViewNow(list: List<Activity>, dbHelper: DBHelper){
        val adapter = TimerOnAdapter(list, dbHelper, this)
        with(binding){
            timerOnRv.layoutManager = LinearLayoutManager(requireContext())
            timerOnRv.adapter = adapter
        }
    }

    override fun activityHasChange() {
        set()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}