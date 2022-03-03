package com.example.myapplication

import android.animation.AnimatorInflater
import android.content.Context
import android.media.MediaPlayer
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val rappersNames = listOf("Figoshin", "Dizzy Dros","Don Bigg", "Tagne", "Khtek", "7liwa", "ElgrandeToto")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        val animator = AnimatorInflater.loadAnimator(this, R.animator.set)

        // ArrayList of class ItemsViewModel
        val data = fetchData(this ,rappersNames)

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(this,animator,data,mediaPlayer)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

    }

    private fun fetchData(context: Context, rappersNames : List<String>) : ArrayList<RapperData> {

        val drawableFields: Array<Field> = R.drawable::class.java.fields
        val drawableLists = drawableFields.map { it.name }.toTypedArray()
        val rawFields: Array<Field> = R.raw::class.java.fields
        val rawLists = rawFields.map { it.name }.toTypedArray()
        var rapperBg : Int
        var rapperIc : Int
        var rapperAdlibs : MutableList<String>


        val rappersData = ArrayList<RapperData>()

        var rapperNameFiltered: String
        for (rapperName in rappersNames) {
            //remove white space from rapperName
            rapperNameFiltered = rapperName.filterNot { it.isWhitespace() }.toLowerCase()

            rapperBg = context.resources.getIdentifier(drawableLists.filter { s -> s.endsWith("bg_"+rapperNameFiltered) }[0],"drawable", context.packageName)
            rapperIc = context.resources.getIdentifier(drawableLists.filter { s -> s.endsWith("ic_"+rapperNameFiltered) }[0],"drawable", context.packageName)
            rapperAdlibs = rawLists.filter { s -> s.endsWith(rapperNameFiltered)}.toMutableList()

            rappersData.add(RapperData(rapperName,rapperBg,rapperIc,rapperAdlibs))
        }

        return rappersData
    }
}
