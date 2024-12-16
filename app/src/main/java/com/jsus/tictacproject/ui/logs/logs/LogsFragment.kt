package com.jsus.tictacproject.ui.logs.logs

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
import com.jsus.tictacproject.code.objects.Register
import com.jsus.tictacproject.databinding.FragmentLogsBinding

class LogsFragment : Fragment() {

    private var _binding: FragmentLogsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[LogsViewModel::class.java]

        _binding = FragmentLogsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        set()

        return root
    }

    fun set(){
        val dbHelper = DBHelper(requireContext())
        val listRegister = dbHelper.getRegisterList().asReversed()
        recyclerViewRegister(listRegister)
        val now = Activity().getNow(dbHelper)
        val list = if (now != Activity()) mutableListOf(now)
                    else emptyList()
        recyclerViewNow(list)
    }

    private fun recyclerViewRegister(list: List<Register>){
        //
        with(binding){
            val adapter = RegisterAdapter(list)
            registerRv.layoutManager = LinearLayoutManager(requireContext())
            registerRv.adapter = adapter
        }
    }

    private fun recyclerViewNow(list: List<Activity>){
        //
        with(binding){
            val adapter = NowAdapter(list)
            nowRv.layoutManager = LinearLayoutManager(requireContext())
            nowRv.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}