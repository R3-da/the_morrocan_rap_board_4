package com.example.myapplication

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class CustomAdapter(private val context: Context, private val mList: ArrayList<RapperData>, private var mediaPlayer: MediaPlayer?) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.bg)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.name
        Log.d("test", ItemsViewModel.toString())
        for(adlib in ItemsViewModel.adlibs) {
//            val themeContext: ContextThemeWrapper = ContextThemeWrapper(context, R.style.buttonStyle)
            val lp = RelativeLayout.LayoutParams(holder.newStyle, null)
            var button : ImageButton = ImageButton(context)
            button.setImageResource(ItemsViewModel.ic)
            button.layoutParams = lp
            Log.d("test", "1")
            holder.rappersView.addView(button)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val rappersView : LinearLayout = itemView.findViewById(R.id.rappersFaces)
        val newStyle = ContextThemeWrapper(itemView.context, R.style.buttonStyle)
        val someTextView = ImageButton(ContextThemeWrapper(itemView.context, R.style.buttonStyle))
    }
}
