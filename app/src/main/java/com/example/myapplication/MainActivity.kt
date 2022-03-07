package com.example.myapplication

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.media.MediaPlayer
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val rappersNames = listOf("Figoshin", "Dizzy Dros","Don Bigg", "Tagne", "Khtek", "7liwa", "Elgrande Toto")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyApplication)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        //get fast scroller
        val fastScrollerView = findViewById<FastScrollerView>(R.id.fastscroller)

        //get fast scroller thumb
        val fastScrollerThumbView = findViewById<FastScrollerThumbView>(R.id.fastscroller_thumb)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        var pop = AnimatorSet()

        // ArrayList of class ItemsViewModel
        val data = fetchData(this ,rappersNames)

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(this,pop,data,mediaPlayer)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        //setting the fast scroller
        fastScrollerView.setupWithRecyclerView(
                recyclerview,
                { position ->
                    val item = data[position] // Get your model object
                    // or fetch the section at [position] from your database
                    FastScrollItemIndicator.Text(
                            item.name.substring(0, 1).toUpperCase() // Grab the first letter and capitalize it
                    ) // Return a text indicator
                }
        )

        fastScrollerThumbView.setupWithFastScroller(fastScrollerView)

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
