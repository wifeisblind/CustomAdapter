package com.example.democustomadapter.customviewadapter


interface CustomViewAdapterHelperDelegate {
    val currentList: MutableList<Any>
    fun submitList(list: MutableList<Any>)
}