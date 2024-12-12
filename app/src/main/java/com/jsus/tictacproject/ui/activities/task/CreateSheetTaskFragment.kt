package com.jsus.tictacproject.ui.activities.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Task
import com.jsus.tictacproject.code.objects.TextConfig
import com.jsus.tictacproject.databinding.SheetCreateTaskBinding

class CreateSheetTaskFragment(private val listener: NewTaskAdd
): BottomSheetDialogFragment() {

    private var _binding: SheetCreateTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetCreateTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        save()

        return root
    }

    fun save(){
        val dbHelper = DBHelper(requireContext())
        with(binding){
            saveButton.setOnClickListener {
                val name = TextConfig(requireContext()).getInfo(nameTIET, nameTIL, 1)
                val desc = TextConfig(requireContext()).getInfo(descTIET, descTIL, 2)

                if (name != null) {
                    val new = Task().create(name, desc, dbHelper)
                    listener.addTask(new)
                    TextConfig(requireContext()).clearText(nameTIET)
                    TextConfig(requireContext()).clearText(descTIET)
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}