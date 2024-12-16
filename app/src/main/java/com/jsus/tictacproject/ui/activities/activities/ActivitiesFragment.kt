package com.jsus.tictacproject.ui.activities.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.Task
import com.jsus.tictacproject.databinding.FragmentActivitiesBinding
import com.jsus.tictacproject.ui.activities.container.SharedViewModel
import com.jsus.tictacproject.ui.home.ActivityChange
import com.jsus.tictacproject.ui.home.TimerOnAdapter

class ActivitiesFragment : Fragment(), NewActivityAdd, ActivityChange {

    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentActivitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[ActivitiesViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        _binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        set()
        buttonOperation()

        sharedViewModel.eventFromFragment1.observe(viewLifecycleOwner){
            set()
        }

        return root
    }

    fun set(){
        //
        val dbHelper = DBHelper(requireContext())
        val itemList = Activity().getList(dbHelper)
        recyclerViewTimers(itemList, dbHelper)
        val now = Activity().getNow(dbHelper)
        val list = if (now != Activity()) mutableListOf(now)
                    else emptyList()
        val taskNow = Task().getNow(dbHelper)
        recyclerViewNow(list, taskNow, dbHelper)
    }

    private fun recyclerViewTimers(itemList: MutableList<Activity>, db: DBHelper){
        val adapter = TimerAdapter(itemList, db, this)
        with(binding){
            Log.d("tictac_ActivitiesFragment", "recyclerViewTimers, itemList: $itemList")
            val now = Activity().getNow(db)
            Log.d("tictac_ActivitiesFragment", "recyclerViewTimers, now: $now")
            if (now != Activity()) itemList.find { it.id == now.id }!!.timer.start = now.timer.start
            timerRv.layoutManager = GridLayoutManager(requireContext(), 2)
            timerRv.adapter = adapter
        }
    }

    private fun recyclerViewNow(list: List<Activity>, task: Task, dbHelper: DBHelper){
        val adapter = TimerOnAdapter(list, task, dbHelper, this)
        with(binding){
            timerOnRv.layoutManager = LinearLayoutManager(requireContext())
            timerOnRv.adapter = adapter
        }
    }

    private fun buttonOperation(){
        binding.addButton.setOnClickListener {
            val createSheetActivity = CreateSheetActivityFragment(this)
            createSheetActivity.show(parentFragmentManager, "CreateActivity")
        }
    }

    override fun addActivity(activity: Activity) {
        set()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun activityHasChange() {
        sharedViewModel.triggerEventFromFragment2()
        set()
    }
}