package com.jsus.tictacproject.ui.activities.task

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
import com.jsus.tictacproject.code.objects.Task
import com.jsus.tictacproject.databinding.FragmentTaskBinding
import com.jsus.tictacproject.ui.home.TimerOnAdapter

class TaskFragment: Fragment(), NewTaskAdd {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner){
            textView.text = it
        }

        set()
        buttonOperation()

        return root
    }

    fun set(){
        val dbHelper = DBHelper(requireContext())
        val itemList = dbHelper.getTaskList()
        recyclerViewTask(itemList, dbHelper)
    }

    private fun recyclerViewTask(list: List<Task>, dbHelper: DBHelper){
        val adapter = TaskAdapter(list)
        with(binding){
            taskRv.layoutManager = LinearLayoutManager(requireContext())
            taskRv.adapter = adapter
        }
    }

    fun buttonOperation(){
        binding.addButton.setOnClickListener {
            val createSheetTask = CreateSheetTaskFragment(this)
            createSheetTask.show(parentFragmentManager, "createTask")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun addTask(task: Task) {
        set()
    }
}