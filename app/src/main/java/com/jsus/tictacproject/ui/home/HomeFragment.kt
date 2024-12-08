package com.jsus.tictacproject.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.Register
import com.jsus.tictacproject.code.objects.TextConfig
import com.jsus.tictacproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val listRegister = mutableListOf<Register>()

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

            val itemList = mutableListOf(
                Activity(0, "Item 0", null),
                Activity(1, "Item 1", null),
                Activity(2, "Item 2", null),
                Activity(3, "Item 3", null),
                Activity(4, "Item 4", null)
            )
            recyclerViewTimers(itemList)
            addButton.setOnClickListener {
                val name = TextConfig(requireContext()).getInfo(nameTIET, nameTIL, 1)
                val desc = TextConfig(requireContext()).getInfo(descTIET, descTIL, 2)

                if (name != null){
                    val new = Activity(itemList.size, name, desc)
                    Log.d("tictac_HomeFragment", "onCreateView, new: $new")
                    itemList.add(new)

                    TextConfig(requireContext()).clearText(nameTIET)
                    TextConfig(requireContext()).clearText(descTIET)
                }
                recyclerViewTimers(itemList)
            }


        }

        return root
    }

    private fun recyclerViewTimers(itemList: List<Activity>){
        with(binding){
            Log.d("tictac_HomeFragment", "recyclerViewTimers, itemList: $itemList")
            val adapter = TimerAdapter(itemList){ newRegister ->
                Log.d("tictac_HomeFragment", "recyclerViewTimers, newRegister: $newRegister")
                listRegister.add(newRegister)
                recyclerViewRegister()
            }
            //timerRv.layoutManager = LinearLayoutManager(requireContext())
            timerRv.layoutManager = GridLayoutManager(requireContext(), 5)
            timerRv.adapter = adapter
        }
    }

    private fun recyclerViewRegister(){
        //
        with(binding){
            val adapter = RegisterAdapter(listRegister)
            registerRv.layoutManager = LinearLayoutManager(requireContext())
            registerRv.adapter = adapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}