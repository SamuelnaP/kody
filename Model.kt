package com.example.nicek

import java.sql.Time

class Model{

    companion object Factory{ //https://kotlinlang.org/docs/tutorials/kotlin-for-py/objects-and-companion-objects.html
        fun createList(): Model = Model()
    }

    var UID: String? = null
    var itemDataText: String? = null
    var done: Boolean? = false
    var cas: Time? = null


}