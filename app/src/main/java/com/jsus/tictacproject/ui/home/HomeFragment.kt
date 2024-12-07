package com.jsus.tictacproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.TextConfig
import com.jsus.tictacproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        with(binding){
            val itemList = mutableListOf<Activity>()
            addButton.setOnClickListener {
                val name = TextConfig(requireContext()).getInfo(nameTIET, nameTIL, 1)
                val desc = TextConfig(requireContext()).getInfo(descTIET, descTIL, 2)

                if (name != null){
                    val new = Activity(itemList.size, name, desc)
                    itemList.add(new)
                }
                reciclerView(itemList)
            }


        }

        return root
    }

    fun reciclerView(itemList: List<Activity>){
        with(binding){
            val adapter = TimerAdapter(itemList)
            timerRv.layoutManager = LinearLayoutManager(requireContext())
            timerRv.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}