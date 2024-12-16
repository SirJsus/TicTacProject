package com.jsus.tictacproject.ui.activities.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.Task
import com.jsus.tictacproject.code.objects.TextConfig
import com.jsus.tictacproject.databinding.SheetCreateTaskBinding

class CreateSheetTaskFragment(
    private val listener: NewTaskAdd,
    private val activityList: List<Activity>
): BottomSheetDialogFragment() {

    private var _binding: SheetCreateTaskBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<Activity>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetCreateTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerViewTimers()
        save()

        return root
    }

    private fun save(){
        val dbHelper = DBHelper(requireContext())
        with(binding){
            saveButton.setOnClickListener {
                val name = TextConfig(requireContext()).getInfo(nameTIET, nameTIL, 1)
                val desc = TextConfig(requireContext()).getInfo(descTIET, descTIL, 2)

                if (name != null) {
                    val new = Task().create(name, desc, dbHelper, list)

                    listener.addTask(new)
                    TextConfig(requireContext()).clearText(nameTIET)
                    TextConfig(requireContext()).clearText(descTIET)
                    dismiss()
                }
            }
        }
    }

    private fun recyclerViewTimers(){
        val adapter = TimerToTaskAdapter(activityList, list){ activity, add ->
            if (add) list.add(activity)
            else {
                val isHere = list.find { it.id == activity.id }
                if (isHere != null) list.remove(activity)
            }
            recyclerViewTimers()
            Log.d("tictac_CreateSheetTaskFragment", "recyclerViewTimers ${activity.name}: $add")
            Log.d("tictac_CreateSheetTaskFragment", "recyclerViewTimers list: $list")
        }
        with(binding){
            timerRv.layoutManager = GridLayoutManager(requireContext(), 2)
            timerRv.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}