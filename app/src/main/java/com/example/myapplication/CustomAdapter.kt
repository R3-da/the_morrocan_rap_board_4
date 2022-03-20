package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class CustomAdapter(private val context: Context, private var pop: AnimatorSet, private var rappersData: ArrayList<RapperData>, private var mediaPlayer: MediaPlayer?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view_design, parent, false)
        return itemHolder(view)

    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as itemHolder
            holder.rappersView.removeAllViews()
            val rapperData = rappersData[position]

            // sets the image to the imageview from our itemHolder class
            holder.imageView.setImageResource(rapperData.bg)

            // sets the text to the textview from our itemHolder class
            holder.textView.text = rapperData.name

            for(i in 0..3) {
                val rapperButton = ImageButton(context)

                rapperButton.setImageResource(rapperData.ic)
                rapperButton.scaleType = ImageView.ScaleType.FIT_CENTER
                rapperButton.adjustViewBounds = true
                rapperButton.background = null
                rapperButton.setPadding(0, 0, 0, 0)
                rapperButton.setBackgroundResource(0)

                if(i <= (rapperData.adlibs.size - 1)) {
                    val adlib = rapperData.adlibs[i]


                    rapperButton.layoutParams = LayoutParams(
                            context.resources.getDimension(R.dimen.rappers_icon_width).toInt(),
                            LayoutParams.MATCH_PARENT, //change the parameters as whatever you want
                            1.0f
                    )

                    val param = rapperButton.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(10, 10, 10, 10)
                    rapperButton.layoutParams = param
                    rapperButton.setBackgroundResource(0)

                    rapperButton.setOnClickListener {
                        pop = setFromXML(rapperButton, adlib)
                    }
                    rapperButton.alpha = 0.7f

                    holder.rappersView.addView(rapperButton)
                }
                else {
                    rapperButton.layoutParams = LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT, //change the parameters as whatever you want
                    )

                    rapperButton.setColorFilter(Color.parseColor("white"))
                    rapperButton.alpha = 0.4f
                    val textView = TextView(context)
                    val frameLayout = FrameLayout(context)
                    frameLayout.addView(rapperButton)
                    frameLayout.layoutParams = LayoutParams(
                            context.resources.getDimension(R.dimen.rappers_icon_width).toInt(),
                            LayoutParams.MATCH_PARENT, //change the parameters as whatever you want
                            1.0f
                    )
                    frameLayout.setPadding(0, 0, 0, 0)
                    val param = frameLayout.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(10, 10, 10, 10)
                    frameLayout.layoutParams = param
                    textView.textSize = context.resources.getDimension(R.dimen.rappers_icon_width).toFloat()
                    textView.text = "?"
                    textView.setTextColor(Color.parseColor("#F8C63E"))
                    frameLayout.addView(textView)
                    textView.gravity = Gravity.CENTER

                    holder.rappersView.addView(frameLayout)
                }
            }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return rappersData.size
    }

    // To get the data to search Category
    fun filterList(filteredCourseList: ArrayList<RapperData>) {
        this.rappersData = filteredCourseList;
        notifyDataSetChanged();
    }

    // Holds the views for adding it to image and text
    class itemHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val rappersView : LinearLayout = itemView.findViewById(R.id.rappersFaces)

    }

    fun setFromXML(view: View, adlib: String) : AnimatorSet{
        view.setOnClickListener(null)

        if(mediaPlayer != null) {
            mediaPlayer?.release()
        }
        var resId : Int = context.resources.getIdentifier(adlib, "raw", context.packageName);
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()
        var animDuration = mediaPlayer?.duration!!.toLong()
        pop = popAnim(animDuration, view, pop)

        view.setOnClickListener{
            setFromXML(view, adlib)
        }
        return pop
    }

    private fun popAnim(pause_duration: Long, view: View, pop: AnimatorSet) : AnimatorSet{
        pop.end()
        val popOutZ = ObjectAnimator.ofFloat(view, "translationZ", 1f)
        val popOutX = setAnim(view, "scaleX", 1f, 1.1f, 400)
        popOutX.interpolator = OvershootInterpolator()
        val popOutY = setAnim(view, "scaleY", 1f, 1.1f, 400)
        popOutY.interpolator = OvershootInterpolator()
        val popOutAlpha = setAnim(view, "Alpha", 0.7f, 1f, 50)

        val popOffX = setAnim(view, "scaleX", 1.1f, 1f, 200)
        popOffX.interpolator = AccelerateInterpolator()
        val popOffY = setAnim(view, "scaleY", 1.1f, 1f, 200)
        popOffY.interpolator = AccelerateInterpolator()
        val popOffAlpha = setAnim(view, "Alpha", 1f, 0.7f, 250)
        val popOffZ = ObjectAnimator.ofFloat(view, "translationZ", 0f)

        val pop = AnimatorSet()
        pop.play(popOutX).with(popOutY).with(popOutAlpha)
        pop.play(popOutZ).before(popOffX)


        pop.play(popOffX).with(popOffY).with(popOffAlpha).after(pause_duration)
        pop.play(popOffZ).after(popOffX)

        pop.play(popOutX).before(popOffX)
        pop.start()

        return pop
    }

    private fun setAnim(view: View, propName: String, valueFrom: Float, valueTo: Float, duration: Long) : ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(view, propName, valueFrom, valueTo)
        animator.duration = duration
        return animator
    }

}
