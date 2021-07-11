package com.example.nicek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() , Update {


    val currentTime = Calendar.getInstance()
    val timeToMatch = Calendar.getInstance()
    var hourToMatch = 0
    var minuteToMatch = 0



    lateinit var databaza: DatabaseReference
    var listPoznamok:MutableList<Model>? = null
    lateinit var adapter: Adapter
    private var listView : ListView?=null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.nicek.R.layout.activity_main)



        val fab = findViewById<View>(R.id.fab) as FloatingActionButton //https://developer.android.com/guide/topics/ui/look-and-feel

        listView=findViewById<ListView>(R.id.item_listView)

        databaza= FirebaseDatabase.getInstance().reference //https://firebase.google.com/?gclid=Cj0KCQjwxNT8BRD9ARIsAJ8S5xYSgAwqPojXEabngvEKm5GgEXrS_NJn2zY9KRtxTYyDTZ2ZKsgstgEaAjzZEALw_wcB

        fab.setOnClickListener { view ->
            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setTitle("Pridaj poznamku")
            alertDialog.setMessage("pridaj poz")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Add"){ dialog, i ->

                val todoItemData = Model.createList()
                todoItemData.itemDataText = textEditText.text.toString()
                todoItemData.done = false
                val newItemData = databaza.child("todo").push()
                todoItemData.UID = newItemData.key

                newItemData.setValue(todoItemData)

                dialog.dismiss()
                Toast.makeText(this,"poznamka pridana", Toast.LENGTH_LONG).show()
            }
            alertDialog.show()
        }

        listPoznamok= mutableListOf<Model>()
        adapter = Adapter(this, listPoznamok!!)
        listView!!.adapter=adapter
        databaza.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "No item added", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listPoznamok!!.clear()
                addItemToList(snapshot)
            }

        })


    }

    private fun addItemToList(snapshot: DataSnapshot) {

        val items = snapshot.children.iterator()

        if(items.hasNext()){
            val index = items.next()
            val iterator = index.children.iterator()
            while (iterator.hasNext()){
                val dalsi = iterator.next()
                val textData = Model.createList()
                val map = dalsi.getValue() as HashMap<String, Any>
                textData.UID = dalsi.key
                textData.done = map.get("done") as Boolean?
                textData.itemDataText = map.get("itemDataText") as String?
                listPoznamok!!.add(textData)
            }
        }

        adapter.notifyDataSetChanged()

    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference = databaza.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)
    }

    override fun onItemDelete(itemUID: String) {
        val itemReference = databaza.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }

}