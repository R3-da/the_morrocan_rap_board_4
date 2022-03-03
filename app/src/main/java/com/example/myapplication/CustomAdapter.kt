package com.example.myapplication

import android.animation.AnimatorInflater
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout
import androidx.core.view.marginBottom
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

        for(adlib in ItemsViewModel.adlibs) {
            val button = ImageButton(context)

            button.layoutParams = LinearLayout.LayoutParams(
                    context.resources.getDimension(R.dimen.rappers_icon_width).toInt(),
                    LinearLayout.LayoutParams.MATCH_PARENT, //change the parameters as whatever you want
                    1.0f
            )
            button.setImageResource(ItemsViewModel.ic)
            button.setOnClickListener {
                setFromXML(button,adlib)
            }
            button.scaleType = ImageView.ScaleType.FIT_CENTER
            button.adjustViewBounds = true
            button.background = null
            button.alpha = 0.7f
            button.setPadding(0,0,0,0)
            button.setBackgroundResource(0)

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

    }

    fun setFromXML(view: View, adlib: String) {
        val animator = AnimatorInflater.loadAnimator(context, R.animator.set)

        view.setOnClickListener(null)

        animator.apply {
            setTarget(view)
            start()
        }

        if(mediaPlayer != null) {
            mediaPlayer?.release()
        }
        var resId : Int = context.resources.getIdentifier(adlib,"raw", context.packageName);
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()

        view.setOnClickListener{
            animator.end()
            setFromXML(view,adlib)
        }
    }
}
