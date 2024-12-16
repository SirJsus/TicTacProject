package com.jsus.tictacproject.ui.activities.container

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    // LiveData para eventos del Fragment 1
    val eventFromFragment1 = MutableLiveData<Unit>()

    // LiveData para eventos del Fragment 2
    val eventFromFragment2 = MutableLiveData<Unit>()

    fun triggerEventFromFragment1() {
        eventFromFragment1.value = Unit
    }

    fun triggerEventFromFragment2() {
        eventFromFragment2.value = Unit
    }
}