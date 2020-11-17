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

    private val itemList: MutableLiveData<List<ItemData>> = MutableLiveData()

    fun getTestList(): LiveData<List<ItemData>> = itemList

    init {
        viewModelScope.launch {
            itemList.value = List(15) { index -> ItemData.create(index) }

            delay(5000)

            itemList.value = List(20) { index -> ItemData.create(index) }


            hasMore.value = false

        }
    }

    fun addFavorite(index: Int) = itemList.value?.let { list ->
        val muList = list.toMutableList()
        muList[index] = list[index].copy(isFavorite = list[index].isFavorite.not())
        itemList.value = muList
    }
}