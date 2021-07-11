package com.example.nicek

interface  Update{

    fun modifyItem(itemUID: String, isDone:Boolean)
    fun onItemDelete(itemUID: String)

}