package com.jsus.tictacproject.ui.activities.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.TextConfig
import com.jsus.tictacproject.databinding.SheetCreateActivityBinding

class CreateSheetActivityFragment(private val listener: newActivityAdd):BottomSheetDialogFragment() {

    private var _binding: SheetCreateActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SheetCreateActivityBinding.inflate(inflater, container, false)
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

                if (name != null){
                    val new = Activity().create(name, desc, dbHelper)
                    Log.d("tictac_CreateSheetActivityFragment", "save, new: $new")
                    listener.addActivity(new)
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