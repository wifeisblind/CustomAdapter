package com.example.democustomadapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val hasMore: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getHasMore(): LiveData<Boolean> = hasMore

    private val testList: MutableLiveData<List<TestData>> = MutableLiveData()

    fun getTestList(): LiveData<List<TestData>> = testList

    init {
        viewModelScope.launch {
            testList.value = List(1) { index -> TestData("This is Test: $index") }

            delay(5000)

            testList.value = List(2) { index -> TestData("This is Test: $index") }

//            delay(100)

            hasMore.value = false


//            delay(3000)
//            testList.value = List(20) { index -> TestData("This is Test: $index") }
        }
    }
}