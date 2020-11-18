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

    private val horizontalList: MutableLiveData<List<HorizontalData>> = MutableLiveData()

    fun getHorizontalList(): LiveData<List<HorizontalData>> = horizontalList

    init {
        itemList.value = List(15) { index -> ItemData.create(index) }
//        horizontalList.value = List(10) {index -> HorizontalData.create(index) }
    }


    fun addFavorite(index: Int) = itemList.value?.let { list ->
        val muList = list.toMutableList()
        muList[index] = list[index].copy(isFavorite = list[index].isFavorite.not())
        itemList.value = muList
    }

    fun loadMore() = viewModelScope.launch {
        if (hasMore.value == true) {
            delay(1_000)
            itemList.value = List(20) { index -> ItemData.create(index) }
            hasMore.value = false
        }
    }

    fun delete(position: Int) {
        itemList.value = itemList.value?.toMutableList()?.apply {
            removeAt(position)
        }
    }
}