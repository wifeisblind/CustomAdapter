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
            testList.value = List(10) { index -> TestData("This is Test: $index") }

            delay(3000)

//            testList.value = List(15) { index -> TestData("This is Test: $index") }

//            delay(3000)

            hasMore.value = false
//            testList.value = List(20) { index -> TestData("This is Test: $index") }
        }
    }
}