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
        itemList.value = List(15) { index -> ItemData.create(index) }
    }


    fun addFavorite(index: Int) = itemList.value?.let { list ->
        val muList = list.toMutableList()
        muList[index] = list[index].copy(isFavorite = list[index].isFavorite.not())
        itemList.value = muList
    }

    fun loadMore() = viewModelScope.launch {
        delay(1_000)

        itemList.value = List(20) { index -> ItemData.create(index) }
        hasMore.value = false
    }
}