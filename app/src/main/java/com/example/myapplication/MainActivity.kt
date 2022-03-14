package com.example.myapplication

import android.animation.AnimatorSet
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val rappersNames = listOf(
        "7ari",
        "7liwa",
        "7toun",
        "8ird",
        "21tach",
        "777ym",
        "Abduh",
        "Ali Ssamid",
        "Anys",
        "Bo9al",
        "Dada",
        "Dizzy Dros",
        "Don Bigg",
        "Elgrande Toto",
        "Figoshin",
        "Khtek",
        "Lferda",
        "Moro",
        "Nores",
        "Saad Dsouli",
        "Shobee",
        "Small X",
        "Snor",
        "Tagne",
        "Tflow",
        "X7kira"
    )

    lateinit var adapter: CustomAdapter
    lateinit var data: ArrayList<RapperData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyApplication)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        //get fast scroller
        val fastScrollerView = findViewById<FastScrollerView>(R.id.fastscroller)

        //get search bar
        val sample_editText = findViewById<AppCompatEditText>(R.id.sample_editText)

        //get fast scroller thumb
        val fastScrollerThumbView = findViewById<FastScrollerThumbView>(R.id.fastscroller_thumb)


        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        var pop = AnimatorSet()

        // ArrayList of class ItemsViewModel
        data = fetchData(this, rappersNames)

        // This will pass the ArrayList to our Adapter
        adapter = CustomAdapter(this, pop, data, mediaPlayer)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter


        //setting the fast scroller
        fastScrollerView.setupWithRecyclerView(
            recyclerview,
            { position ->
                val item = data[position] // Get your model object
                // or fetch the section at [position] from your database
                FastScrollItemIndicator.Text(
                    item.name.replace("[^A-Za-z]".toRegex(), "#").substring(0, 1)
                        .toUpperCase() // Grab the first letter and capitalize it
                ) // Return a text indicator
            }
        )

        fastScrollerThumbView.setupWithFastScroller(fastScrollerView)

        sample_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    private fun fetchData(context: Context, rappersNames: List<String>) : ArrayList<RapperData> {

        val drawableFields: Array<Field> = R.drawable::class.java.fields
        val drawableLists = drawableFields.map { it.name }.toTypedArray()
        val rawFields: Array<Field> = R.raw::class.java.fields
        val rawLists = rawFields.map { it.name }.toTypedArray()
        var rapperBg : Int
        var rapperIc : Int
        var rapperAdlibs : MutableList<String>

        val rappersNamesOrdered = rappersNames.sortedBy { it.toString() }


        val rappersData = ArrayList<RapperData>()

        var rapperNameFiltered: String
        for (rapperName in rappersNamesOrdered) {
            //remove white space from rapperName
            rapperNameFiltered = rapperName.filterNot { it.isWhitespace() }.toLowerCase()
            Log.d("rapperName : ", rapperNameFiltered)
            rapperBg = context.resources.getIdentifier(
                drawableLists.filter { s -> s.endsWith("bg_" + rapperNameFiltered) }[0],
                "drawable",
                context.packageName
            )
            rapperIc = context.resources.getIdentifier(
                drawableLists.filter { s -> s.endsWith("ic_" + rapperNameFiltered) }[0],
                "drawable",
                context.packageName
            )
            rapperAdlibs = rawLists.filter { s -> s.endsWith(rapperNameFiltered)}.toMutableList()

            rappersData.add(RapperData(rapperName, rapperBg, rapperIc, rapperAdlibs))
        }

        return rappersData
    }

    private fun isNumber(s: String): Boolean {
        return when(s.toIntOrNull())
        {
            null -> false
            else -> true
        }
    }

    private fun filter(text: String) {

        val filteredCourseAry: ArrayList<RapperData> = ArrayList()

        val courseAry : ArrayList<RapperData> = data

        for (eachCourse in courseAry) {
            if (eachCourse.name!!.toLowerCase().contains(text.toLowerCase())) {
                filteredCourseAry.add(eachCourse)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filteredCourseAry);
    }
}
