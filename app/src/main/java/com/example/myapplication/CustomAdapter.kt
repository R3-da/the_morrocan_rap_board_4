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
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class CustomAdapter(private val context: Context, private var pop: AnimatorSet, private var mList: ArrayList<RapperData>, private var mediaPlayer: MediaPlayer?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



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
            val ItemsViewModel = mList[position]

            // sets the image to the imageview from our itemHolder class
            holder.imageView.setImageResource(ItemsViewModel.bg)

            // sets the text to the textview from our itemHolder class
            holder.textView.text = ItemsViewModel.name

            for(i in 0..3) {
                val button = ImageButton(context)


                if(i <= (ItemsViewModel.adlibs.size - 1)) {
                    button.layoutParams = LayoutParams(
                            context.resources.getDimension(R.dimen.rappers_icon_width).toInt(),
                            LayoutParams.MATCH_PARENT, //change the parameters as whatever you want
                            1.0f
                    )

                    button.setImageResource(ItemsViewModel.ic)
                    button.scaleType = ImageView.ScaleType.FIT_CENTER
                    button.adjustViewBounds = true
                    button.background = null
                    button.setPadding(0, 0, 0, 0)
                    var param = button.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(10, 10, 10, 10)
                    button.layoutParams = param
                    button.setBackgroundResource(0)

                    var adlib = ItemsViewModel.adlibs[i]

                    button.setOnClickListener {
                        pop = setFromXML(button, adlib)
                    }
                    button.alpha = 0.7f

                    holder.rappersView.addView(button)
                }
                else {
                    button.layoutParams = LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT, //change the parameters as whatever you want
                    )

                    button.setImageResource(ItemsViewModel.ic)
                    button.scaleType = ImageView.ScaleType.FIT_CENTER
                    button.adjustViewBounds = true
                    button.background = null
                    button.setPadding(0, 0, 0, 0)

                    button.setBackgroundResource(0)

                    button.setColorFilter(Color.parseColor("white"))
                    button.alpha = 0.4f
                    val textView = TextView(context)
                    val frameLayout = FrameLayout(context)
                    frameLayout.addView(button)
                    frameLayout.layoutParams = LayoutParams(
                            context.resources.getDimension(R.dimen.rappers_icon_width).toInt(),
                            LayoutParams.MATCH_PARENT, //change the parameters as whatever you want
                            1.0f
                    )
                    frameLayout.setPadding(0, 0, 0, 0)
                    var param = frameLayout.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(10, 10, 10, 10)
                    frameLayout.layoutParams = param
                    textView.textSize = context.resources.getDimension(R.dimen.rappers_icon_width).toFloat()
                    textView.text = "?"
                    textView.setTextColor(Color.parseColor("#EF9E2D"))
                    frameLayout.addView(textView)
                    textView.gravity = Gravity.CENTER

                    holder.rappersView.addView(frameLayout)

                }
//            setAllParentsClip(button, false)
            }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // To get the data to search Category
    fun filterList(filteredCourseList: ArrayList<RapperData>) {
        this.mList = filteredCourseList;
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

    private fun setAllParentsClip(view: View, enabled: Boolean) {
        var view = view
        while (view.parent != null && view.parent is ViewGroup) {
            val viewGroup = view.parent as ViewGroup
            viewGroup.clipChildren = enabled
            viewGroup.clipToPadding = enabled
            view = viewGroup
            Log.d("tag", "1")
        }
    }
}
