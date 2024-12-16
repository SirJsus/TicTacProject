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
import com.jsus.tictacproject.ui.activities.container.SharedViewModel
import com.jsus.tictacproject.ui.home.ActivityChange
import com.jsus.tictacproject.ui.home.TimerOnAdapter

private lateinit var activityList: MutableList<Activity>
private lateinit var taskList: MutableList<Task>
private lateinit var dbHelper: DBHelper

class TaskFragment: Fragment(), NewTaskAdd, ActivityChange {
    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[TaskViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner){
            textView.text = it
        }

        set()
        buttonOperation()

        sharedViewModel.eventFromFragment2.observe(viewLifecycleOwner){
            set()
        }

        return root
    }

    fun set(){
        db()
        recyclerViewTask()


    }

    fun db(){
        dbHelper = DBHelper(requireContext())
        activityList = Activity().getList(dbHelper)
        val nowTask = Task().getNow(dbHelper)
        val nowActivity = Activity().getNow(dbHelper)
        val list = if (nowActivity != Activity()) mutableListOf(nowActivity)
                    else emptyList()
        recyclerViewNow(list, nowTask, dbHelper)
        taskList = Task().getList(dbHelper)
        taskList.add(0, Task())
        if (nowTask != Task()) {
            taskList.remove(nowTask)
            taskList.remove(Task())
            if (nowActivity != Activity())
                nowTask.listActivity.find { nowActivity.id == it.id }!!.timer.start = nowActivity.timer.start
            taskList.add(0, nowTask)
        }
    }

    private fun recyclerViewTask(){
        val adapter = TaskAdapter(taskList, dbHelper, this)
        with(binding){
            taskRv.layoutManager = LinearLayoutManager(requireContext())
            taskRv.adapter = adapter
        }
    }

    private fun recyclerViewNow(list: List<Activity>, task: Task, dbHelper: DBHelper){
        val adapter = TimerOnAdapter(list, task, dbHelper, this)
        with(binding){
            timerOnRv.layoutManager = LinearLayoutManager(requireContext())
            timerOnRv.adapter = adapter
        }
    }

    fun buttonOperation(){
        binding.addButton.setOnClickListener {
            db()
            val createSheetTask = CreateSheetTaskFragment(this, activityList, dbHelper)
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

    override fun activityHasChange() {
        sharedViewModel.triggerEventFromFragment1()
        set()
    }
}