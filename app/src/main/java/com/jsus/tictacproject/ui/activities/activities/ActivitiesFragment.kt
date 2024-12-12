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
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.databinding.FragmentActivitiesBinding

class ActivitiesFragment : Fragment(), NewActivityAdd {

    private var _binding: FragmentActivitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[ActivitiesViewModel::class.java]

        _binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        set()
        buttonOperation()

        return root
    }

    fun set(){
        //
        val dbHelper = DBHelper(requireContext())
        val itemList = Activity().getList(dbHelper)
        recyclerViewTimers(itemList, dbHelper)

    }

    private fun recyclerViewTimers(itemList: MutableList<Activity>, db: DBHelper){
        with(binding){
            Log.d("tictac_ActivitiesFragment", "recyclerViewTimers, itemList: $itemList")
            val now = db.getNow()
            Log.d("tictac_ActivitiesFragment", "recyclerViewTimers, now: $now")
            if (now != Activity()) itemList.find { it.id == now.id }!!.timer.start = now.timer.start
            val adapter = TimerAdapter(itemList, db)
            timerRv.layoutManager = GridLayoutManager(requireContext(), 5)
            timerRv.adapter = adapter
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
}