package com.jsus.tictacproject.ui.logs.graph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GraphViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Graph Fragment"
    }
    val text: LiveData<String> = _text
}