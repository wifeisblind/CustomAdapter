package com.example.democustomadapter.customviewadapter


interface CustomViewAdapterHelperDelegate {
    val currentList: List<Any>
    fun submitList(list: MutableList<Any>)
}