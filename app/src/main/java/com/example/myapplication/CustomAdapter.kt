package com.example.myapplication

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.*
import android.widget.LinearLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import java.time.Duration
import java.util.*


class CustomAdapter(private val context: Context,private var pop : AnimatorSet,private val mList: ArrayList<RapperData>, private var mediaPlayer: MediaPlayer?) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

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
                pop = setFromXML(button,adlib)
            }
            button.scaleType = ImageView.ScaleType.FIT_CENTER
            button.adjustViewBounds = true
            button.background = null
            button.alpha = 0.7f
            button.setPadding(0,0,0,0)
            val param = button.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(10,10,10,10)
            button.layoutParams = param
            button.setBackgroundResource(0)

            holder.rappersView.addView(button)

//            setAllParentsClip(button, false)
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

    fun setFromXML(view: View, adlib: String) : AnimatorSet{
        view.setOnClickListener(null)

        if(mediaPlayer != null) {
            mediaPlayer?.release()
        }
        var resId : Int = context.resources.getIdentifier(adlib,"raw", context.packageName);
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()
        var animDuration = mediaPlayer?.duration!!.toLong()
        pop = popAnim(animDuration, view, pop)

        view.setOnClickListener{
            setFromXML(view,adlib)
        }
        return pop
    }

    private fun popAnim(pause_duration: Long, view:View, pop: AnimatorSet) : AnimatorSet{
        pop.end()
        val popOutZ = ObjectAnimator.ofFloat(view, "translationZ", 1f)
        val popOutX = setAnim(view,"scaleX", 1f,1.1f, 400)
        popOutX.interpolator = OvershootInterpolator()
        val popOutY = setAnim(view,"scaleY", 1f,1.1f, 400)
        popOutY.interpolator = OvershootInterpolator()
        val popOutAlpha = setAnim(view,"Alpha", 0.7f,1f, 50)

        val popOffX = setAnim(view,"scaleX", 1.1f,1f, 200)
        popOffX.interpolator = AccelerateInterpolator()
        val popOffY = setAnim(view,"scaleY", 1.1f,1f, 200)
        popOffY.interpolator = AccelerateInterpolator()
        val popOffAlpha = setAnim(view,"Alpha", 1f,0.7f, 250)
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

    private fun setAnim(view: View, propName:String, valueFrom:Float, valueTo:Float,duration:Long) : ObjectAnimator {
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
